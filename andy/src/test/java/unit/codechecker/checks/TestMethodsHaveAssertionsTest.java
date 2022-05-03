package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.TestMethodsHaveAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMethodsHaveAssertionsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({"AllTestsWithAssertions,true", "TestMissingAssertion,false", "TestUsingAssertThrows,true",
            "StackOverflowTestWithAnonymousClass,true" // this was breaking a student solution in midterm 2021
    })
    void lookForAssertions(String classToRun, boolean expectation) {
        Check check = new TestMethodsHaveAssertions();
        run(classToRun + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
