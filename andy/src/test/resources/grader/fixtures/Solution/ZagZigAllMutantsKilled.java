package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


// This solution kills 26 out of the 28 identified mutants.
// Because 2 of the 28 mutants cannot be killed, 26 killed mutants will counts as 100%.
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

    @Test
    void invalidStrings() {
        assertThatThrownBy(() -> new ZagZig().zagzig("", 1))
                .isInstanceOf(IllegalArgumentException.class);

        StringBuilder longString = new StringBuilder();

        // <= 1000
        for(int i = 0; i < 1000; i++) {
            longString.append("a");
        }
        assertThat(new ZagZig().zagzig(longString.toString(), 1)).isEqualTo(longString.toString());

        // > 1000
        longString.append("a");
        assertThatThrownBy(() -> new ZagZig().zagzig(longString.toString(), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalidNumberOfRows() {
        assertThatThrownBy(() -> new ZagZig().zagzig("somestring", 0))
                .isInstanceOf(IllegalArgumentException.class);

        // <= 1000
        assertThat(new ZagZig().zagzig("SOMESTRING", 1000)).isEqualTo("G\nN\nI\nR\nT\nS\nE\nM\nO\nS");
        // > 1000
        assertThatThrownBy(() -> new ZagZig().zagzig("somestring", 1001))
                .isInstanceOf(IllegalArgumentException.class);

    }


}
