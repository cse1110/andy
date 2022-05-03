package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

// @BeforeAll methods should be static
class PiecewiseTest {

    Piecewise piecewise;

    @BeforeAll
    void setup(){
        piecewise = new Piecewise();
    }

    /**
     * Parameterized test for boundaries of the piecewise
     * in range checking method.
     *
     * @param x  x coordinate
     * @param y  y coordinate
     * @param expected  expected result (boolean)
     */
    @ParameterizedTest
    @CsvSource({
            "1, 5, false",  // x lower boundary (out point)
            "2, 5, true",   // x lower boundary (in point)
            "10, 2, true",  // x upper boundary (in point)
            "11, 2, false", // x upper boundary (out point)
            "3, 1, true",   // y lower boundary (in point)
            "3, 0, false",  // y lower boundary (out point)
            "3, 10, true",  // y upper boundary (in point)
            "3, 11, false", // y upper boundary (out point)
            "4, 10, true",  // min y, max x (in bounds)
            "5, 10, false", // y > 14 - x
            "10, 4, true",  // y == 14 - x
            "11, 4, false"  // y < 14 - x
    })
    public void testPiecewiseBoundaryTests(int x, int y, boolean expected) {
        testPiecewiseHelper(x, y, expected);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, false",   // x lower boundary (out point)
            "2, 1, true",    // x lower boundary (in point)
            "10, 1, true",   // x upper boundary (in point)
            "11, 1, false",  // x upper boundary (out point)
            "2, 0, false",   // y lower boundary (out point)
            "2, 10, true",   // y upper boundary (in point)
            "2, 11, false",  // y upper boundary (out point)
            "4, 10, true",   // y == 14 - x     <-- max x, max y
            "4, 11, false",  // y > 14 - x
            "10, 4, true",   // y == 14 - x     <-- max x, max y
            "10, 5, false",  // y > 14 - x
            "11, 4, false",  // y > 14 - x
            "1, 10, false",  // y < 14 - x (x out)
            "5, 10, false"   // y > 14 - x
    })
    public void testPiecewiseBoundaryChartTests(int x, int y, boolean expected) {
        testPiecewiseHelper(x, y, expected);
    }

    private void testPiecewiseHelper(int x, int y, boolean expected) {
        boolean result = piecewise.isInRange(x, y);
        assertEquals(expected, result, x + ", " + y + " in range: " + expected);
    }
}