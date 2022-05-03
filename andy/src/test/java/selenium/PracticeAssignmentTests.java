package selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import selenium.pageobjects.WebLabSubmissionPage;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static unit.writer.standard.StandardResultTestAssertions.*;

public class PracticeAssignmentTests extends WebLabSeleniumTestBase {

    private static final String ASSIGNMENT_PRACTICE = "89104";

    private String testSubmissionContent;

    @BeforeEach
    public void loadSubmission() throws IOException {
        this.testSubmissionContent = readSubmissionFile("/selenium/solutions/", "Upvote.java");
    }

    @Test
    public void testPracticeSubmissionOnlyTests() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_PRACTICE, USER_ID));

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
                .has(noMetaTests())
                .has(noFinalGrade())
                .has(zeroScoreExplanation())
                .has(mode("PRACTICE"))
                .doesNotContain("pitest")
                .doesNotContain("jacoco")
                .contains("Test score: 0/100");
    }

    @Test
    public void testPracticeSubmissionCoverage() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_PRACTICE, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.testSubmissionContent);
        webLabSubmissionPage.runWithCoverage();

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
                .has(noMetaTests())
                .has(noFinalGrade())
                .has(zeroScoreExplanation())
                .has(mode("PRACTICE"))
                .contains("pitest")
                .contains("jacoco")
                .contains("Test score: 0/100");
    }

    @Test
    public void testPracticeSubmissionWithoutHints() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_PRACTICE, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.testSubmissionContent);
        webLabSubmissionPage.assessWithoutHints();

        String output = webLabSubmissionPage.getOutput();

        assertThat(output)
                .has(compilationSuccess())
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(2))
                .has(linesCovered(10))
                .has(instructionsCovered(31))
                .has(branchesCovered(2))
                .has(mutationScore(3, 4))
                .has(scoreOfCodeChecks(17, 18))
                .has(not(codeCheck("tests should have assertions", false, 1)))
                .has(not(codeCheck("getPoints should have an assertion", true, 3)))
                .has(metaTestsPassing(2))
                .has(metaTests(3))
                .has(not(metaTestPassing("does not update")))
                .has(not(metaTestFailing("does not add points if post is not featured")))
                .has(fullGradeDescription("Branch coverage", 2, 2, 0.25))
                .has(fullGradeDescription("Mutation coverage", 3, 4, 0.25))
                .has(fullGradeDescription("Code checks", 17, 18, 0.25))
                .has(fullGradeDescription("Meta tests", 2, 3, 0.25))
                .contains("Final grade: 84/100")
                .has(mode("PRACTICE"))
                .contains("pitest")
                .contains("jacoco")
                .contains("Test score: 84/100");
    }

    @Test
    public void testPracticeSubmissionWithHints() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_PRACTICE, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.testSubmissionContent);
        webLabSubmissionPage.assessWithHints();

        String output = webLabSubmissionPage.getOutput();

        assertThat(output)
                .has(compilationSuccess())
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(2))
                .has(linesCovered(10))
                .has(instructionsCovered(31))
                .has(branchesCovered(2))
                .has(mutationScore(3, 4))
                .has(scoreOfCodeChecks(17, 18))
                .has(codeCheck("UserRepository should be mocked", true, 1))
                .has(codeCheck("Scoring should be mocked", true, 1))
                .has(codeCheck("StackOverflow should not be mocked", true, 1))
                .has(codeCheck("Post should not be mocked", true, 1))
                .has(codeCheck("Spies should not be used", true, 1))
                .has(codeCheck("pointsForFeaturedPost should be set up", true, 2))
                .has(codeCheck("pointsForNormalPost should be set up", true, 2))
                .has(codeCheck("set up pointsForFeaturedPost just once", true, 1))
                .has(codeCheck("set up pointsForNormalPost just once", true, 1))
                .has(codeCheck("update should be verified in both tests", true, 3))
                .has(codeCheck("tests should have assertions", false, 1))
                .has(codeCheck("getPoints should have an assertion", true, 3))
                .has(metaTestsPassing(2))
                .has(metaTests(3))
                .has(metaTestPassing("does not update"))
                .has(metaTestFailing("does not add points if post is not featured"))
                .has(metaTestPassing("change condition"))
                .has(fullGradeDescription("Branch coverage", 2, 2, 0.25))
                .has(fullGradeDescription("Mutation coverage", 3, 4, 0.25))
                .has(fullGradeDescription("Code checks", 17, 18, 0.25))
                .has(fullGradeDescription("Meta tests", 2, 3, 0.25))
                .contains("Final grade: 84/100")
                .has(mode("PRACTICE"))
                .contains("pitest")
                .contains("jacoco")
                .contains("Test score: 84/100");
    }
}
