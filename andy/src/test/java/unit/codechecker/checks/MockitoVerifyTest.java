package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.MockitoVerify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nl.tudelft.cse1110.andy.codechecker.checks.MockitoVerify.MethodType.AFTEREACH;
import static nl.tudelft.cse1110.andy.codechecker.checks.MockitoVerify.MethodType.TEST;
import static org.assertj.core.api.Assertions.assertThat;

public class MockitoVerifyTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({"add,1,true", "add,2,false", "remove,1,false", "remove,2,true", "contains,3,true", "clear,1,true"})
    void findCallsToVerify(String methodWeExpectAVerify, int numberOfOccurrences, boolean expectation) {
        Check check = new MockitoVerify(methodWeExpectAVerify, TEST, Comparison.EQ, numberOfOccurrences);
        run("MockitoVerifyCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({"add,1,true", "add,2,false"})
    void findCallsToVerifyInAfterEach(String methodWeExpectAVerify, int numberOfOccurrences, boolean expectation) {
        Check check = new MockitoVerify(methodWeExpectAVerify, AFTEREACH, Comparison.EQ, numberOfOccurrences);
        run("MockitoVerifyCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({"remove,1", "contains,2", "get, 1"})
    void checkTheUseOfNever(String methodWeExpectAVerify, int numberOfOccurrences) {
        Check check = new MockitoVerify(methodWeExpectAVerify, TEST, Comparison.EQ, numberOfOccurrences, true);
        run("MockitoVerifyNeverCalls.java", check);
        assertThat(check.result()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"isEmpty", "clear"})
    void times0IsTheSameAsNever(String methodWeExpectAVerify) {
        Check check = new MockitoVerify(methodWeExpectAVerify, TEST, Comparison.EQ, 1, true);
        run("MockitoVerifyNeverCalls.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void ignoreAnonymousClasses() { // was breaking in midterm 2021
        Check check = new MockitoVerify("update", TEST, Comparison.EQ, 2);
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isTrue();
    }

}
