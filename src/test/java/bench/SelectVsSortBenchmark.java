package bench;

import algo.DeterministicSelect;
import util.MetricsCounters;
import util.DepthTracker;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class SelectVsSortBenchmark {

    @Param({"10000", "50000", "100000"})
    public int n;

    private int[] data;
    private int k;

    @Setup(Level.Invocation)
    public void setup() {
        Random rand = new Random(42);
        data = rand.ints(n, 0, 1_000_000).toArray();
        k = n / 2;
    }

    @Benchmark
    public int deterministicSelect() {
        int[] copy = Arrays.copyOf(data, data.length);
        DeterministicSelect select = new DeterministicSelect(new MetricsCounters(), new DepthTracker());
        return select.select(copy, k);
    }

    @Benchmark
    public int sortAndPick() {
        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy[k];
    }
}
