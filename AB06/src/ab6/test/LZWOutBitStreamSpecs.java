package ab6.test;

import ab6.impl.Eberl.LZWOutBitStream;
import org.junit.Assert;
import org.junit.Test;

public class LZWOutBitStreamSpecs {

    @Test
    public void aDefaultConstructedOutBufferGivesAnEmptyArray() {

        // given
        LZWOutBitStream outBuffer = new LZWOutBitStream(12);

        // when
        byte[] obtained = outBuffer.toByteArray();

        // then
        Assert.assertArrayEquals(new byte[]{}, obtained);
    }

    @Test
    public void writingValuesToArrayGivesThemBackAccordingly() {

        // given
        LZWOutBitStream outBuffer = new LZWOutBitStream(10);

        // when
        outBuffer.write(456);
        outBuffer.write(1013);
        outBuffer.write(321);
        outBuffer.write(999);
        byte[] obtained = outBuffer.toByteArray();

        // then
        // expected is pre-calculated using Python (see also LZWInBufferSpecs)
        byte[] expected = {(byte)0x72, (byte)0x3f, (byte)0x55, (byte)0x07, (byte)0xe7};
        Assert.assertArrayEquals(expected, obtained);
    }

    @Test
    public void writingValuesToArrayAlsoWorksIfLastByteIsNotFullyUsed() {

        // given
        LZWOutBitStream outBuffer = new LZWOutBitStream(12);

        // when
        outBuffer.write(3456);
        outBuffer.write(4095);
        outBuffer.write(4037);
        outBuffer.write(2033);
        outBuffer.write(1438);
        outBuffer.write(123);
        byte[] obtained = outBuffer.toByteArray();

        // then
        // expected is pre-calculated using Python (see also LZWInBufferSpecs)
        byte[] expected = {(byte)0xd8, (byte)0x0f, (byte)0xff, (byte)0xfc, (byte)0x57, (byte)0xf1, (byte)0x59, (byte)0xe0, (byte)0x7b};
        Assert.assertArrayEquals(expected, obtained);
    }

    @Test
    public void writingValuesToArrayPerformsReAllocationsIfNecessary() {

        // given
        LZWOutBitStream outBuffer = new LZWOutBitStream(16);

        // when adding couple of values
        for (int i = 0; i < 4000; i++) {
            outBuffer.write(i);
        }

        byte[] obtained = outBuffer.toByteArray();

        // then
        Assert.assertEquals(8000, obtained.length);
        int offset = 0;
        for (int expected = 0; expected < 4000; expected++) {
            int nr = 0;
            nr |= (obtained[offset++] & 0xff);
            nr <<= 8;
            nr |= (obtained[offset++] & 0xff);

            Assert.assertEquals(expected, nr);
        }
    }
}
