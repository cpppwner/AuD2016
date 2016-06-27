package ab2.test;

import ab2.Helper;

import ab2.impl.Eberl.SearchAlgosImpl;
import org.junit.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Integration tests for measuring seach algorithm's speeds.
 *
 * <p>
 *     Enable those tests if wanted, they won't be available by default.
 * </p>
 */
@Ignore("Long running integration tests")
public class IntegrationTests {

    private static final int MIN_SIZE = 1000 * 1000;
    private static final int MAX_SIZE = MIN_SIZE * 500;
    private static final int STEP_SIZE = MIN_SIZE;

    private static final int REPETITIONS = 100 * 1000;

    private enum SearchAlgorithm {

        BinarySearch,
        InterpolationSearch
    }

    /** Map containing measured average duration for searching */
    private Map<Integer, Map<SearchAlgorithm, Double>> algorithmDurations;
    private Map<Integer, Map<SearchAlgorithm, Long>> numValueComparisons;

    /** instance of our search algorithms */
    private static SearchAlgosImpl searchAlgos;

    @BeforeClass
    public static void setUpClass() {

        searchAlgos = new SearchAlgosImpl();
    }

    @Before
    public void setUpTest() {

        algorithmDurations = new HashMap<>();
        numValueComparisons = new HashMap<>();

        // reset the search counters
        searchAlgos.binarySearchValueCompCounter = 0;
        searchAlgos.interpolationSearchValueCompCounter = 0;
    }

    @Test
    public void searchSecondLastElementInNonEquallyDistributedArray() {

        for (int size = MIN_SIZE; size <= MAX_SIZE; size += STEP_SIZE) {

            final int currentSize = size;

            // generate data
            final int[] data = new int[size];
            for (int i = 0; i < size - 1; i++)
                data[i] = Integer.MIN_VALUE + i;
            data[size - 1] = Integer.MAX_VALUE;

            // run tests
            runTest(data, () -> data[currentSize-2]);
        }

        // and produce CSV
        produceCsvStatistics("secondlast.csv");
    }

    @Test
    public void searchFirstElementInEquallyDistributedArray() {

        for (int size = MIN_SIZE; size <= MAX_SIZE; size += STEP_SIZE) {

            // generate data
            final int[] data = new int[size];
            for (int i = 0; i < size; i++)
                data[i] = i * 2;

            // run tests
            runTest(data, () -> data[0]);
        }

        // and produce CSV
        produceCsvStatistics("first.csv");
    }

    @Test
    public void randomSearchInRandomData() {

        for (int size = MIN_SIZE; size <= MAX_SIZE; size += STEP_SIZE) {

            // generate data
            final int[] data = Helper.getSortedData(size);

            // run tests
            runTest(data, () -> data[new Random(System.currentTimeMillis()).nextInt(data.length)]);
        }

        // and produce CSV
        produceCsvStatistics("random.csv");
    }

    private void runTest(int[] data, Supplier<Integer> valueProvider) {

        System.out.println("Current size: " + data.length);

        // first add missing stuff to the map
        algorithmDurations.put(data.length, new HashMap<>());
        numValueComparisons.put(data.length, new HashMap<>());

        searchAlgos.binarySearchValueCompCounter = 0;
        searchAlgos.interpolationSearchValueCompCounter = 0;

        double totalDurationBinSearchNanoSec = 0;
        double totalDurationInterpolationSearchNanoSec = 0;

        long start;
        int index, value;

        for (int repetition = 0; repetition < REPETITIONS; repetition++) {

            // get the value to search for
            value = valueProvider.get();

            // first do binary search
            start = System.nanoTime();
            index = searchAlgos.BinarySearch(data, value);
            totalDurationBinSearchNanoSec += (System.nanoTime() - start);

            // make sure the value found is correct
            Assert.assertEquals(data[index], value);

            // then do interpolation search
            start = System.nanoTime();
            index = searchAlgos.InterpolationSearch(data, value);
            totalDurationInterpolationSearchNanoSec += (System.nanoTime() - start);

            // make sure the value found is correct
            Assert.assertEquals(data[index], value);
        }

        // last but not least update the map with average durations in nanoseconds
        algorithmDurations.get(data.length).put(SearchAlgorithm.BinarySearch, totalDurationBinSearchNanoSec);
        algorithmDurations.get(data.length).put(SearchAlgorithm.InterpolationSearch, totalDurationInterpolationSearchNanoSec);

        // and also the number of value comparisons we had
        numValueComparisons.get(data.length).put(SearchAlgorithm.BinarySearch, searchAlgos.binarySearchValueCompCounter);
        numValueComparisons.get(data.length).put(SearchAlgorithm.InterpolationSearch, searchAlgos.interpolationSearchValueCompCounter);
    }

    private void produceCsvStatistics(String outputFilename) {

        // produce header for csv
        String[] header = { "#Elements in input",
                "#Repetitions",
                "Avg. Duration " + SearchAlgorithm.BinarySearch.name() + " [nanoseconds]",
                "Avg. Duration " + SearchAlgorithm.InterpolationSearch.name() + " [nanoseconds]]",
                "Total #value comparison "  + SearchAlgorithm.BinarySearch.name(),
                "Total #value comparison "  + SearchAlgorithm.InterpolationSearch.name() };


        // produce data for csv
        Object[][] data = new Object[algorithmDurations.size()][header.length];
        int rowIndex = 0;

        for (int size : algorithmDurations.keySet().stream().sorted().mapToInt(e -> e).toArray()) {

            data[rowIndex][0] = size;
            data[rowIndex][1] = REPETITIONS;
            data[rowIndex][2] = algorithmDurations.get(size).get(SearchAlgorithm.BinarySearch) / REPETITIONS;
            data[rowIndex][3] = algorithmDurations.get(size).get(SearchAlgorithm.InterpolationSearch) / REPETITIONS;
            data[rowIndex][4] = numValueComparisons.get(size).get(SearchAlgorithm.BinarySearch);
            data[rowIndex][5] = numValueComparisons.get(size).get(SearchAlgorithm.InterpolationSearch);

            rowIndex += 1;
        }

        // finally write the stuff to csv file
        try {
            Helper.exportCSV(header, data, outputFilename);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
