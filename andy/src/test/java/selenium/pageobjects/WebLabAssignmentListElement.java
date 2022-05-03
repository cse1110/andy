package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebLabAssignmentListElement {
    private final WebElement parent;

    // Use relative xpaths to find elements
    private final By typeHolder = By.xpath("./td[1]/span");

    private final By assignmentLink = By.xpath("./td[2]/a");

    public WebLabAssignmentListElement(WebElement parent) {
        this.parent = parent;
    }

    public String getAssignmentType() {
        return parent.findElement(typeHolder).getAttribute("data-original-title");
    }

    public String getUrl() {
        return parent.findElement(assignmentLink).getAttribute("href");
    }
}
