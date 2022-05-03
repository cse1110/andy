package delft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AllTestsWithAssertions {

    @Test
    void t1() {
        // junit assertion
        Assertions.assertEquals(2,1+1);
        Assertions.assertEquals(3,1+2);
    }

    @ParameterizedTest
    void t2() {
        // assertj assertion
        assertThat(1+1).isEqualTo(auxiliaryMethod());
    }

    private int auxiliaryMethod() {
        return 2;
    }
}
