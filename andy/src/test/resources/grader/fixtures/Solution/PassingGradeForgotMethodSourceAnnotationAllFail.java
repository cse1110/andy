package delft;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import delft.PassingGrade;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

// No tests are detected, since student left out @MethodSource annotation
class PassingGradeTest {

    @ParameterizedTest
//    @MethodSource("generator")
    void passed(float grade, boolean pass) {
        boolean result = new PassingGrade().passed(grade);
        assertEquals(pass, result);
    }

    private static Stream<Arguments> generator() {
        return Stream.of(
                Arguments.of(1.0f, false), // out-point
                Arguments.of(4.9f, false), // boundary: off-point
                Arguments.of(5.0f, true), // boundary: on-point
                Arguments.of(10.0f, true), // boundary: max value
                Arguments.of(7.5f, true)    // extra in-point (small test suite)
        );
    }
}