package delft;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SeleniumOnePassingOneFailingTest {
    private final String TEST_URL = "https://nonexistentpage.github.io/";

    @Test
    void demoOfAPassingTest() {
        // select which driver to use
        WebDriver browser = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);

        // visit a page
        browser.get(TEST_URL);

        // find an HTML element in the page
        WebElement welcomeHeader = browser.findElement(By.tagName("h1"));

        // assert it contains what we want
        assertThat(welcomeHeader.getText()).isEqualTo("404");

        // close the browser and the selenium session
        browser.close();
    }

    @Test
    void demoOfAFailingTest() {
        // select which driver to use
        WebDriver browser = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);

        // visit a page
        browser.get(TEST_URL);

        // find an HTML element in the page
        WebElement welcomeHeader = browser.findElement(By.tagName("p"));

        // assert it contains what we want
        assertThat(welcomeHeader.getText()).isEqualTo("404");

        // close the browser and the selenium session
        browser.close();
    }

}
