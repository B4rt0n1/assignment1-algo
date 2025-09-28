package algo;

import util.ArrayUtils;
import util.DepthTracker;
import util.MetricsCounters;

public class DeterministicSelect {
    private final MetricsCounters counters;
    private final DepthTracker depth;

    public DeterministicSelect(MetricsCounters counters, DepthTracker depth) {
        this.counters = counters;
        this.depth = depth;
    }

    public int select(int[] arr, int k) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Empty array");
        if (k < 0 || k >= arr.length) throw new IllegalArgumentException("k out of range");
        return select(arr, 0, arr.length - 1, k);
    }

    private int select(int[] arr, int left, int right, int k) {
        while (true) {
            if (left == right) return arr[left];

            int pivotIndex = medianOfMedians(arr, left, right);
            pivotIndex = partition(arr, left, right, pivotIndex);

            if (k == pivotIndex) {
                return arr[k];
            } else if (k < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
        }
    }

    private int partition(int[] arr, int left, int right, int pivotIndex) {
        ArrayUtils.guardRange(arr, pivotIndex);
        int pivotValue = arr[pivotIndex];
        ArrayUtils.swap(arr, pivotIndex, right);
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (arr[i] < pivotValue) {
                ArrayUtils.swap(arr, storeIndex++, i);
            }
        }
        ArrayUtils.swap(arr, storeIndex, right);
        return storeIndex;
    }

private int medianOfMedians(int[] arr, int left, int right) {
    int n = right - left + 1;

    if (n <= 5) {
        insertionSort(arr, left, right);
        return left + n / 2;
    }

    int numMedians = 0;
    for (int i = left; i <= right; i += 5) {
        int subRight = Math.min(i + 4, right);
        insertionSort(arr, i, subRight);
        int medianIndex = i + (subRight - i) / 2;
        ArrayUtils.swap(arr, left + numMedians, medianIndex);
        numMedians++;
    }

    int medLeft = left;
    int medRight = left + numMedians - 1;
    return medianOfMedians(arr, medLeft, medRight);
}


    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
}
