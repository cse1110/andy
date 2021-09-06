package selenium;

import nl.tudelft.cse1110.andy.utils.ResourceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import selenium.pageobjects.WebLabLoginPage;
import selenium.pageobjects.WebLabSubmissionPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static testutils.WebLabTestAssertions.*;

public class WebLabSeleniumTest {
    // Remember to set the credentials as an environment variable ("username:password")
    // Only works with local WebLab accounts, NOT netid
    private static final String WEBLAB_CREDENTIALS_ENV_VAR = "WEBLAB_CREDENTIALS";
    private static final String WEBLAB_URL = "https://weblab.tudelft.nl";
    private static final String WEBLAB_LOGIN_PATH = "/signin";

    private static final String COURSE_ID = "cse1110";
    private static final String EDITION_ID = "sandbox";
    private static final String ASSIGNMENT_PRACTICE = "89104";
    private static final String SUBMISSION_PRACTICE = "36461";

    private static final String WEBLAB_SUBMISSION_PATH = "/" + COURSE_ID +
                                                         "/" + EDITION_ID +
                                                         "/assignment/%s/submission/%s/edit";

    private WebDriver driver;
    private String weblabUsername;
    private String weblabPassword;

    private String submissionContent;

    @BeforeEach
    public void setup() throws IOException {
        this.driver = new FirefoxDriver();

        String[] weblabCredentials = System.getenv(WEBLAB_CREDENTIALS_ENV_VAR).split(":");
        this.weblabUsername = weblabCredentials[0];
        this.weblabPassword = weblabCredentials[1];

        this.submissionContent = Files.readString(Path.of(
                ResourceUtils.resourceFolder("/selenium/solutions/") + "Upvote.java"));

        this.login();
    }

    @AfterEach
    public void cleanup() {
        driver.quit();
    }

    private void login() {
        WebLabLoginPage loginPage = new WebLabLoginPage(this.driver, WEBLAB_URL + WEBLAB_LOGIN_PATH);
        loginPage.navigate();
        loginPage.login(this.weblabUsername, this.weblabPassword);
    }

    @Test
    public void testPracticeSubmission() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_PRACTICE, SUBMISSION_PRACTICE));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.submissionContent);
        webLabSubmissionPage.runSpecTests();

        String output = webLabSubmissionPage.getOutput();

        webLabSubmissionPage.submitSolution();

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