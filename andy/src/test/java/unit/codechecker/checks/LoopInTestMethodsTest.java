package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.LoopInTestMethods;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class LoopInTestMethodsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "DoWhileLoop, true",
            "EnhancedForLoop, true",
            "ForLoop, true",
            "WhileLoop, true",
            "LoopOtherMethod, false"
    })
    void loops(String file, boolean expectation) {
        Check check = new LoopInTestMethods();
        run("LoopInTestMethods" + file + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }



}
