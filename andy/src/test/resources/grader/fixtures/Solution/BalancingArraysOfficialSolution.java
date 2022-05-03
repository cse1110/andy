package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class SplittingTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("generator")
    void split(String name, int[] nums, boolean result) {
        assertThat(Splitting.canBalance(nums)).isEqualTo(result);
    }

    static Stream<Arguments> generator() {
        return Stream.of(
                // True cases
                Arguments.of("Two equal elements", new int[]{2405872, 2405872}, true),
                Arguments.of("Single adds up to two", new int[]{102, 50, 52}, true),
                Arguments.of("Three add up to single", new int[]{67, 33, 11, 111}, true),
                Arguments.of("Three add up to two", new int[]{67, 33, 11, 42, 69}, true),
                Arguments.of("Split outside middle", new int[]{1, 1, 1, 1, 4}, true),

                // False cases
                Arguments.of("Multiple non-splittable elements odd sum", new int[]{1, 2, 3, 4, 5}, false),
                Arguments.of("Multiple non-splittable elements even sum", new int[]{1, 2, 3, 4, 5, 6, 7}, false),
                Arguments.of("half == 0, but odd sum", new int[]{3, 3, 1}, false),

                // pre-conditions
                Arguments.of("Null test", null, false),
                Arguments.of("Empty input list", new int[]{}, false),
                Arguments.of("Single element (0)", new int[]{0}, false),
                Arguments.of("Single element", new int[]{14335}, false)
        );
    }
}
