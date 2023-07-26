package pgdp.algorithms;

import java.util.Arrays;

public class GnomeSort {

	/**
	 * Sort input array using gnome sort
	 * @param a integer array to be sorted using gnome sort
	 */
	public static void gnomeSort(int[] a) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int r = 1; r < a.length - i; r++) {
				if (a[r] < a[r-1]) {
					int s = a[r];
					a[r] = a[r-1];
					a[r-1] = s;
				}
			}
		}
	}

	public static void main(String[] args) {
		int[] a = new int[] { 5, 2, 3, 1, 6, 0, 4 };
		gnomeSort(a);
		System.out.println(Arrays.toString(a));
	}

}
