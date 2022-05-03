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

class ArrayUtilsSimpleTests {

    /** Use this method to convert a list of integers to an array */
    private int[] convertListToArray(List<Integer> numbers) {
        int[] array = numbers.stream().mapToInt(x -> x).toArray();
        return array;
    }

    /**Test when the element specified is certainly not in the array.
     * The asserted index should be -1 instead of 2 for this to work.
     */
    @Property
    void testNoElementInWholeArray(
            @ForAll @Size(value=10) List<@IntRange(min = -1000, max = 1000) Integer> numbers,
            @ForAll("inexistentElement") int valueToFind) {

        int[] arr = convertListToArray(numbers);
        assertEquals(2, ArrayUtils.indexOf(arr, valueToFind, 0));
    }

    @Provide
    private Arbitrary<Integer> inexistentElement() {
        return Arbitraries.oneOf(
                Arbitraries.integers().lessOrEqual(-1001),
                Arbitraries.integers().greaterOrEqual(1001)
        );
    }

}

