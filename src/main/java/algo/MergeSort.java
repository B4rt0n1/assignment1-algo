package algo;

import util.DepthTracker;
import util.MetricsCounters;

public class MergeSort {
    private static final int INSERTION_SORT_CUTOFF = 32; // tweakable constant

    private final MetricsCounters counters;
    private final DepthTracker depthTracker;

    public MergeSort(MetricsCounters counters, DepthTracker depthTracker) {
        this.counters = counters;
        this.depthTracker = depthTracker;
    }

    public void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int[] buffer = new int[arr.length];
        mergeSort(arr, buffer, 0, arr.length - 1);
    }

    private void mergeSort(int[] arr, int[] buffer, int left, int right) {
        depthTracker.enter();

        if (right - left + 1 <= INSERTION_SORT_CUTOFF) {
            insertionSort(arr, left, right);
            depthTracker.exit();
            return;
        }

        int mid = (left + right) >>> 1;
        mergeSort(arr, buffer, left, mid);
        mergeSort(arr, buffer, mid + 1, right);
        merge(arr, buffer, left, mid, right);

        depthTracker.exit();
    }

    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private void merge(int[] arr, int[] buffer, int left, int mid, int right) {
        // Copy into buffer
        for (int i = left; i <= right; i++) {
            buffer[i] = arr[i];
            counters.incAllocations();
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (compare(buffer[i], buffer[j]) <= 0) {
                arr[k++] = buffer[i++];
            } else {
                arr[k++] = buffer[j++];
            }
        }
        while (i <= mid) {
            arr[k++] = buffer[i++];
        }
        while (j <= right) {
            arr[k++] = buffer[j++];
        }
    }

    private int compare(int a, int b) {
        counters.incComparisons();
        return Integer.compare(a, b);
    }
}
