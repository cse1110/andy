package unit.codechecker;

import static nl.tudelft.cse1110.andy.utils.ResourceUtils.resourceFolder;

public class CodeCheckerTestUtils {
    public String getTestResource(String fixtureName) {
        return resourceFolder("/codechecker/fixtures/") + fixtureName;
    }

}
