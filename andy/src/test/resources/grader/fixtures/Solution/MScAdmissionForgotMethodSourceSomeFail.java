package delft;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Method source is not provided... 3/3 pass
class MScAdmissionTest {

    private final MScAdmission admission = new MScAdmission();

    @ParameterizedTest
    @MethodSource("validGenerator")
    void validInputs(int act, double gpa, boolean expectedResult) {
        boolean admit = admission.admit(act, gpa);

        assertThat(admit).isEqualTo(expectedResult);
    }

    @ParameterizedTest
//    @MethodSource("invalidGenerator")
    void invalidInputs(int act, double gpa) {
        assertThatThrownBy(() -> {
            admission.admit(act, gpa);
        })
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("has to be between");
    }

    // ACT and GPA are tightly coupled: you quickly get into another equivalent class
    // we base the classes on the restrictions
    private static Stream<Arguments> validGenerator() {
        return Stream.of(
                Arguments.of(35,3.5, false), // off-point 3.5 ACT
                Arguments.of(36,3.5, true),  // both on-points
                Arguments.of(36,3.4, false)
        );
    }


    // ACT has to be between [0,36]
    // GPA has to be between [0, 4.0]
    private static Stream<Arguments> invalidGenerator() {
        return Stream.of(
                Arguments.of(-1,4.0),
                Arguments.of(40,4.0)
        );
    }
}
