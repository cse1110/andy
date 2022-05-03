package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparisonTest {

    @ParameterizedTest
    @CsvSource({
            "LT,1,2,true","LT,2,1,false",
            "LTE,1,2,true","LTE,2,1,false", "LTE,2,2,true",
            "GT,1,2,false","GT,2,1,true",
            "GTE,1,2,false","GTE,2,1,true", "LTE,2,2,true",
            "EQ,1,2,false","EQ,1,1,true"
    })
    void comparison(String operator, int actual, int expected, boolean result) {
        // TODO: make this better.
        Comparison comparison = null;
        if(operator.equals("LT"))
            comparison = Comparison.LT;
        if(operator.equals("LTE"))
            comparison = Comparison.LTE;
        if(operator.equals("GT"))
            comparison = Comparison.GT;
        if(operator.equals("GTE"))
            comparison = Comparison.GTE;
        if(operator.equals("EQ"))
            comparison = Comparison.EQ;

        assertThat(comparison.compare(actual, expected))
                .isEqualTo(result);
    }
}
