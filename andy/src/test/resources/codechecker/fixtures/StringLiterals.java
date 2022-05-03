package delft;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

public class StringLiterals {

    @Provide
    @DisplayName("super long name to see if it's captured")
    Arbitrary<String> twoCharNoPalin() {
        List<String> strings = Arrays.asList("ac", "ad", "ae", "ef", "ab", "be", "za", "bf", "tu", "ui", "eq", "de", "ma", "za", "ei", "pi", "oe");
        Arbitrary<String> stringList = Arbitraries.of(strings);
        return stringList;
    }
}
