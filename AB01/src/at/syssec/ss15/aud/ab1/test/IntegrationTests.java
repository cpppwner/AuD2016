package at.syssec.ss15.aud.ab1.test;

import at.syssec.ss15.aud.ab1.Helper;
import at.syssec.ss15.aud.ab1.SearchAlgos;
import at.syssec.ss15.aud.ab1.impl.Eberl.SearchAlgosImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Integration tests for measuring algorithm's speeds.
 *
 * <p>
 *     Enable those tests if wanted, they won't be available by default.
 * </p>
 */
@Ignore("Long running integration tests")
public class IntegrationTests {

    private static int MIN_SIZE = 100000;
    private static int MAX_SIZE = 1000000;
    private static int STEP_SIZE = MIN_SIZE;

    private static int REPETITIONS = 10;

    private enum SortingAlgorithm {

        BubbleSort,
        QuickSort1,
        QuickSort3,
        QuickSort5
    }

    private static SearchAlgos searchAlgos;
    private static Map<SortingAlgorithm, Consumer<int[]>> sortingAlgorithmConsumerMap;

    @BeforeClass
    public static void setUp()
    {
        // setup search algos + methods to invoke
        searchAlgos = new SearchAlgosImpl();
        sortingAlgorithmConsumerMap = new HashMap<>();
        sortingAlgorithmConsumerMap.put(SortingAlgorithm.BubbleSort, searchAlgos::BubbleSort);
        sortingAlgorithmConsumerMap.put(SortingAlgorithm.QuickSort1, searchAlgos::QuickSort1);
        sortingAlgorithmConsumerMap.put(SortingAlgorithm.QuickSort3, searchAlgos::QuickSort3);
        sortingAlgorithmConsumerMap.put(SortingAlgorithm.QuickSort5, searchAlgos::QuickSort5);
    }

    private void runTests(String outputFilename, Function<Integer, int[]> inputGenerator)
    {
        Map<Integer, Map<SortingAlgorithm, Double>> algorithmDurations = new HashMap<>();

        for (int size = MIN_SIZE; size <= MAX_SIZE; size += STEP_SIZE) {

            // initialize dictionary entry for this size
            algorithmDurations.put(size, new HashMap<>());
            for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
                algorithmDurations.get(size).put(algorithm, Double.valueOf(0.0));
            }

            for (int repetition = 0; repetition < REPETITIONS; repetition++) {

                // generate input to sort
                int[] data = inputGenerator.apply(size);

                // make the reference sorted array (the one to compare to)
                // therefore using Java's built-in sorting, since this should work
                int[] referenceSorted = Arrays.copyOf(data, data.length);
                Arrays.sort(referenceSorted);

                // now run the different algorithms
                for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {

                    // first create a copy of the original array
                    int[] input = Arrays.copyOf(data, data.length);
                    Consumer<int[]> sortingMethod = sortingAlgorithmConsumerMap.get(algorithm);

                    // call sorting method and measure duration in nanoseconds
                    long start = System.nanoTime();
                    sortingMethod.accept(input);
                    long duration = System.nanoTime() - start;

                    // compare the result of our sorting algorithm to the reference
                    Assert.assertArrayEquals(referenceSorted, input);

                    // update the duration in the map -> using microseconds is totally enough
                    double durationInMicroseconds = (double)duration / 1000.0;
                    algorithmDurations.get(size).put(algorithm,
                            algorithmDurations.get(size).get(algorithm) + durationInMicroseconds);
                }

                System.out.println("--> " + outputFilename.replace(".csv", "") + " [#" + size + "] " + (repetition + 1) + "/" + REPETITIONS);
            }
        }

        // last but not least write csv
        produceCsvStatistics(outputFilename, algorithmDurations);
    }

    private void produceCsvStatistics(String outputFilename, Map<Integer, Map<SortingAlgorithm, Double>> algorithmDurations) {

        // produce header for csv
        String[] header = { "#Elements in input",
                            "#Repetitions",
                            "Avg. Duration " + SortingAlgorithm.BubbleSort.name() + " [microseconds]",
                            "Avg. Duration " + SortingAlgorithm.QuickSort1.name() + " [microseconds]",
                            "Avg. Duration " + SortingAlgorithm.QuickSort3.name() + " [microseconds]",
                            "Avg. Duration " + SortingAlgorithm.QuickSort5.name() + " [microseconds]" };


        // produce data for csv
        Object[][] data = new Object[algorithmDurations.size()][header.length];
        int rowIndex = 0;

        for (int size : algorithmDurations.keySet().stream().sorted().mapToInt(e -> e).toArray()) {

            data[rowIndex][0] = size;
            data[rowIndex][1] = REPETITIONS;
            data[rowIndex][2] = algorithmDurations.get(size).get(SortingAlgorithm.BubbleSort) / REPETITIONS;
            data[rowIndex][3] = algorithmDurations.get(size).get(SortingAlgorithm.QuickSort1) / REPETITIONS;
            data[rowIndex][4] = algorithmDurations.get(size).get(SortingAlgorithm.QuickSort3) / REPETITIONS;
            data[rowIndex][5] = algorithmDurations.get(size).get(SortingAlgorithm.QuickSort5) / REPETITIONS;

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

    @Test
    public void RandomInputTest()
    {
        runTests("random.csv", numElements -> Helper.getData(numElements));
    }

    @Test
    public void PresortedInputTest()
    {
        runTests("sorted.csv", numElements ->
        {
            int[] result = new int[numElements];
            for (int i = 0; i < numElements; i++) {
                result[i] = i;
            }

            return result;
        });
    }

    @Test
    public void ReverseSortedInputTest()
    {
        runTests("reversesorted.csv", numElements ->
        {
            int[] result = new int[numElements];
            for (int i = 0; i < numElements; i++) {
                result[numElements - i - 1] = i;
            }

            return result;
        });
    }

    @Test
    public void SameValueInputTest()
    {
        runTests("equalelement.csv", numElements ->
        {
            int randomValue = new Random(System.currentTimeMillis()).nextInt();
            int[] result = new int[numElements];;

            for (int i = 0; i < numElements; i++) {
                result[i] = randomValue;
            }

            return result;
        });
    }
}
