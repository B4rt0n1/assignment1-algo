package algo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import util.DepthTracker;
import util.MetricsCounters;

public class EdgeCaseTest {

    @Test
    public void testEmptyArraySort() {
        int[] arr = {};
        new MergeSort(new MetricsCounters(), new DepthTracker()).sort(arr);
        assertEquals(0, arr.length);
    }

    @Test
    public void testSingleElementQuickSort() {
        int[] arr = {42};
        new QuickSort(new MetricsCounters(), new DepthTracker()).sort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    public void testAllDuplicatesMergeSort() {
        int[] arr = {5, 5, 5, 5};
        new MergeSort(new MetricsCounters(), new DepthTracker()).sort(arr);
        assertArrayEquals(new int[]{5, 5, 5, 5}, arr);
    }

    @Test
    public void testSelectSingleElement() {
        int[] arr = {7};
        int result = new DeterministicSelect(new MetricsCounters(), new DepthTracker()).select(arr, 0);
        assertEquals(7, result);
    }

    @Test
    public void testClosestPairIdenticalPoints() {
        ClosestPair.Point[] pts = {
            new ClosestPair.Point(1, 1),
            new ClosestPair.Point(1, 1)
        };
        ClosestPair.Result res = new ClosestPair(new MetricsCounters(), new DepthTracker()).findClosest(pts);
        assertEquals(0.0, res.distance, 1e-9);
    }
}
