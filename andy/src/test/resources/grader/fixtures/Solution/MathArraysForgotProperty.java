package delft;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.UniqueElements;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

// Student forgot @Property annotation, causing no tests to be detected -> 0/0 to pass.
class MathArraysPBTest {

//    @Property
        // an array of size 100 with doubles between [1,20] will definitely have repeated numbers
        // if you use only @ForAll, you are likely to never have repeated elements in the array, and thus,
        // never exercising the full property!
    void unique(
            @ForAll
            @Size(value = 100)
                    List<@DoubleRange(min = 1, max = 20) Double> numbers) {

        double[] doubles = convertListToArray(numbers);
        double[] result = MathArrays.unique(doubles);

        assertThat(result)
                .contains(doubles) // contains all the elements
                .doesNotHaveDuplicates() // no duplicates
                .isSortedAccordingTo(Comparator.reverseOrder()); // in descending order
    }

    /** Use this method to convert a list of integers to an array */
    private double[] convertListToArray(List<Double> numbers) {
        double[] array = numbers.stream().mapToDouble(x -> x).toArray();
        return array;
    }
}
