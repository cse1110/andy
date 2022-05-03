package delft;

import java.lang.IllegalArgumentException;

class NumberUtils {

    private NumberUtils() {
        // Empty constructor
    }

    public static int addPositive(int a, int b) {
        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException();
        }
        return a + b;
    }
}
