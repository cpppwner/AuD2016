package ab2.test;

import ab2.SearchAlgos;
import ab2.impl.Eberl.SearchAlgosImpl;
import org.junit.Assert;
import org.junit.Test;

public class InterpolationSearchSpec {

    @Test(expected=IllegalArgumentException.class)
    public void whenArrayToSearchInIsNullAnExceptionIsThrown() {

        // given
        SearchAlgos target = new SearchAlgosImpl();

        // then
        target.InterpolationSearch(null, 1);
    }

    @Test
    public void searchingForValueInEmptyArrayReturnsMinusOne() {

        // given
        SearchAlgos target = new SearchAlgosImpl();

        // when
        int obtained = target.InterpolationSearch(new int[0], 1);

        // then
        Assert.assertEquals(-1, obtained);
    }

    @Test
    public void searchingForValueInArrayReturnsAppropriateIndex() {

        // given
        int[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SearchAlgos target = new SearchAlgosImpl();

        // then
        for (int index = 0; index < data.length; index++)
            Assert.assertEquals(index, target.InterpolationSearch(data, data[index]));
    }

    @Test
    public void searchingForNonExistingValueInArrayReturnsMinusOne() {

        // given
        int[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SearchAlgos target = new SearchAlgosImpl();

        // then
        Assert.assertEquals(-1, target.InterpolationSearch(data, 11));
        Assert.assertEquals(-1, target.InterpolationSearch(data, -1));
    }

    @Test
    public void searchingForValueInArrayContainingAlwaysTheSameReturnsFirstIndex() {

        // given
        SearchAlgos target = new SearchAlgosImpl();

        // then
        Assert.assertEquals(0, target.InterpolationSearch(new int[] {1, 1, 1, 1, 1}, 1));
    }


    @Test
    public void searchingForValueInNotEquallyDistributedInputGetsValueAccordingly() {

        // given
        SearchAlgos target = new SearchAlgosImpl();

        // then
        Assert.assertEquals(2, target.InterpolationSearch(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, Integer.MAX_VALUE}, 3));
    }

    @Test
    public void searchingAlsoWorksForPositiveAndNegativeValues() {

        // given
        int[] data = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
        SearchAlgos target = new SearchAlgosImpl();

        // then
        for (int index = 0; index < data.length; index++)
            Assert.assertEquals(index, target.InterpolationSearch(data, data[index]));
    }

    @Test
    public void searchingAlsoWorksWhenArrayContainsOnlyNegativeValues() {

        // given
        int[] data = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1};
        SearchAlgos target = new SearchAlgosImpl();

        // then
        for (int index = 0; index < data.length; index++)
            Assert.assertEquals(index, target.InterpolationSearch(data, data[index]));
    }

    @Test
    public void searchingAlsoWorksWhenArrayContainsOnlyValuesCloseToIntegerMinValue() {

        // given
        int[] data = new int[10];
        for (int index = 0; index < data.length; index++)
            data[index] = Integer.MIN_VALUE + index;

        SearchAlgos target = new SearchAlgosImpl();

        // then
        for (int index = 0; index < data.length; index++)
            Assert.assertEquals(index, target.InterpolationSearch(data, data[index]));
    }

    @Test
    public void searchingAlsoWorksWhenArrayContainsOnlyValuesCloseToIntegerMaxValue() {

        // given
        int[] data = new int[10];
        for (int index = 0; index < data.length; index++)
            data[index] = Integer.MAX_VALUE - data.length + index;

        SearchAlgos target = new SearchAlgosImpl();

        // then
        for (int index = 0; index < data.length; index++)
            Assert.assertEquals(index, target.InterpolationSearch(data, data[index]));
    }

    @Test
    public void searchingAlsoWorksForSpecialArithmeticUnderflowCondition() {

        // given
        int[] data = new int[1000];
        for (int index = 0; index < data.length - 1; index++)
            data[index] = Integer.MIN_VALUE + index;
        data[data.length - 1] = Integer.MAX_VALUE;

        SearchAlgos target = new SearchAlgosImpl();

        // then
        for (int index = 0; index < data.length; index++)
            Assert.assertEquals(index, target.InterpolationSearch(data, data[index]));
    }
}
