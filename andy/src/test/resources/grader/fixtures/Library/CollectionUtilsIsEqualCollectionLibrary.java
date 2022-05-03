package delft;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class CollectionUtils {

    /** <code>CollectionUtils</code> should not normally be instantiated. */
    private CollectionUtils() {
    }

    /**
     * Helper class to easily access cardinality properties of two collections.
     *
     * @param <O>
     *            the element type
     */
    private static class CardinalityHelper<O> {

        /** Contains the cardinality for each object in collection A. */
        final Map<O, Integer> cardinalityA;

        /** Contains the cardinality for each object in collection B. */
        final Map<O, Integer> cardinalityB;

        /**
         * Create a new CardinalityHelper for two collections.
         *
         * @param a
         *            the first collection
         * @param b
         *            the second collection
         */
        public CardinalityHelper(final Iterable<? extends O> a, final Iterable<? extends O> b) {
            cardinalityA = CollectionUtils.<O>getCardinalityMap(a);
            cardinalityB = CollectionUtils.<O>getCardinalityMap(b);
        }

        /**
         * Returns the frequency of this object in collection A.
         *
         * @param obj
         *            the object
         * @return the frequency of the object in collection A
         */
        public int freqA(final Object obj) {
            return getFreq(obj, cardinalityA);
        }

        /**
         * Returns the frequency of this object in collection B.
         *
         * @param obj
         *            the object
         * @return the frequency of the object in collection B
         */
        public int freqB(final Object obj) {
            return getFreq(obj, cardinalityB);
        }

        private int getFreq(final Object obj, final Map<?, Integer> freqMap) {
            final Integer count = freqMap.get(obj);
            if (count != null) {
                return count.intValue();
            }
            return 0;
        }
    }

    /**
     * Returns a {@link Map} mapping each unique element in the given
     * {@link Collection} to an {@link Integer} representing the number of
     * occurrences of that element in the {@link Collection}.
     *
     * <p>
     * Only those elements present in the collection will appear as keys in the map.
     *
     * @param <O>
     *            the type of object in the returned {@link Map}. This is a super
     *            type of &lt;I&gt;.
     * @param coll
     *            the collection to get the cardinality map for, must not be null
     * @return the populated cardinality map
     */
    private static <O> Map<O, Integer> getCardinalityMap(final Iterable<? extends O> coll) {
        final Map<O, Integer> count = new HashMap<>();
        for (final O obj : coll) {
            final Integer c = count.get(obj);
            if (c == null) {
                count.put(obj, Integer.valueOf(1));
            } else {
                count.put(obj, Integer.valueOf(c.intValue() + 1));
            }
        }
        return count;
    }

    /**
     * Returns {@code true} iff the given {@link Collection}s contain exactly the
     * same elements with exactly the same cardinalities.
     *
     * <p>
     * That is, iff the cardinality of <i>e</i> in <i>a</i> is equal to the
     * cardinality of <i>e</i> in <i>b</i>, for each element <i>e</i> in <i>a</i> or
     * <i>b</i>.
     *
     * @param a
     *            the first collection, must not be null
     * @param b
     *            the second collection, must not be null
     * @return <code>true</code> iff the collections contain the same elements with
     *         the same cardinalities.
     */
    public static boolean isEqualCollection(final Collection<?> a, final Collection<?> b) {
        if (a.size() != b.size()) {
            return false;
        }
        final CardinalityHelper<Object> helper = new CardinalityHelper<>(a, b);
        if (helper.cardinalityA.size() != helper.cardinalityB.size()) {
            return false;
        }
        for (final Object obj : helper.cardinalityA.keySet()) {
            if (helper.freqA(obj) != helper.freqB(obj)) {
                return false;
            }
        }
        return true;
    }
}
