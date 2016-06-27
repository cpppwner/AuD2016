package at.syssec.ss15.aud.ab1.test;

import at.syssec.ss15.aud.ab1.SearchAlgos;
import at.syssec.ss15.aud.ab1.impl.Eberl.SearchAlgosImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Simple specs for testing Quick sort using JUnit4
 */
public class BubbleSortSpec {

    @Test
    public void CallingBubbleSortWithNullWillNotFail() {
        // given
        int[] input = null;
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertEquals(null, input); // if we don't reach this line something went wrong
    }

    @Test
    public void CallingBubbleSortWithEmptyArrayReturnsImmediately() {
        // given
        int[] input = {};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{}, input); // if we don't reach this line something went wrong
    }

    @Test
    public void CallingBubbleSortWithAnArrayContainingOneElementReturnsImmediately() {
        // given
        int[] input = {42};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{42}, input);
    }

    @Test
    public void CallingBubbleSortWithASortedArrayKeepsItSorted() {
        // given
        int[] input = {1, 2, 3, 4, 5};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, input);
    }

    @Test
    public void CallingBubbleSortWithReverseSortedArrayResortsIt() {
        // given
        int[] input = {5, 4, 3, 2, 1};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, input);
    }

    @Test
    public void CallingBubbleSortWithArrayContainingSameElementMultipleTimesWorks() {
        // given
        int[] input = {42, 42, 42, 42, 42};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{42, 42, 42, 42, 42}, input);
    }

    @Test
    public void CallingBubbleSortWorks() {
        // given
        int[] input = {1, 5, 3, 2, 7, 1, 9, 4, 1};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{1, 1, 1, 2, 3, 4, 5, 7, 9}, input);
    }

    @Test
    public void SpecialInput()
    {
        // given
        int[] input = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.BubbleSort(input);

        // then
        Assert.assertArrayEquals(new int[]{ 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6 }, input);
    }
}
