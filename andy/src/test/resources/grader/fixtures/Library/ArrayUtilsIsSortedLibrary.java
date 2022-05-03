package delft;

class ArrayUtils {

    /** <code>ArrayUtils</code> should not normally be instantiated. */
    private ArrayUtils() {
    }

    /**
     * This method checks whether the provided array is sorted according to natural
     * ordering.
     *
     * @param array
     *            the array to check
     * @return whether the array is sorted according to natural ordering
     * @since 3.4
     */
    public static boolean isSorted(final int[] array) {
        if (array == null || array.length < 2) {
            return true;
        }
        int previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final int current = array[i];
            if (previous > current) {
                return false;
            }
            previous = current;
        }
        return true;
    }
}

