package delft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class ListUtils {

	/** <code>ListUtils</code> should not normally be instantiated. */
	private ListUtils() {
	}

	/**
	 * Returns a new list containing all elements that are contained in both given
	 * lists.
	 *
	 * @param <E>
	 *            the element type
	 * @param list1
	 *            the first list
	 * @param list2
	 *            the second list
	 * @return the intersection of those two lists
	 * @throws NullPointerException
	 *             if either list is null
	 */
	public static <E> List<E> intersection(final List<? extends E> list1, final List<? extends E> list2) {
		final List<E> result = new ArrayList<>();
		List<? extends E> smaller = list1;
		List<? extends E> larger = list2;
		if (list1.size() > list2.size()) {
			smaller = list2;
			larger = list1;
		}
		final HashSet<E> hashSet = new HashSet<>(smaller);
		for (final E e : larger) {
			if (hashSet.contains(e)) {
				result.add(e);
				hashSet.remove(e);
			}
		}
		return result;
	}

}

