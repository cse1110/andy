package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JQWikPropertyTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "1, true",
            "2, true",
            "3, true",
            "4, false",
    })
    void classWithProperty(int minimumNumber, boolean expectation) {
        Check check = new JQWikProperty(Comparison.GTE, minimumNumber);
        run("ManyJQWikTests.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @Test
    void classWithoutProperty() {
        Check check = new JQWikProperty(Comparison.GTE, 1);
        run("NoTests.java", check);
        assertThat(check.result()).isFalse();
    }

}
