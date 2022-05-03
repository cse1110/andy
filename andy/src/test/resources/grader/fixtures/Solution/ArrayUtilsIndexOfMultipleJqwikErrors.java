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

class ArrayUtilsMultipleTests {

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
}

