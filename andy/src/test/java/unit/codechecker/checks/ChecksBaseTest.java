package unit.codechecker.checks;

import unit.codechecker.CodeCheckerTestUtils;
import unit.codechecker.JDTParser;
import nl.tudelft.cse1110.andy.codechecker.checks.Check;

public class ChecksBaseTest {

    protected void run(String className, Check check) {
        new JDTParser().run(new CodeCheckerTestUtils().getTestResource(className), check);
    }

}
