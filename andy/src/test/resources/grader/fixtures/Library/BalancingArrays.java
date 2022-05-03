package delft;

class Splitting {

    private Splitting() {
        // Empty Constructor
    }

    /**
     * Given a non-empty array, return true if there is a place to split the array
     * so that the sum of the numbers on one side is equal to the sum of the numbers
     * on the other side.
     *
     * @param nums
     *            the array containing the input numbers. The array must be non-null
     *            and with len > 1; the program returns false in case any
     *            pre-condition is violated.
     * @return true if they can be split in the way defined above
     */
    public static boolean canBalance(int[] nums) {
        if (nums == null || nums.length <= 1)
            return false;
        int sum = 0;
        for (int num : nums)
            sum += num;
        if (sum % 2 == 1)
            return false;
        int half = sum / 2;
        for (int i = 0; half > 0; i++) {
            half -= nums[i];
        }
        return (half == 0);
    }
}
