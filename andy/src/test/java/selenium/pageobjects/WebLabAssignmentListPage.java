package selenium.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class WebLabAssignmentListPage extends BasePageObject {

    public WebLabAssignmentListPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(className = "majorrow")
    private List<WebElement> subAssignments;

    @FindBy(className = "masterrow")
    private List<WebElement> assignmentHeaders; // used to check whether the table has fully loaded

    public List<String> getSubdirectoryUrls() {
        return getSubAssignmentUrls("Collection of Assignments");
    }

    public List<String> getAssignmentUrls() {
        return getSubAssignmentUrls("Programming question (docker:cse1110-andy)");
    }

    private List<String> getSubAssignmentUrls(String type) {
        this.awaitListLoaded();

        // Convert subassignment rows to WebLabAssignmentListElement objects,
        // filter by type,
        // and finally extract the url.
        return subAssignments.stream()
                .map(WebLabAssignmentListElement::new)
                .filter(element -> element.getAssignmentType().equals(type))
                .map(WebLabAssignmentListElement::getUrl)
                .collect(Collectors.toList());
    }

    private void awaitListLoaded() {
        waitUntil(30, () -> !assignmentHeaders.isEmpty());
    }
}
