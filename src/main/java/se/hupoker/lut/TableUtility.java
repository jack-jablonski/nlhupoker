package se.hupoker.lut;

import java.util.Arrays;

/**
 * 
 * @author Alexander Nyberg
 *
 */
final class TableUtility {
	private TableUtility() {}

	public static void swap(int v[], int first, int second) {
		int temp = v[first];

		v[first] = v[second];
		v[second] = temp;
	}

	public static void initArray(int v[], int value) {
		Arrays.fill(v, value);
	}
	
	public static void initArray(int v[][], int value) {
		for (int arr[] : v) { initArray(arr, value); }
	}
	
	public static void initArray(int v[][][], int value) {
		for (int arr[][] : v) { initArray(arr, value); }
	}
	
	public static void initArray(int v[][][][], int value) {
		for (int arr[][][] : v) { initArray(arr, value); }
	}
	
	public static void initArray(int v[][][][][], int value) {
		for (int arr[][][][] : v) { initArray(arr, value); }
	}
	
	public static void initArray(int v[][][][][][], int value) {
		for (int arr[][][][][] : v) { initArray(arr, value); }
	}
	
	public static void initArray(int v[][][][][][][], int value) {
		for (int arr[][][][][][] : v) {	initArray(arr, value); }
	}

    public static int maximumOccurrence(int v[]) {
        int maximum = 0;

        for (int value : v) {
            maximum = Math.max(maximum, value);
        }

        return maximum;
    }

	/**
	 * 
	 * @param ranks
	 * @param suits
	 * @return
	 */
	public static int [] getSortedBoard(int ranks[], int suits[]) {
		int [] board = new int[ranks.length-2];

		for (int i=0; i < ranks.length-2; i++) {
			board[i] = ranks[i+2]*4 + suits[i+2];
		}

		Arrays.sort(board);

		return board;
	}
}