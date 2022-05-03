package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikArbitrary;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JQWikArbitraryTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "JQWikTestsWithArbitrary,ABC,true",
            "JQWikTestsWithArbitrary,ABCD,false",
            "ManyJQWikTests,ABC,false"
    })
    void returnArbitraries(String fixtureName, String arbitraryName, boolean expectation) {
        Check check = new JQWikArbitrary(arbitraryName);

        run( fixtureName + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource(value={
            "JQWikTestsWithArbitrary,true"
    })
    void returnArbitrariesOfAnyType(String fixtureName, boolean expectation) {
        Check check = new JQWikArbitrary();

        run( fixtureName + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
