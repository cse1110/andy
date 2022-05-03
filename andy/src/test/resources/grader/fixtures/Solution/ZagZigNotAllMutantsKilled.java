package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


// This solution kills only 24 out of the 28 identified mutants.
// 2 of the 28 cannot be killed, so 26 will count as 100%.
// "changed conditional boundary" mutant on line 13 survives.
// "changed conditional boundary" mutant on line 15 survives.
class ZagZigTest {


    @ParameterizedTest
    @CsvSource({
            "A,A",
            "AB,AB"
    })
    void singleRow(String s, String expected) {
        assertThat(new ZagZig().zagzig(s,1))
                .isEqualTo(expected);
    }

    @Test
    void multipleRows() {
        assertThat(new ZagZig().zagzig("PAYPALISHIRING",2))
                .isEqualTo(
                        "APLSIIG\n" +
                                "PYAIHRN");

        assertThat(new ZagZig().zagzig("PAYPALISHIRING",3))
                .isEqualTo(
                        "Y I R\n" +
                                "APLSIIG\n" +
                                "P A H N");

        assertThat(new ZagZig().zagzig("PAYPALISHIRING",4))
                .isEqualTo(
                        "P  I\n" +
                                "YA HR\n" +
                                "A LS IG\n" +
                                "P  I  N");

        assertThat(new ZagZig().zagzig("PAYPALISHIRING",5))
                .isEqualTo(
                        "A   N\n" +
                                "PL  IG\n" +
                                "Y I R\n" +
                                "A  SI\n" +
                                "P   H");


    }

    @Test
    void moreRowsThanChars() {
        assertThat(new ZagZig().zagzig("ABC",4))
                .isEqualTo("C\nB\nA");
    }


}