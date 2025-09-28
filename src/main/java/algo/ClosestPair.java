package algo;

import java.util.Arrays;
import java.util.Comparator;

import util.DepthTracker;
import util.MetricsCounters;

public class ClosestPair {

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Result {
        public final Point p1, p2;
        public final double distance;

        public Result(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }
    }

    private final MetricsCounters counters;
    private final DepthTracker depthTracker;

    public ClosestPair(MetricsCounters counters, DepthTracker depthTracker) {
        this.counters = counters;
        this.depthTracker = depthTracker;
    }

    public Result findClosest(Point[] points) {
    if (points == null || points.length < 2) {
        throw new IllegalArgumentException("At least two points required");
    }

    for (int i = 0; i < points.length - 1; i++) {
        for (int j = i + 1; j < points.length; j++) {
            if (points[i].x == points[j].x && points[i].y == points[j].y) {
                return new Result(points[i], points[j], 0.0);
            }
        }
    }

    Point[] pointsSortedByX = points.clone();
    Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));

    Point[] pointsSortedByY = points.clone();
    Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));

    return closestPair(pointsSortedByX, pointsSortedByY);
}

    private Result closestPair(Point[] px, Point[] py) {
        depthTracker.enter();

        int n = px.length;
        if (n <= 3) {
            Result result = bruteForce(px);
            depthTracker.exit();
            return result;
        }

        int mid = n / 2;
        Point midPoint = px[mid];

        Point[] Qx = Arrays.copyOfRange(px, 0, mid);
        Point[] Rx = Arrays.copyOfRange(px, mid, n);

        // Divide py into Qy and Ry
        Point[] Qy = new Point[mid];
        Point[] Ry = new Point[n - mid];
        int qi = 0, ri = 0;
        for (Point p : py) {
            if (p.x <= midPoint.x) Qy[qi++] = p;
            else Ry[ri++] = p;
        }

        Result leftResult = closestPair(Qx, Qy);
        Result rightResult = closestPair(Rx, Ry);

        Result best = leftResult.distance < rightResult.distance ? leftResult : rightResult;
        Result stripResult = stripClosest(py, midPoint.x, best.distance);

        depthTracker.exit();
        return (stripResult != null && stripResult.distance < best.distance) ? stripResult : best;
    }

    private Result bruteForce(Point[] pts) {
        double minDist = Double.POSITIVE_INFINITY;
        Point a = null, b = null;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double d = distance(pts[i], pts[j]);
                if (d < minDist) {
                    minDist = d;
                    a = pts[i];
                    b = pts[j];
                }
            }
        }
        return new Result(a, b, minDist);
    }

    private Result stripClosest(Point[] py, double midX, double delta) {
        Point[] strip = Arrays.stream(py)
                .filter(p -> Math.abs(p.x - midX) < delta)
                .toArray(Point[]::new);

        double minDist = delta;
        Point a = null, b = null;

        for (int i = 0; i < strip.length; i++) {
            for (int j = i + 1; j < strip.length && (strip[j].y - strip[i].y) < minDist; j++) {
                double d = distance(strip[i], strip[j]);
                if (d < minDist) {
                    minDist = d;
                    a = strip[i];
                    b = strip[j];
                }
            }
        }

        return (a != null) ? new Result(a, b, minDist) : null;
    }

    private double distance(Point p1, Point p2) {
        counters.incComparisons();
        return Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }
}
