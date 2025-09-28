package algo;

import java.util.Random;

import util.DepthTracker;
import util.MetricsCounters;

public class QuickSort {
    private final MetricsCounters counters;
    private final DepthTracker depthTracker;
    private final Random rand = new Random();

    public QuickSort(MetricsCounters counters, DepthTracker depthTracker) {
        this.counters = counters;
        this.depthTracker = depthTracker;
    }

    public void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int left, int right) {
        depthTracker.enter();

        while (left < right) {
            int pivotIndex = partition(arr, left, right);

            if (pivotIndex - left < right - pivotIndex) {
                quickSort(arr, left, pivotIndex - 1);
                left = pivotIndex + 1; // tail call on larger side
            } else {
                quickSort(arr, pivotIndex + 1, right);
                right = pivotIndex - 1;
            }
        }

        depthTracker.exit();
    }

    private int partition(int[] arr, int left, int right) {
        int pivotIndex = left + rand.nextInt(right - left + 1);
        swap(arr, pivotIndex, right);
        int pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (compare(arr[j], pivot) <= 0) {
                swap(arr, ++i, j);
            }

        }
        swap(arr, i + 1, right);
        return i + 1;
    }

    private int compare(int a, int b) {
        counters.incComparisons();
        return Integer.compare(a, b);
    }

    private void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
