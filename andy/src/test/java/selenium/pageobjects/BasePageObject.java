package selenium.pageobjects;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Supplier;

public class BasePageObject {
    protected final WebDriver driver;
    protected final String url;

    protected BasePageObject(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
        PageFactory.initElements(driver, this);
    }

    public void navigate() {
        this.driver.navigate().to(url);
    }

    protected void awaitElementVisibility(WebElement... elements) {
        WebDriverWait wait = new WebDriverWait(this.driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    protected void waitUntil(int seconds, Supplier<Boolean> until) {
        WebDriverWait wait = new WebDriverWait(this.driver, seconds);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                try {
                    return until.get();
                } catch (Exception ex) {
                    return false;
                }
            }
        });
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            if (element.isDisplayed()) {
                return true;
            }
        } catch (NoSuchElementException e) {
            // ignore, element div does not exist in DOM
            return false;
        }

        return false;
    }
}
