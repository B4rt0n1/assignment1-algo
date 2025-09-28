package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import util.DepthTracker;
import util.MetricsCounters;


public class ClosestPair {

    private final MetricsCounters counters;
    private final DepthTracker depth;

    public ClosestPair(MetricsCounters counters, DepthTracker depth) {
        this.counters = counters;
        this.depth = depth;
    }

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Result {
        public final Point p1;
        public final Point p2;
        public final double distance;

        public Result(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }
    }

    public Result findClosest(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        Point[] px = points.clone();
        Point[] py = points.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));

        return closestPair(px, py);
    }

    private Result closestPair(Point[] px, Point[] py) {
        int n = px.length;

        if (n <= 3) {
            return bruteForce(px);
        }

        int mid = n / 2;
        Point midPoint = px[mid];

        Point[] pxLeft = Arrays.copyOfRange(px, 0, mid);
        Point[] pxRight = Arrays.copyOfRange(px, mid, n);

        List<Point> pyLeftList = new ArrayList<>();
        List<Point> pyRightList = new ArrayList<>();
        for (Point p : py) {
            if (p.x <= midPoint.x) {
                pyLeftList.add(p);
            } else {
                pyRightList.add(p);
            }
        }

        Point[] pyLeft = pyLeftList.toArray(new Point[0]);
        Point[] pyRight = pyRightList.toArray(new Point[0]);

        Result leftResult = closestPair(pxLeft, pyLeft);
        Result rightResult = closestPair(pxRight, pyRight);

        double d = Math.min(leftResult.distance, rightResult.distance);
        Result best = leftResult.distance <= rightResult.distance ? leftResult : rightResult;

        List<Point> strip = new ArrayList<>();
        for (Point p : py) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < d; j++) {
                double dist = dist(strip.get(i), strip.get(j));
                if (dist < d) {
                    d = dist;
                    best = new Result(strip.get(i), strip.get(j), dist);
                }
            }
        }

        return best;
    }

    private Result bruteForce(Point[] points) {
        double minDist = Double.POSITIVE_INFINITY;
        Point p1 = null, p2 = null;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double d = dist(points[i], points[j]);
                if (d < minDist) {
                    minDist = d;
                    p1 = points[i];
                    p2 = points[j];
                }
            }
        }

        return new Result(p1, p2, minDist);
    }

    private double dist(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
