package selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import selenium.pageobjects.WebLabSubmissionPage;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static unit.writer.standard.StandardResultTestAssertions.*;

public class ExamAssignmentTests extends WebLabSeleniumTestBase {

    private static final String ASSIGNMENT_EXAM = "89106";

    private String testSubmissionContent;

    @BeforeEach
    public void loadSubmission() throws IOException {
        this.testSubmissionContent = readSubmissionFile("/selenium/solutions/", "Upvote.java");
    }

    @Test
    public void testExamSubmissionOnlyTests() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_EXAM, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.testSubmissionContent);
        webLabSubmissionPage.runOnlyTests();

        String output = webLabSubmissionPage.getOutput();

        assertThat(output)
                .has(compilationSuccess())
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(2))
                .has(noJacocoCoverage())
                .has(noPitestCoverage())
                .has(noCodeChecks())
                .has(not(scoreOfCodeChecks(17, 18)))
                .has(not(codeCheck("tests should have assertions", false, 1)))
                .has(noMetaTests())
                .has(not(metaTestsPassing(2)))
                .has(not(metaTests(3)))
                .has(not(metaTestPassing("does not update")))
                .has(not(metaTestFailing("does not add points if post is not featured")))
                .has(noFinalGrade())
                .has(not(fullGradeDescription("Branch coverage", 2, 2, 0.25)))
                .has(not(fullGradeDescription("Mutation coverage", 3, 4, 0.25)))
                .has(not(fullGradeDescription("Code checks", 17, 18, 0.25)))
                .has(not(fullGradeDescription("Meta tests", 2, 3, 0.25)))
                .has(zeroScoreExplanation())
                .doesNotContain("Final grade:")
                .has(mode("EXAM"))
                .doesNotContain("pitest")
                .doesNotContain("jacoco")
                .contains("Test score: 0/100");
    }

    @ParameterizedTest
    @MethodSource("testExamSubmissionGenerator")
    public void testExamSubmission(Consumer<WebLabSubmissionPage> action) {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_EXAM, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.testSubmissionContent);
        action.accept(webLabSubmissionPage);

        String output = webLabSubmissionPage.getOutput();

        assertThat(output)
                .has(compilationSuccess())
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(2))
                .has(linesCovered(10))
                .has(instructionsCovered(31))
                .has(branchesCovered(2))
                .has(mutationScore(3, 4))
                .has(noCodeChecks())
                .has(not(scoreOfCodeChecks(17, 18)))
                .has(not(codeCheck("tests should have assertions", false, 1)))
                .has(noMetaTests())
                .has(not(metaTestsPassing(2)))
                .has(not(metaTests(3)))
                .has(not(metaTestPassing("does not update")))
                .has(not(metaTestFailing("does not add points if post is not featured")))
                .has(noFinalGrade())
                .has(not(fullGradeDescription("Branch coverage", 2, 2, 0.25)))
                .has(not(fullGradeDescription("Mutation coverage", 3, 4, 0.25)))
                .has(not(fullGradeDescription("Code checks", 17, 18, 0.25)))
                .has(not(fullGradeDescription("Meta tests", 2, 3, 0.25)))
                .has(zeroScoreExplanation())
                .doesNotContain("Final grade:")
                .has(mode("EXAM"))
                .contains("pitest")
                .contains("jacoco")
                .contains("Test score: 0/100");
    }

    private static Stream<Arguments> testExamSubmissionGenerator() {
        return Stream.of(
                Arguments.of((Consumer<WebLabSubmissionPage>) WebLabSubmissionPage::runWithCoverage),
                Arguments.of((Consumer<WebLabSubmissionPage>) WebLabSubmissionPage::assessWithoutHints),
                Arguments.of((Consumer<WebLabSubmissionPage>) WebLabSubmissionPage::assessWithHints)
        );
    }
}
