package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ListUtilsTest {

    // In this example test class, there is no compilation failure.
    // The report should inform the student that the compilation was successful.

    @ParameterizedTest
    @MethodSource("generator")
    void containsAny(String description, List<Integer> coll1, List<Integer> coll2, List<Integer> expectedResult) {
        List<Integer> result = ListUtils.intersection(coll1, coll2);
        Collections.sort(result);
        Collections.sort(expectedResult);
        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> generator() {
        Arguments t1 = Arguments.of("empty list 1", Collections.emptyList(), Arrays.asList(1), Collections.emptyList());
        Arguments t2 = Arguments.of("empty list 2", Arrays.asList(1), Collections.emptyList(), Collections.emptyList());
        Arguments t3 = Arguments.of("0 elements in the intersection", Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6),
                Collections.emptyList());
        Arguments t4 = Arguments.of("1 element in the intersection", Arrays.asList(1, 2, 3), Arrays.asList(3, 4, 5),
                Arrays.asList(3));
        Arguments t5 = Arguments.of("many elements in the intersection", Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4),
                Arrays.asList(2, 3));
        return Stream.of(t1, t2, t3, t4, t5);
    }

    @ParameterizedTest
    @MethodSource("nullGenerator")
    void nullList(String description, List<?> coll1, List<?> coll2) {
        assertThatThrownBy(() -> ListUtils.intersection(coll1, coll2)).isInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> nullGenerator() {
        Arguments tc1 = Arguments.of("c1 null", null, Arrays.asList(1, 4, 5));
        Arguments tc2 = Arguments.of("c2 null", Arrays.asList(1, 2, 3), null);
        Arguments tc3 = Arguments.of("both null", null, null);
        Arguments tc4 = Arguments.of("c1 null c2 empty", null, new ArrayList<Integer>());
        return Stream.of(tc1, tc2, tc3, tc4);
    }

}






