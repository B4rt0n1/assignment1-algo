package algo;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import util.DepthTracker;
import util.MetricsCounters;

public class DeterministicSelectTest {

    @Test
    public void testSmallArray() {
        int[] arr = {7, 2, 9, 4, 1};
        DeterministicSelect sel = new DeterministicSelect(new MetricsCounters(), new DepthTracker());
        assertEquals(1, sel.select(arr.clone(), 0)); // smallest
        assertEquals(4, sel.select(arr.clone(), 2)); // median
        assertEquals(9, sel.select(arr.clone(), 4)); // largest
    }

    @Test
    public void testRandomArray() {
        int[] arr = new Random().ints(1000, -10000, 10000).toArray();
        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        DeterministicSelect sel = new DeterministicSelect(new MetricsCounters(), new DepthTracker());

        for (int k = 0; k < 100; k++) {
            int result = sel.select(arr.clone(), k);
            assertEquals(sorted[k], result);
        }
    }

    @Test
    public void testDepthLogN() {
        int[] arr = new Random().ints(2048, 0, 100000).toArray();
        DepthTracker depth = new DepthTracker();
        DeterministicSelect sel = new DeterministicSelect(new MetricsCounters(), depth);
        sel.select(arr, arr.length / 2);

        assertTrue(depth.getMaxDepth() <= 4 * (int) (Math.log(arr.length) / Math.log(2)) + 10);
    }

    @Test
    public void testThrowsForInvalidK() {
        int[] arr = {1, 2, 3};
        DeterministicSelect sel = new DeterministicSelect(new MetricsCounters(), new DepthTracker());
        assertThrows(IllegalArgumentException.class, () -> sel.select(arr, -1));
        assertThrows(IllegalArgumentException.class, () -> sel.select(arr, 3));
    }
}
