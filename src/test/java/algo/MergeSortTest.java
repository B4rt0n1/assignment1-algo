package algo;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import util.DepthTracker;
import util.MetricsCounters;

public class MergeSortTest {

    @Test
    public void testSortSmallArray() {
        int[] arr = {5, 2, 9, 1, 5, 6};
        int[] expected = arr.clone();
        Arrays.sort(expected);

        MergeSort ms = new MergeSort(new MetricsCounters(), new DepthTracker());
        ms.sort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testSortRandomLarge() {
        Random rand = new Random();
        int[] arr = rand.ints(10000, 0, 100000).toArray();
        int[] expected = arr.clone();
        Arrays.sort(expected);

        MergeSort ms = new MergeSort(new MetricsCounters(), new DepthTracker());
        ms.sort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testDepthIsLogN() {
        int n = 1 << 12; // 4096
        int[] arr = new Random().ints(n, 0, 100000).toArray();

        DepthTracker depth = new DepthTracker();
        MergeSort ms = new MergeSort(new MetricsCounters(), depth);
        ms.sort(arr);

        assertTrue(depth.getMaxDepth() <= 2 * (int) (Math.log(n) / Math.log(2)) + 10);
    }
}