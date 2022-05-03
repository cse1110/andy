package delft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class NumberUtils {

    private NumberUtils() {
        // Empty constructor
    }

    /**
     * The method receives two numbers, left and right, represented as lists, and
     * returns the subtraction of the two numbers (left + right), also as a list.
     *
     * <p>
     * For example, if you want to subtract the numbers 42 and 23, you would need to
     * create a (left) list with two elements [4,2] and a (right) list with two
     * elements [2,3]. 42-23 = 19, so the program would produce another list with
     * two elements [1,9]
     *
     * <p>
     * [4,2] - [2,3] = [1,9]
     *
     * <p>
     * Each element in the left and right lists should be a number from [0-9]. The
     * method only supports cases when the left number is greater than or equal to
     * the right number, such that the result is always non-negative. An
     * IllegalArgumentException is thrown in case either of these pre-conditions
     * does not hold.
     *
     * <p>
     * Leading zeroes are removed from the result, so [0,5] - [2] = [3] (and not
     * [0,3]).
     *
     * @param left
     *            a list containing the left number. Null returns null, empty means
     *            0.
     * @param right
     *            a list containing the right number. Null returns null, empty means
     *            0.
     * @return the difference between left and right, as a list
     */
    public static List<Integer> subtract(List<Integer> left, List<Integer> right) {
        // if any is null, return null
        if (left == null || right == null)
            return null;
        // reverse the numbers so that the least significant digit goes to the left.
        ArrayList<Integer> reversedLeft = new ArrayList<>(left);
        Collections.reverse(reversedLeft);
        ArrayList<Integer> reversedRight = new ArrayList<>(right);
        Collections.reverse(reversedRight);
        LinkedList<Integer> result = new LinkedList<>();
        // while there's a digit, keep subtracting them
        // if there's carry, take the carry into consideration
        int carry = 0;
        for (int i = 0; i < Math.max(reversedLeft.size(), reversedRight.size()); i++) {
            int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
            int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;
            if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                throw new IllegalArgumentException();
            int subtraction = leftDigit - carry - rightDigit;
            if (subtraction < 0) {
                subtraction += 10;
                carry = 1;
            } else {
                carry = 0;
            }
            result.addFirst(subtraction);
        }
        // If there is leftover carry, it means left<right, which is not supported
        if (carry > 0)
            throw new IllegalArgumentException();
        // remove leading zeroes from the result
        while (result.size() > 0 && result.get(0) == 0)
            result.remove(0);
        if (result.isEmpty()) {
            result.addFirst(0);
        }
        return result;
    }
}
