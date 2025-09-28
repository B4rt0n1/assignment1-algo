package util;

import java.util.Random;

public final class ArrayUtils {
    private static final Random RAND = new Random();

    private ArrayUtils() {} // prevent instantiation

    public static void swap(int[] arr, int i, int j) {
        guardNotNull(arr);
        guardRange(arr, i);
        guardRange(arr, j);
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static void shuffle(int[] arr) {
        guardNotNull(arr);
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RAND.nextInt(i + 1);
            swap(arr, i, j);
        }
    }


    public static int partition(int[] arr, int left, int right) {
        guardNotNull(arr);
        guardRange(arr, left);
        guardRange(arr, right);
        if (left > right) throw new IllegalArgumentException("left > right");

        int pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swap(arr, ++i, j);
            }
        }
        swap(arr, i + 1, right);
        return i + 1;
    }

    public static void guardNotNull(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
    }

    public static void guardRange(int[] arr, int index) {
        if (index < 0 || index >= arr.length) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }
}
