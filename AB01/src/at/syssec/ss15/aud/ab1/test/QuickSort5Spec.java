package at.syssec.ss15.aud.ab1.test;

import at.syssec.ss15.aud.ab1.SearchAlgos;
import at.syssec.ss15.aud.ab1.impl.Eberl.SearchAlgosImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Spec class for testing Quick Sort algorithm using Lomuto's partition scheme (last element as pivot).
 */
public class QuickSort5Spec {

    @Test
    public void CallingQuickSortWithNullWillNotFail() {
        // given
        int[] input = null;
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertEquals(null, input); // if we don't reach this line something went wrong
    }

    @Test
    public void CallingQuickSortWithEmptyArrayReturnsImmediately() {
        // given
        int[] input = {};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{}, input); // if we don't reach this line something went wrong
    }

    @Test
    public void CallingQuickSortWithAnArrayContainingOneElementReturnsImmediately() {
        // given
        int[] input = {42};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{42}, input);
    }

    @Test
    public void CallingQuickSortWithArraysContainingOnlyTwoElementsWorks() {
        // given
        int[] input = {42, 40};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{40, 42}, input);

        // and when
        input = new int[]{39, 40};
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{39, 40}, input);

        // and when
        input = new int[]{42, 42};
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{42, 42}, input);
    }

    @Test
    public void CallingQuickSortWithArraysContainingOnlyThreeElementsWorks() {
        // given
        int[] input = {42, 40, 43};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{40, 42, 43}, input);

        // and when
        input = new int[]{39, 40, 41};
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{39, 40, 41}, input);

        // and when
        input = new int[]{42, 42, 42};
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{42, 42, 42}, input);
    }

    @Test
    public void CallingQuickSortWithArraysContainingOnlyFourElementsWorks() {
        // given
        int[] input = {42, 40, 43, 2};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{2, 40, 42, 43}, input);

        // and when
        input = new int[]{39, 40, 41, 42};
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{39, 40, 41, 42}, input);

        // and when
        input = new int[]{42, 42, 42, 42};
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{42, 42, 42, 42}, input);
    }

    @Test
    public void CallingQuickSortWithASortedArrayKeepsItSorted() {
        // given
        int[] input = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, input);
    }

    @Test
    public void CallingQuickSortWithReverseSortedArrayResortsIt() {
        // given
        int[] input = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, input);
    }

    @Test
    public void CallingQuickSortWithArrayContainingSameElementMultipleTimesWorks() {
        // given
        int[] input = {42, 42, 42, 42, 42, 42, 42, 42, 42, 42};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{42, 42, 42, 42, 42, 42, 42, 42, 42, 42}, input);
    }

    @Test
    public void CallingQuickSortWorks() {
        // given
        int[] input = {42, 1, 5, 3, 2, 7, 1, 9, 4, 1, 42, 10, 37};
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{1, 1, 1, 2, 3, 4, 5, 7, 9, 10, 37, 42, 42}, input);
    }

    @Test
    public void SpecialInput()
    {
        // given
        int[] input = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
        SearchAlgos target = new SearchAlgosImpl();

        // when
        target.QuickSort5(input);

        // then
        Assert.assertArrayEquals(new int[]{ 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6 }, input);
    }
}
