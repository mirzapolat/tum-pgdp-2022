package pgdp.algorithms;

import java.util.Arrays;

public class RecursiveSorting {

    /**	Implementation of the MergeSort algorithm
     *
     * @param array Any Integer-Array
     * @return The passed Integer-Array, but sorted in ascending order
     */
    public static int[] mergeSort(int[] array) {

        if (array.length == 1) return array;

        int[] firstPart = new int[array.length/2];
        int[] secondPart = new int[array.length - firstPart.length];

        for (int i = 0; i < array.length; i++) {
            if (i < firstPart.length) firstPart[i] = array[i];
            else secondPart[i - firstPart.length] = array[i];
        }

        return merge(mergeSort(firstPart), mergeSort(secondPart));
    }

    // Hilfsmethode (muss nicht verwendet werden, könnte aber hilfreich sein)
    public static int[] merge(int[] first, int[] second) {

        int[] result = new int[first.length + second.length];

        int ifi = 0;
        int ise = 0;

        for (int i = 0; i < result.length; i++) {
            if (ise == second.length) result[i] = first[ifi++];
            else if (ifi == first.length) result[i] = second[ise++];
            else {
                if (first[ifi] > second[ise]) result[i] = second[ise++];
                else result[i] = first[ifi++];
            }
        }

        return result;
    }



    /**	Implementation of the StoogeSort algorithm
     *
     * @param array Any Integer-Array
     * @return The passed Integer-Array, but sorted in ascending order
     */
    public static void stoogeSort(int[] array) {
        stoogeSort(array, 0, array.length);
    }

    public static void stoogeSort(int[] array, int from, int to) {

    }



    /**	Implementation of the SelectionSort algorithm in a recursive way
     *
     * @param array Any Integer-Array
     * @return The passed Integer-Array, but sorted in ascending order
     */
    public static void selectionSortRec(int[] a) {
        selectionSortRec(a, a.length - 1);
    }

    public static void selectionSortRec(int[] a, int toIncl) {

    }

    // Hilfsmethode (muss nicht verwendet werden, könnte aber hilfreich sein)
    public static int findIndexOfLargest(int[] a, int toIncl) {
        return -1;
    }

    // Hilfsmethode (muss nicht verwendet werden, könnte aber hilfreich sein)
    public static void swap(int[] a, int firstPos, int secondPos) {

    }

    // Für Experimente
    public static void main(String[] args) {
        int[] a = {3, 4, 9, 2, 5, 0, 2, 1, 6, 4, -3};
        mergeSort(a);
        System.out.println(Arrays.toString(a));
    }

}
