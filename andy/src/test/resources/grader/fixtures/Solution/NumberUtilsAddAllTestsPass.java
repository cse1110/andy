package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


//  --- JUnit execution
//      4/4 passed
class NumberUtilsTests {

    @ParameterizedTest
    @MethodSource("digitsOutOfRange")
    void shouldThrowExceptionWhenDigitsAreOutOfRange(List<Integer> left, List<Integer> right) {
        assertThatThrownBy(() -> add(left, right))
                .isInstanceOf(IllegalArgumentException.class);

    }

    static Stream<Arguments> digitsOutOfRange() {
        return Stream.of(
                Arguments.of(numbers(1,-1,1), numbers(1,1,1)),
                Arguments.of(numbers(1,1,1), numbers(1,-1,1)),
                Arguments.of(numbers(1,11,1), numbers(1,1,1)),
                Arguments.of(numbers(1,1,1), numbers(1,11,1)));
    }

    // returns a mutable list of integers
    private static List<Integer> numbers(int... nums) {
        List<Integer> list = new ArrayList<>();
        for(int n : nums)
            list.add(n);
        return list;
    }

    private static List<Integer> add(List<Integer> left, List<Integer> right) {
        return NumberUtils.add(left, right);
    }
}

