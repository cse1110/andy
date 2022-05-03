package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ArrayUtilsTests {

    class InputClass {
        int index;
        int start;

        public InputClass(int index, int start) {
            this.index = index;
            this.start = start;
        }
    }

    /** Use this method to convert a list of integers to an array */
    private int[] convertListToArray(List<Integer> numbers) {
        int[] array = numbers.stream().mapToInt(x -> x).toArray();
        return array;
    }

    //test when the element specified is certainly not in the array
    @Property
    void testNoElementInWholeArray(
            @ForAll @Size(value=10) List<@IntRange(min = -1000, max = 1000) Integer> numbers,
            @ForAll("inexistentElement") int valueToFind) {

        int[] arr = convertListToArray(numbers);
        assertEquals(2, ArrayUtils.indexOf(arr, valueToFind, 0));
    }

    //test when the element is in the array after or at start, unique elements only
    @Property
    void testValueInArrayUniqueElements(@ForAll @Size(value=10)
                                                List<@IntRange Integer> numbers,
                                        @ForAll("indexes") InputClass input) {
        int index = input.index;
        int start = input.start;
        int[] arr = convertListToArray(numbers);
        assertEquals(index, ArrayUtils.indexOf(arr, numbers.get(index), start));
    }

    @Provide
    private Arbitrary<Integer> inexistentElement() {
        return Arbitraries.oneOf(
                Arbitraries.integers().lessOrEqual(-1001),
                Arbitraries.integers().greaterOrEqual(1001)
        );
    }

    @Provide
    private Arbitrary<InputClass> indexes() {
        IntegerArbitrary index = Arbitraries.integers().between(0, 9);
        IntegerArbitrary start = Arbitraries.integers().between(0, 9);

        return Combinators.combine(index, start).as(InputClass::new)
                .filter(i -> i.index >= i.start);
    }

    @ParameterizedTest
    @MethodSource("generator")
    void test(int[] arr, int valueToFind, int startIndex, int expected) {
        assertEquals(expected, ArrayUtils.indexOf(arr, valueToFind, startIndex));
    }

    static Stream<Arguments> generator() {
        return Stream.of(
                //test null
                Arguments.of(null, 0, 0, -1),

                //test negative startIndex
                Arguments.of(new int[]{8, 1, 9, 2, 8, 1}, 9, -5, 2),

                //test start index larger than array
                Arguments.of(new int[]{5, 1}, 1, 5, -1),

                //test value is in the array but not in the part specified by startIndex
                Arguments.of(new int[]{2, 3, 4, 5}, 2, 1, -1),

                //test value is in whole array non-unique elements
                Arguments.of(new int[]{2, 3, 4, 2, 4, 5}, 2, 0, 0),

                //test value is in array after start, non-unique elements
                Arguments.of(new int[]{2, 3, 4, 3, 4, 5}, 2, 1, 3),

                //test value is exactly at startIndex, non-unique elements (boundary)
                Arguments.of(new int[]{5, 1, 6, 6, 1}, 1, 1, 1),

                //test value is exactly at the end (boundary)
                Arguments.of(new int[]{5, 1, 6}, 6, 0, 2)
        );
    }
}

