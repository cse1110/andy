package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikArbitraries;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JQWikArbitrariesTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "JQWikUsingArbitraries,integers,true",
            "JQWikUsingArbitraries,doubles,true",
            "JQWikUsingArbitraries,strings,false",
            "ManyJQWikTests,integers,false"
    })
    void useOfArbitraries(String fixtureName, String arbitraryName, boolean expectation) {
        Check check = new JQWikArbitraries(arbitraryName);
        run( fixtureName + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
