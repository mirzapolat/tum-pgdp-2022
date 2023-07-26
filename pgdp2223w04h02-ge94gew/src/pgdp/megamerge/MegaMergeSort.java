package pgdp.megamerge;

import java.util.Arrays;

public class MegaMergeSort {

	/**
	 * Sorts the array using mega merge sort with div splits
	 * @param array the array to be sorted
	 * @param div the split factor
	 * @return the sorted array
	 */
	protected int[] megaMergeSort(int[] array, int div) {
		return megaMergeSort(array, div, 0, array.length);
	}

	/**
	 * Sorts the array using mega merge sort with div splits in the defined range
	 * @param array the array to be sorted
	 * @param div the split factor
	 * @param from the lower bound (inclusive)
	 * @param to the upper bound (exclusive)
	 * @return the sorted array
	 */
	protected int[] megaMergeSort(int[] array, int div, int from, int to) {
		if (div > array.length) div = array.length;
		if (from == to) return new int[0];

		int [][] splitted = split(array, div);
		for (int i = 0; i < splitted.length; i++){
			if (splitted[i].length <= 1) break;
			splitted[i] = megaMergeSort(splitted[i], div, 0, splitted[i].length);
		}

		return merge(splitted, 0, div);
	}

	public static int[][] split(int[] array, int div) {
		if (div > array.length) div = array.length;
		if (array.length == 0) return new int[][] {};
		if (div == 1) return new int[][] {array};
		else if (div == 0) return new int[][] {};

		int length = array.length / div;
		if (array.length % div != 0) length++;

		int[] part = new int[length];
		int[] rest = new int[array.length - length];
		System.arraycopy(array, 0, part, 0, length);
		System.arraycopy(array, length, rest, 0, array.length - length);

		int[][] returner = new int[div][];
		returner[0] = part;
		System.arraycopy(split(rest, div - 1), 0, returner, 1, div - 1);

		return returner;
	}

	/**
	 * Merges all arrays in the given range
	 * @param arrays to be merged
	 * @param from lower bound (inclusive)
	 * @param to upper bound (exclusive)
	 * @return the merged array
	 */
	protected int[] merge(int[][] arrays, int from, int to) {
		if (from == to) return new int[0];
		if (from == to-1) return arrays[from];
		return merge(merge(arrays, from+1, to), arrays[from]);
	}

	/**
	 * Merges the given arrays into one
	 * @param arr1 the first array
	 * @param arr2 the second array
	 * @return the resulting array
	 */
	protected int[] merge(int[] arr1, int[] arr2) {
		int index1 = 0, index2 = 0;
		int[] result = new int[arr1.length + arr2.length];

		for (int i = 0; i < result.length; i++) {
			if (index1 == arr1.length) result[i] = arr2[index2++];
			else if (index2 == arr2.length) result[i] = arr1[index1++];
			else {
				if (arr1[index1] <= arr2[index2]) result[i] = arr1[index1++];
				else result[i] = arr2[index2++];
			}
		}

		return result;
	}

	public static void main(String[] args) {
		MegaMergeSort mms = new MegaMergeSort();
		int[] arr = new int[] { 17, 29, 30, 106,86,50, 0,5, 93, -27, -203, -100, -43, -69, 83, 0};
		int[] res = mms.megaMergeSort(arr, 4);
		System.out.println(Arrays.toString(res));
	}
}
