package algo;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import util.DepthTracker;
import util.MetricsCounters;

public class QuickSortTest {

    @Test
    public void testSmallArray() {
        int[] arr = {5, 3, 8, 4, 2};
        int[] expected = arr.clone();
        Arrays.sort(expected);

        QuickSort qs = new QuickSort(new MetricsCounters(), new DepthTracker());
        qs.sort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testLargeRandomArray() {
        int[] arr = new Random().ints(10000, -100000, 100000).toArray();
        int[] expected = arr.clone();
        Arrays.sort(expected);

        QuickSort qs = new QuickSort(new MetricsCounters(), new DepthTracker());
        qs.sort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testDepthIsLogN() {
        int n = 1 << 14; // 16384
        int[] arr = new Random().ints(n, -100000, 100000).toArray();

        DepthTracker depth = new DepthTracker();
        QuickSort qs = new QuickSort(new MetricsCounters(), depth);
        qs.sort(arr);

        int maxExpectedDepth = 2 * (int) (Math.log(n) / Math.log(2)) + 10;
        assertTrue(depth.getMaxDepth() <= maxExpectedDepth, 
            "Max depth too high: " + depth.getMaxDepth());
    }

    @Test
    public void testMetricsTracked() {
        MetricsCounters counters = new MetricsCounters();
        DepthTracker depth = new DepthTracker();

        int[] arr = {3, 1, 2};
        QuickSort qs = new QuickSort(counters, depth);
        qs.sort(arr);

        assertTrue(counters.getComparisons() > 0);
        assertTrue(depth.getMaxDepth() > 0);
    }
}
