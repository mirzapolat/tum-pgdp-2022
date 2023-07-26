package pgdp.algorithms;

import java.util.Arrays;

public class BinarySearch {

	/**
	 * recursive binary search
	 * @param a sorted integer array
	 * @param value value to be found
	 * @return index of value in a or -1 if not present
	 */
	public static int binarySearchRec(int[] a, int value) {

		return binarySearchRec(a, value, 0, a.length - 1);
	}

	private static int binarySearchRec(int[] a, int value, int start, int end) {
		if (a.length == 1 && a[0] == value)

		int middle = a.length / 2;



		return 0;
	}

	/**
	 * Iterative binary search
	 * @param a sorted integer array
	 * @param value value to be found
	 * @return index of value in a or -1 if not present
	 */
	public static int binarySearchIter(int[] a, int value) {
		return -2;
	}

	/** Find a value in an array by dividing it in three parts.
	 *  The array is assumed to be ascendingly ordered.
	 *
	 * @param array An ascendingly ordered array
	 * @param value Any Integer
	 * @return The index of the (a) position in the array, that holds the 'value',
	 * 			-1, if none exists
	 */
	public static int ternarySearch(int[] array, int value) {
		return ternarySearch(array, value, 0, array.length);
	}

	public static int ternarySearch(int[] array, int value, int start, int end) {
		return -2;
	}

	/** Binary search in a 2D-Array.
	 *  Two adjacent elements array[i][j] and array[i][j + 1] are assumed to be in ascending order,
	 *  i.e. array[i][j] <= array[i][j + 1].
	 *  Two adjacent elements array[i][j] and array[i + 1][j] are assumed to be in ascending order,
	 * 	i.e. array[i][j] <= array[i + 1][j].
	 *
	 * @param array A 2D-rectangular array ordered in the above described way.
	 * @param value Any Integer
	 * @return A 2-entry array {i, j} with the two indices of the (a) position, the 'value'
	 *      	is to be found in the 'array', 'null', if there is none.
	 */
	public static int[] bibinarySearch(int[][] array, int value) {
		if(array.length == 0) {
			return null;
		}
		return bibinarySearch(array, value, 0, array.length, 0, array[0].length);
	}

	public static int[] bibinarySearch(int[][] array, int value, int iStart, int iEnd, int jStart, int jEnd) {
		return new int[] {-1, -1};
	}

	public static void main(String[] args) {
		int[][] a = new int[][] {
				{0, 1, 2, 3, 4, 5},
				{5, 6, 7, 8, 9, 10},
				{10, 11, 12, 13, 14, 15},
				{15, 16, 17, 18, 19, 20},
				{20, 21, 22, 23, 24, 25}
		};
		System.out.println(Arrays.toString(bibinarySearch(a, 5)));
	}

}
