package at.syssec.ss15.aud.ab1.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import at.syssec.ss15.aud.ab1.Helper;
import at.syssec.ss15.aud.ab1.SearchAlgos;
import at.syssec.ss15.aud.ab1.impl.Eberl.SearchAlgosImpl;

public class SearchTest {

    SearchAlgos tools = new SearchAlgosImpl();


    @Test
    public void testBS() {
        int[] data = Helper.getData(10);
        int[] data2 = Arrays.copyOf(data, data.length);

        tools.BubbleSort(data);
        Arrays.sort(data2);
        Assert.assertArrayEquals(data, data2);
    }


    @Test
    public void testQS1() {
        int[] data = Helper.getData(1000);
        int[] data2 = Arrays.copyOf(data, data.length);

        tools.QuickSort1(data);
        Arrays.sort(data2);
        Assert.assertArrayEquals(data, data2);
    }

    @Test
    public void testQS3() {
        int[] data = Helper.getData(1000);
        int[] data2 = Arrays.copyOf(data, data.length);

        tools.QuickSort3(data);
        Arrays.sort(data2);
        Assert.assertArrayEquals(data, data2);
    }

    @Test
    public void testQS5() {
        int[] data = Helper.getData(1000);
        int[] data2 = Arrays.copyOf(data, data.length);

        tools.QuickSort5(data);
        Arrays.sort(data2);
        Assert.assertArrayEquals(data, data2);
    }

}