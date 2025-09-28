package util;

public class MetricsCounters {
    private long comparisons = 0;
    private long allocations = 0;

    public void reset() {
        comparisons = 0;
        allocations = 0;
    }

    public void incComparisons() {
        comparisons++;
    }

    public void addComparisons(long c) {
        comparisons += c;
    }

    public void incAllocations() {
        allocations++;
    }

    public void addAllocations(long a) {
        allocations += a;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getAllocations() {
        return allocations;
    }

    @Override
    public String toString() {
        return "Comparisons=" + comparisons + ", Allocations=" + allocations;
    }
}
