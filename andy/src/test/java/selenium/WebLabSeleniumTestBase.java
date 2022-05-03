package selenium;

import nl.tudelft.cse1110.andy.utils.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import selenium.pageobjects.WebLabLoginPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class WebLabSeleniumTestBase {
    // Remember to set the credentials as an environment variable ("username:password")
    // Only works with local WebLab accounts, NOT netid
    private static final String WEBLAB_CREDENTIALS_ENV_VAR = "WEBLAB_CREDENTIALS";
    protected static final String WEBLAB_URL = "https://weblab.tudelft.nl";
    private static final String WEBLAB_LOGIN_PATH = "/signin";

    protected static final String USER_ID = "36461";

    protected static final String WEBLAB_ASSIGNMENT_BASE_PATH = "///assignment/%s/";

    protected static final String WEBLAB_SUBMISSION_PATH = WEBLAB_ASSIGNMENT_BASE_PATH + "submission/%s/edit";

    protected static final String WEBLAB_ASSIGNMENT_VIEW_PATH = WEBLAB_ASSIGNMENT_BASE_PATH + "view";

    protected static final String WEBLAB_ANSWER_PATH = WEBLAB_ASSIGNMENT_BASE_PATH + "answer";

    private static final String WEBLAB_SELENIUM_HEADLESS_ENV_VAR = "WEBLAB_SELENIUM_HEADLESS";

    protected WebDriver driver;
    private String weblabUsername;
    private String weblabPassword;

    @BeforeAll
    public void setup() throws IOException {
        String credentialsString = System.getenv(WEBLAB_CREDENTIALS_ENV_VAR);
        String[] weblabCredentials = credentialsString != null ? credentialsString.split(":") : null;

        if (weblabCredentials == null || weblabCredentials.length != 2) {
            fail("WebLab credentials are not configured. Provide a local WebLab username and " +
                 "password in the \"" + WEBLAB_CREDENTIALS_ENV_VAR + "\" environment variable in the format " +
                 "\"username:password\" or \"email:password\". The provided user has to be enrolled in the " +
                 "course containing the test assignments.");
            return;
        }

        this.weblabUsername = weblabCredentials[0];
        this.weblabPassword = weblabCredentials[1];

        FirefoxOptions options = new FirefoxOptions();
        if (StringUtils.isNotEmpty(System.getenv(WEBLAB_SELENIUM_HEADLESS_ENV_VAR))) {
            options.setHeadless(true);
        }

        this.driver = new FirefoxDriver(options);

        this.login();
    }

    @AfterAll
    public void cleanup() {
        driver.quit();
    }

    protected String readSubmissionFile(String path, String filename) throws IOException {
        return Files.readString(Path.of(
                ResourceUtils.resourceFolder(path) + filename));
    }

    private void login() {
        WebLabLoginPage loginPage = new WebLabLoginPage(this.driver, WEBLAB_URL + WEBLAB_LOGIN_PATH);
        loginPage.navigate();
        String redirectUrl = loginPage.login(this.weblabUsername, this.weblabPassword);
        if (!redirectUrl.equals(WEBLAB_URL + "/")) {
            fail("Could not log into WebLab. Are the provided credentials valid?");
        }
    }
}
