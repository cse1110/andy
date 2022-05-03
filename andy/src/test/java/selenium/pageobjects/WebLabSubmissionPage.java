package selenium.pageobjects;

import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WebLabSubmissionPage extends BasePageObject {

    public WebLabSubmissionPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[1]/div")
    private WebElement solutionDiv;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[1]/div/span/form/div/div[1]/textarea")
    private WebElement solutionTextAreaHidden;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/span")
    private WebElement saveButton;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[2]")
    private WebElement runOnlyTestsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[3]")
    private WebElement runWithCoverageBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[4]")
    private WebElement assessWithoutHintsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[5]")
    private WebElement assessWithHintsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/div/div/pre")
    private WebElement output;

    public void runOnlyTests() {
        this.runOnlyTestsBtn.click();
        this.awaitExecutionStart();
    }

    public void runWithCoverage() {
        this.runWithCoverageBtn.click();
        this.awaitExecutionStart();
    }

    public void assessWithoutHints() {
        this.assessWithoutHintsBtn.click();
        this.awaitExecutionStart();
    }

    public void assessWithHints() {
        this.assessWithHintsBtn.click();
        this.awaitExecutionStart();
    }

    public void enterSolution(String solution) {
        this.awaitElementVisibility(solutionDiv);

        // No way to set content without javascript: element is not visible.
        String script = String.format("document.getElementById('%s').value = '%s';",
                solutionTextAreaHidden.getAttribute("id"),
                StringEscapeUtils.escapeJavaScript(solution));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);

        this.saveButton.click();

        // Wait a few seconds for the submission to be processed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        waitUntil(15, () -> saveButton.getText().equals("Saved"));
    }

    public String getOutput() {
        // Wait until WebLab finishes processing submission
        waitUntil(90, () -> output.getText().contains("Status: Done"));
        return this.output.getText();
    }

    private void awaitExecutionStart() {
        waitUntil(30, () -> !output.getText().contains("Status: Done"));
    }
}
