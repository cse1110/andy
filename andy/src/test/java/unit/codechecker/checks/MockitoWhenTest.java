package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.MockitoWhen;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoWhenTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({
            "add,0,false", "add,1,true", "add,2,false",
            "remove,0,false","remove,1,true","remove,2,false",
            "contains,3,true", "contains,2,false", "contains,4,false",
            "equals,1,false","equals,2,true","equals,3,false"
    })
    void findCallsToWhen(String methodThatWeExpectAMockitoWhen, int numberOfOccurrences, boolean expectation) {
        Check check = new MockitoWhen(methodThatWeExpectAMockitoWhen, Comparison.EQ, numberOfOccurrences);
        run("MockitoWhenCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
