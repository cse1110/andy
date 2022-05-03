package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class MathArraysTest {

    // In this example test class, we have two compilation failures.
    // On line 21, the method name is wrong.
    // On lines 25 and 33, the variable tc2 is not found, since it has not been declared.

    @ParameterizedTest(name = "{0}")
    @MethodSource("generator")
    void unique(String description, double[] array, double[] expectedArray) {
        assertThat(MathArrays.unique(array)).isEqual(expectedArray);
    }

    private static Stream<Arguments> generator() {
        tc2 = Arguments.of("empty", new double[]{}, new double[]{});
        Arguments tc3 = Arguments.of("nan", new double[]{1, 2, 3, Double.NaN}, new double[]{Double.NaN, 3d, 2d, 1d});
        Arguments tc4 = Arguments.of("no duplicates", new double[]{2d, 1d, 3d}, new double[]{3d, 2d, 1d});
        Arguments tc5 = Arguments.of("single duplicate", new double[]{2d, 3d, 1d, 3d, 3d}, new double[]{3d, 2d, 1d});
        Arguments tc6 = Arguments.of("many duplicates", new double[]{2d, 3d, 1d, 3d, 3d, 2d}, new double[]{3d, 2d, 1d});
        Arguments tc7 = Arguments.of("single element", new double[]{2d}, new double[]{2d});
        Arguments tc8 = Arguments.of("ascending order", new double[]{1d, 2d, 3d}, new double[]{3d, 2d, 1d});
        Arguments tc9 = Arguments.of("descending order", new double[]{3d, 2d, 1d}, new double[]{3d, 2d, 1d});
        return Stream.of(tc2, tc3, tc4, tc5, tc6, tc7, tc8, tc9);
    }

    @Test
    void nullArray() {
        assertThatThrownBy(() -> MathArrays.unique(null)).isInstanceOf(NullPointerException.class);
    }
}






