package selenium;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import selenium.pageobjects.WebLabAnswerPage;
import selenium.pageobjects.WebLabAssignmentListPage;
import selenium.pageobjects.WebLabSubmissionPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static unit.writer.standard.StandardResultTestAssertions.compilationSuccess;
import static unit.writer.standard.StandardResultTestAssertions.finalGrade;

public class AllExerciseTests extends WebLabSeleniumTestBase {

    protected static final String BASE_ASSIGNMENT_ID = "91800";

    @ParameterizedTest
    @MethodSource("scrapeAssignmentIds")
    public void testAllAssignments(String assignmentId) {
        WebLabAnswerPage webLabAnswerPage = new WebLabAnswerPage(driver,
                WEBLAB_URL + String.format(WEBLAB_ANSWER_PATH, assignmentId));

        webLabAnswerPage.navigate();

        String solution = webLabAnswerPage.getSolution();

        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, assignmentId, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(solution);
        webLabSubmissionPage.assessWithHints();

        String output = webLabSubmissionPage.getOutput();

        assertThat(output)
                .has(compilationSuccess())
                .has(finalGrade(100))
                .contains("Test score: 100/100");
    }

    public Stream<Arguments> scrapeAssignmentIds() {
        return scrapeAssignmentIds(WEBLAB_URL + String.format(WEBLAB_ASSIGNMENT_VIEW_PATH, BASE_ASSIGNMENT_ID));
    }

    private Stream<Arguments> scrapeAssignmentIds(String baseAssignmentUrl) {
        WebLabAssignmentListPage listPage = new WebLabAssignmentListPage(driver, baseAssignmentUrl);
        listPage.navigate();

        // Scrape programming assignments
        Stream<Arguments> assignmentIds = listPage.getAssignmentUrls().stream()
                .map(AllExerciseTests::getAssignmentId)
                .map(Arguments::of);

        // Recursively scrape assignments in subdirectories
        for (String subdirectoryUrl : listPage.getSubdirectoryUrls()) {
            Stream<Arguments> subdirectoryAssignmentIds = scrapeAssignmentIds(subdirectoryUrl);
            assignmentIds = Stream.concat(assignmentIds, subdirectoryAssignmentIds);
        }

        return assignmentIds;
    }

    private static String getAssignmentId(String path) {
        Pattern pattern = Pattern.compile("/assignment/(\\d+)/");
        Matcher matcher = pattern.matcher(path);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not parse path");
        }

        return matcher.group(1);
    }
}
