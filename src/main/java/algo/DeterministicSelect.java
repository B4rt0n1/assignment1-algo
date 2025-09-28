package algo;

import util.ArrayUtils;
import util.DepthTracker;
import util.MetricsCounters;

public class DeterministicSelect {
    private final MetricsCounters counters;
    private final DepthTracker depthTracker;

    public DeterministicSelect(MetricsCounters counters, DepthTracker depthTracker) {
        this.counters = counters;
        this.depthTracker = depthTracker;
    }


public int select(int[] arr, int k) {
    ArrayUtils.guardNotNull(arr);
    if (arr.length == 0) {
        throw new IllegalArgumentException("Array is empty");
    }
    if (k < 0 || k >= arr.length) {
        throw new IllegalArgumentException("k out of range");
    }
    if (arr.length == 1) return arr[0];
    return select(arr, 0, arr.length - 1, k);
}


    private int select(int[] arr, int left, int right, int k) {
        depthTracker.enter();

        while (true) {
            if (left == right) {
                depthTracker.exit();
                return arr[left];
            }

            int pivotIndex = medianOfMedians(arr, left, right);
            pivotIndex = partition(arr, left, right, pivotIndex);

            if (k == pivotIndex) {
                depthTracker.exit();
                return arr[k];
            } else if (k < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
        }
    }


    private int partition(int[] arr, int left, int right, int pivotIndex) {
        ArrayUtils.swap(arr, pivotIndex, right);
        int pivot = arr[right];
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (compare(arr[i], pivot) < 0) {
                ArrayUtils.swap(arr, storeIndex++, i);
            }
        }
        ArrayUtils.swap(arr, storeIndex, right);
        return storeIndex;
    }


    private int medianOfMedians(int[] arr, int left, int right) {
        int n = right - left + 1;
        if (n < 5) {
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

        return select(arr, left, left + numMedians - 1, left + numMedians / 2);
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

    private int compare(int a, int b) {
        counters.incComparisons();
        return Integer.compare(a, b);
    }
}
