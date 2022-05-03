package delft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUsingAssertThrows {

    @Test
    void myBeautifulTest() {
        String nothing = null;
        assertThrows(NullPointerException.class, () -> nothing.toString());
    }
}
