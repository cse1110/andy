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
import java.lang.IllegalArgumentException;

class NumberUtilsTest {

    @Property
    void testAddition(@ForAll int a, @ForAll int b) {
        assertEquals(a + b, NumberUtils.addPositive(a, b));
    }

    @Property
    void testNegativeAdddition(@ForAll @IntRange(max = -1) int a, @ForAll @IntRange(max = -1) int b) {
        assertThrows(IllegalArgumentException.class, () -> {
           NumberUtils.addPositive(a, b);
        });
    }

}

