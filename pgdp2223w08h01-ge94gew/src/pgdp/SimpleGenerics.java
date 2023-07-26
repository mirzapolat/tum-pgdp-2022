package pgdp;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public final class SimpleGenerics {

	private SimpleGenerics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a String of the given Collection.
	 * 
	 * @param collection
	 * @return String representation of the collection
	 */
	public static String toString(Collection<?> collection) {

		StringBuilder res = new StringBuilder("{");

		for(Object a : collection) {
			res.append(String.valueOf(a)).append(", ");
		}

		if (!collection.isEmpty()) res = new StringBuilder(res.substring(0, res.length() - 2));
		res.append("}");

		return res.toString();
	}

	/**
	 * Returns int array of collection.
	 * 
	 * @param collection
	 * @return int array
	 */
	public static int[] toIntArray(Collection<Integer> collection) {
		int[] res = new int[collection.size()];
		int index = 0;

		for (Integer a: collection) res[index++] = a;

		return res;
	}

	/**
	 * Generates an generic array of type T with the given length.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param length
	 * @return reference to the generated generic array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] generateGenericArray(Class<T> clazz, int length) {
		final T[] arr = (T[]) Array.newInstance(clazz, length);
		return arr;
	}

	/**
	 * Returns the given collection in a sorted array.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param collection
	 * @param comparator dictates the order of the output
	 * @return array of type T
	 */
	public static <T> T[] specialSort(Class<T> clazz, Collection<T> collection, Comparator<T> comparator) {
		if (collection.isEmpty()) return generateGenericArray(clazz, 0);

		T[] s = generateGenericArray(clazz, collection.size());

		int ind = 0;
		for (T a : collection) s[ind++] = a;

		Arrays.sort(s, comparator);
		return s;
	}

	/**
	 * Returns a collection of all elements that are contained by each Collection of
	 * collections. Collections of the input are not modified.
	 * 
	 * @param <T>
	 * @param collections not null, may not contain null values.
	 * @return intersection of all collections
	 */
	public static <T> Collection<T> intersection(Collection<T>[] collections) {
		if (collections.length == 0) return Collections.emptyList();

		Collection<T> res = new ArrayList<>(collections[0]);
		Collection<T> del = new ArrayList<>();

		for (int i = 1; i < collections.length; i++) {
			for (T element : res) {
				if (!collections[i].contains(element)) del.add(element);
			}
		}

		for (T element : del) res.remove(element);

		return res;
	}

	/**
	 * Returns the values stored in the map. Equivalent to map.values().
	 * 
	 * @param <K> key type
	 * @param <V> value type
	 * @param map
	 * @return set of values
	 */
	public static <K, V> Set<V> getValues(Map<K, V> map) {

		Set<V> res = new HashSet<>();

		for (K key : map.keySet()) {
			res.add(map.get(key));
		}

		return res;
	}

	public static void main(String... args) {
		Collection<Integer> list = new ArrayList<>();
		list.add(8);
		list.add(2);
		list.add(2);
		list.add(4);
		Collection<Integer> list1 = new ArrayList<>();
		list1.add(20);
		list1.add(2);
		list1.add(4);
		Collection<Integer> list3 = new ArrayList<>();
		list3.add(9);
		list3.add(8);
		list3.add(2);
		list3.add(4);
		Collection<ArrayList>[] listArray = new Collection[]{list1, list, list3};
		System.out.println(Arrays.toString(intersection(listArray).toArray()));
	}
}
