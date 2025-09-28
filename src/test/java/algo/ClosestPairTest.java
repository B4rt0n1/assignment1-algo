package algo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import util.DepthTracker;
import util.MetricsCounters;

public class ClosestPairTest {

    @Test
    public void testSimpleCase() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4),
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(2, 2)
        };

        ClosestPair cp = new ClosestPair(new MetricsCounters(), new DepthTracker());
        ClosestPair.Result res = cp.findClosest(points);

        assertEquals(Math.sqrt(2), res.distance, 1e-9);
    }

    @Test
    public void testTwoPoints() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(4, 5)
        };

        ClosestPair cp = new ClosestPair(new MetricsCounters(), new DepthTracker());
        ClosestPair.Result res = cp.findClosest(points);

        assertEquals(5.0, res.distance, 1e-9);
    }

    @Test
    public void testRandomLarge() {
        int n = 2000;
        ClosestPair.Point[] points = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new ClosestPair.Point(Math.random() * 1000, Math.random() * 1000);
        }

        ClosestPair cp = new ClosestPair(new MetricsCounters(), new DepthTracker());
        ClosestPair.Result res = cp.findClosest(points);

        assertTrue(res.distance > 0);
        assertNotNull(res.p1);
        assertNotNull(res.p2);
    }

    @Test
    public void testDepthIsLogN() {
        int n = 4096;
        ClosestPair.Point[] points = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new ClosestPair.Point(Math.random(), Math.random());
        }

        DepthTracker depth = new DepthTracker();
        ClosestPair cp = new ClosestPair(new MetricsCounters(), depth);
        cp.findClosest(points);

        assertTrue(depth.getMaxDepth() <= 2 * (int) (Math.log(n) / Math.log(2)) + 10);
    }
}
