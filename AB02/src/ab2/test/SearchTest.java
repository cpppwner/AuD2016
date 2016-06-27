package ab2.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import ab2.Helper;
import ab2.SearchAlgos;
import ab2.impl.Eberl.SearchAlgosImpl;

public class SearchTest {

    SearchAlgos tools = new SearchAlgosImpl();

    @Test
    public void testBinarySearch() throws Exception {

        int value = 7698;
        int[] data = Helper.getData(1000);
        data[0] = value;
        Arrays.sort(data);


        int result = tools.BinarySearch(data, value);
        int result2 = Arrays.binarySearch(data, value);

        Assert.assertEquals(result2,result);
    }

    @Test
    public void testInterpolationSearch() throws Exception {
        int[] data = Helper.getData(1000);
        Arrays.sort(data);
        int value = data[0];  
        int result = tools.InterpolationSearch(data, value);
        Assert.assertEquals(0, result);
        
        

        int[] data2 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        int pos = 17;
        value = data2[pos];
        result = tools.InterpolationSearch(data2, value);
        Assert.assertEquals(pos, result);

        
        
        
        result = tools.InterpolationSearch(data2, 21);
        Assert.assertEquals(-1, result);
    }

}