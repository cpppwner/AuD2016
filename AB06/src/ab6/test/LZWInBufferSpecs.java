package ab6.test;

import ab6.impl.Eberl.LZWInBitStream;
import org.junit.Assert;
import org.junit.Test;

public class LZWInBufferSpecs {

    @Test
    public void aDefaultConstructedInBufferDoesNotHaveNext() {

        // given
        LZWInBitStream inBuffer = new LZWInBitStream(12);

        // then
        Assert.assertFalse(inBuffer.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void whenCallingGetNextOnDefaultConstructedInBufferAnExceptionIsThrown() {

        // given
        LZWInBitStream inBuffer = new LZWInBitStream(12);

        // then the exception is thrown
        inBuffer.getNext();
    }

    @Test(expected = IllegalStateException.class)
    public void appendingWithLeftoverFromPreviousAppendThrowsException() {

        // given
        LZWInBitStream inBuffer = new LZWInBitStream(10);
        // 456, 1013, 321, 999  - pre-calculated using Python
        byte[] data = {(byte)0x72, (byte)0x3f, (byte)0x55, (byte)0x07, (byte)0xe7};
        inBuffer.append(new byte[]{data[0], data[1]});

        // then consume some data
        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(456, inBuffer.getNext());

        // and when appending the next byte, there will be a left-over buffer created
        inBuffer.append(new byte[]{data[2]});

        // then verify data
        Assert.assertTrue(inBuffer.hasNext());

        // and then append another time
        inBuffer.append(new byte[]{data[3]});
    }

    @Test
    public void getNextOnInitializedBufferGivesNextValue() {

        // given
        LZWInBitStream inBuffer = new LZWInBitStream(12);
        // 3456, 4095, 4037, 2033, 1438, 123 - pre-calculated using Python
        byte[] data = {(byte)0xd8, (byte)0x0f, (byte)0xff, (byte)0xfc, (byte)0x57, (byte)0xf1, (byte)0x59, (byte)0xe0, (byte)0x7b};

        // when
        inBuffer.append(data);

        // then
        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(3456, inBuffer.getNext());

        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(4095, inBuffer.getNext());

        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(4037, inBuffer.getNext());

        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(2033, inBuffer.getNext());

        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(1438, inBuffer.getNext());

        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(123, inBuffer.getNext());

        Assert.assertFalse(inBuffer.hasNext());
    }

    @Test
    public void getNextWithStreamingCase() {

        // given
        LZWInBitStream inBuffer = new LZWInBitStream(10);
        // 456, 1013, 321, 999  - pre-calculated using Python
        byte[] data = {(byte)0x72, (byte)0x3f, (byte)0x55, (byte)0x07, (byte)0xe7};

        // when
        inBuffer.append(new byte[]{data[0]});

        // then
        Assert.assertFalse(inBuffer.hasNext());

        // and when adding some more data
        inBuffer.append(new byte[]{data[1]});

        // then
        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(456, inBuffer.getNext());
        Assert.assertFalse(inBuffer.hasNext());

        // and when adding some more data
        inBuffer.append(new byte[]{data[2], data[3]});
        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(1013, inBuffer.getNext());
        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(321, inBuffer.getNext());
        Assert.assertFalse(inBuffer.hasNext());

        // and when adding last data part
        inBuffer.append(new byte[]{data[4]});
        Assert.assertTrue(inBuffer.hasNext());
        Assert.assertEquals(999, inBuffer.getNext());
        Assert.assertFalse(inBuffer.hasNext());
    }
}
