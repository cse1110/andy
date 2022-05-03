package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class CollectionUtilsTest {

    @ParameterizedTest
    @MethodSource("generator")
    void containsAny(String description, Collection<?> coll1, Collection<?> coll2, boolean expectedResult) {
        boolean result = CollectionUtils.isEqualCollection(coll1, coll2);
        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> generator() {
        Arguments t1 = Arguments.of("empty list A", Collections.emptyList(), Arrays.asList(1), false);
        Arguments t2 = Arguments.of("empty list B", Arrays.asList(1), Collections.emptyList(), false);
        Arguments t3 = Arguments.of("same one item A, one item B", Arrays.asList(1), Arrays.asList(1), true);
        Arguments t4 = Arguments.of("different one item A, one item B", Arrays.asList(1), Arrays.asList(2), false);
        Arguments t5 = Arguments.of("A is exactly same as B", Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), true);
        Arguments t6 = Arguments.of("A is equals to B, but elements are different in order", Arrays.asList(1, 2, 3),
                Arrays.asList(1, 3, 2), true);
        Arguments t7 = Arguments.of("A is equals to B, multiple cardinalities", Arrays.asList(1, 2, 3, 3),
                Arrays.asList(3, 1, 3, 2), true);
        Arguments t8 = Arguments.of("A is not equal to B, different cardinalities", Arrays.asList(1, 2, 3, 3),
                Arrays.asList(3, 1, 2), false);
        Arguments t9 = Arguments.of("A larger than B", Arrays.asList(1, 2, 3), Arrays.asList(1, 2), false);
        Arguments t10 = Arguments.of("B larger than A", Arrays.asList(1, 2), Arrays.asList(1, 2, 3), false);
        Arguments t11 = Arguments.of("A and B different cardinality, same size", Arrays.asList(1, 1),
                Arrays.asList(1, 2), false);
        return Stream.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @ParameterizedTest
    @MethodSource("nullGenerator")
    void nullList(String description, Collection<?> coll1, Collection<?> coll2) {
        assertThatThrownBy(() -> CollectionUtils.isEqualCollection(coll1, coll2)).isInstanceOf(Exception.class);
    }

    private static Stream<Arguments> nullGenerator() {
        Arguments tc1 = Arguments.of("c1 null", null, Arrays.asList(1, 4, 5));
        Arguments tc2 = Arguments.of("c2 null", Arrays.asList(1, 2, 3), null);
        Arguments tc3 = Arguments.of("both null", null, null);
        return Stream.of(tc1, tc2, tc3);
    }
}
