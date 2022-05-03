package delft;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

// This test class contains normal @Tests, parameterized tests and pbt.
class ArrayUtilsTests {



    @Test
    void negativeIndexSimplyBecomesAZero() {
        assertThat(ArrayUtils.indexOf(new int[]{1, 2, 3, 4, 5}, 4, -1)).isEqualTo(3);
    }




    @ParameterizedTest
    @MethodSource("invalidGenerator")
    void invalidTest(int[] array, int valueToFind, int startIndex) {
        assertThat(ArrayUtils.indexOf(array, valueToFind, startIndex)).isEqualTo(-1);
    }

    private static Stream<Arguments> invalidGenerator() {
        return Stream.of(
                /* T01 */ Arguments.of(null, 37, 0),
                /* T02 */ Arguments.of(new int[0], 37, 0),
                /* T03 */ Arguments.of(new int[]{1, 2, 3, 4, 5}, 6, 0)
        );
    }




    @Property
    void indexOfShouldFindFirstValue(
            /*
             * we generate a list with 20 numbers, ranging from -1000, 1000. Range chosen
             * randomly.
             */
            @ForAll @Size(value = 20) List<@IntRange(min = -1000, max = 1000) Integer> numbers,
            /**
             * we generate a random number that we'll insert in the list. This number is
             * outside the range of the list, so that we can easily find it.
             */
            @ForAll @IntRange(min = 1001, max = 2000) int value,
            /** We randomly pick a place to put the element in the list */
            @ForAll @IntRange(max = 19) int indexToAddElement,
            /** We randomly pick a number to start the search in the array */
            @ForAll @IntRange(max = 19) int startIndex) {
        /* we add the number to the list in the randomly chosen position */
        numbers.add(indexToAddElement, value);
        /*
         * we add the number at the end of the list too, so there will always be the
         * element somewhere
         */
        numbers.add(value);
        /* we convert the list to an array, as expected by the method */
        int[] array = convertListToArray(numbers);
        /**
         * if we added the element after the start index, then, we expect the method to
         * return the position where we inserted the element. Else, we expect the method
         * to return the index of the last element in the array.
         */
        int expectedIndex = indexToAddElement >= startIndex ? indexToAddElement : array.length - 1;
        assertThat(ArrayUtils.indexOf(array, value, startIndex)).isEqualTo(expectedIndex);
    }

    /** Use this method to convert a list of integers to an array */
    private int[] convertListToArray(List<Integer> numbers) {
        int[] array = numbers.stream().mapToInt(x -> x).toArray();
        return array;
    }
}
