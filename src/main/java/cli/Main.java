package cli;

import java.io.IOException;
import java.util.Random;

import algo.ClosestPair;
import algo.DeterministicSelect;
import algo.MergeSort;
import algo.QuickSort;
import util.CSVWriter;
import util.DepthTracker;
import util.MetricsCounters;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String algo = getArgValue(args, "--algo");
        int n = Integer.parseInt(getArgValueOrDefault(args, "--n", "10000"));
        int k = Integer.parseInt(getArgValueOrDefault(args, "--k", "0"));
        String csvFile = getArgValueOrDefault(args, "--out", "metrics.csv");

        MetricsCounters counters = new MetricsCounters();
        DepthTracker depth = new DepthTracker();

        // Generate input
        int[] arr = new Random().ints(n, 0, 1_000_000).toArray();

        // Create CSV writer
        try (CSVWriter writer = new CSVWriter(csvFile)) {
            writer.writeHeader("algorithm", "n", "time(ms)", "comparisons", "allocations", "maxDepth");

            long startTime = System.nanoTime();
            runAlgorithm(algo, arr, k, counters, depth);
            long elapsed = (System.nanoTime() - startTime) / 1_000_000;

            writer.writeRow(algo, n, elapsed, counters.getComparisons(), counters.getAllocations(), depth.getMaxDepth());
        }

        System.out.println("Done. Metrics written to " + csvFile);
    }

    private static void runAlgorithm(String algo, int[] arr, int k, MetricsCounters counters, DepthTracker depth) {
        switch (algo.toLowerCase()) {
            case "mergesort":
                new MergeSort(counters, depth).sort(arr);
                break;
            case "quicksort":
                new QuickSort(counters, depth).sort(arr);
                break;
            case "select":
                int result = new DeterministicSelect(counters, depth).select(arr, k);
                System.out.println("k-th element: " + result);
                break;
            case "closest":
                ClosestPair.Point[] points = generateRandomPoints(arr.length);
                ClosestPair.Result res = new ClosestPair(counters, depth).findClosest(points);
                System.out.printf("Closest distance: %.5f between (%.2f, %.2f) and (%.2f, %.2f)%n",
                        res.distance, res.p1.x, res.p1.y, res.p2.x, res.p2.y);
                break;
            default:
                System.err.println("Unknown algorithm: " + algo);
                printUsage();
                System.exit(1);
        }
    }

    private static ClosestPair.Point[] generateRandomPoints(int n) {
        Random rand = new Random();
        ClosestPair.Point[] points = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new ClosestPair.Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
        }
        return points;
    }

    private static String getArgValue(String[] args, String key) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) return args[i + 1];
        }
        throw new IllegalArgumentException("Missing required argument: " + key);
    }

    private static String getArgValueOrDefault(String[] args, String key, String defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) return args[i + 1];
        }
        return defaultValue;
    }

    private static void printUsage() {
        System.out.println("""
            Usage:
              java -jar target/project.jar --algo <mergesort|quicksort|select|closest> [--n <size>] [--k <index>] [--out <csv>]
            
            Examples:
              java -jar target/project.jar --algo mergesort --n 50000 --out merge.csv
              java -jar target/project.jar --algo quicksort --n 100000 --out quick.csv
              java -jar target/project.jar --algo select --n 50000 --k 25000 --out select.csv
              java -jar target/project.jar --algo closest --n 10000 --out closest.csv
            """);
    }
}
