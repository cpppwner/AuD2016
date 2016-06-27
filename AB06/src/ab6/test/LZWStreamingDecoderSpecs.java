package ab6.test;

import ab6.impl.Eberl.LZWStreamingDecoder;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class LZWStreamingDecoderSpecs {

    @Test
    public void passingEmptyArrayWillNotHaveAnyEffect() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(12); // take 16, cause it's easier to calculate

        // when
        decoder.data(new byte[]{});
        byte[] obtained = decoder.finish();

        // then
        Assert.assertArrayEquals(new byte[]{}, obtained);
    }

    @Test
    public void basicDecodingAsInWikipediaWorks() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16); // take 16, cause it's easier to calculate
        byte[] data = {
                (byte)0x00, (byte)0x4c, // L
                (byte)0x00, (byte)0x5a, // Z
                (byte)0x00, (byte)0x57, // W
                (byte)0x01, (byte)0x01, // LZ
                (byte)0x00, (byte)0x37, // 7
                (byte)0x00, (byte)0x38, // 8
                (byte)0x01, (byte)0x04, // LZ7
                (byte)0x00, (byte)0x37, // 7
                (byte)0x01, (byte)0x01, // LZ
                (byte)0x00, (byte)0x43, // C
                (byte)0x01, (byte)0x01, // LZ
                (byte)0x00, (byte)0x4d, // M
                (byte)0x01, (byte)0x03, // WL
                (byte)0x00, (byte)0x5a, // Z
                (byte)0x00, (byte)0x41, // A
                (byte)0x00, (byte)0x50, // P
        };

        // when
        decoder.data(data);
        byte[] obtained = decoder.finish();

        // then
        String expectedMessage = "LZWLZ78LZ77LZCLZMWLZAP";
        Assert.assertEquals(expectedMessage, new String(obtained));
    }


    @Test
    public void streamingDecodingAsInWikipediaWorks() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16); // take 16, cause it's easier to calculate
        byte[] data = {
                (byte)0x00, (byte)0x4c, // L
                (byte)0x00, (byte)0x5a, // Z
                (byte)0x00, (byte)0x57, // W
                (byte)0x01, (byte)0x01, // LZ
                (byte)0x00, (byte)0x37, // 7
                (byte)0x00, (byte)0x38, // 8
                (byte)0x01, (byte)0x04, // LZ7
                (byte)0x00, (byte)0x37, // 7
                (byte)0x01, (byte)0x01, // LZ
                (byte)0x00, (byte)0x43, // C
                (byte)0x01, (byte)0x01, // LZ
                (byte)0x00, (byte)0x4d, // M
                (byte)0x01, (byte)0x03, // WL
                (byte)0x00, (byte)0x5a, // Z
                (byte)0x00, (byte)0x41, // A
                (byte)0x00, (byte)0x50, // P
        };

        // when
        for (byte aData : data)
            decoder.data(new byte[]{aData});
        byte[] obtained = decoder.finish();

        // then
        String expectedMessage = "LZWLZ78LZ77LZCLZMWLZAP";
        Assert.assertEquals(expectedMessage, new String(obtained));
    }

    @Test
    public void decodingWorksWithAllByteValues() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16);
        byte[] data = new byte[512];
        for (int i = 0; i < 256; i++) {
            data[i * 2] = (byte)((i >>> 8) & 0xff);
            data[i * 2 + 1] = (byte)(i & 0xff);
        }

        // when
        decoder.data(data);
        byte[] obtained = decoder.finish();

        // then
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++)
            sb.append((char)i);

        Assert.assertEquals(sb.toString(), new String(obtained, Charset.forName("ISO-8859-1")));
    }

    @Test
    public void streamingCaseWhenThereIsNoCodeAfterCodeTableReset() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16);
        byte[] data = { (byte)0x00, (byte)0x30, (byte)0x00, (byte)0x31, (byte)0x01, (byte)0x00};

        // when
        decoder.data(data);
        byte[] obtained = decoder.finish();

        // then
        Assert.assertArrayEquals(new byte[]{0x30, 0x31}, obtained);
    }

    @Test(expected = IllegalStateException.class)
    public void passingUnexpectedCharacterAtBeginningThrowsIllegalStateException() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16);
        byte[] data = { (byte)0x01, (byte)0xff, (byte)0x00, (byte)0x42 };

        // then
        decoder.data(data);
    }

    @Test(expected = IllegalStateException.class)
    public void passingUnexpectedCharacterInTheMiddleThrowsIllegalStateException() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16);
        byte[] data = { (byte)0x00, (byte)0x42, (byte)0x00, (byte)0x43, (byte)0x01, (byte)0xff };

        // then
        decoder.data(data);
    }

    @Test(expected = IllegalStateException.class)
    public void passingUnexpectedCharacterAfterCodeTableReset() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16);
        byte[] data = { (byte)0x00, (byte)0x42, (byte)0x00, (byte)0x43, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0xef};

        // then
        decoder.data(data);
    }

    @Test(expected = IllegalStateException.class)
    public void whenResetIsNotEncounteredAndCodeTableGetsTooBig() {

        // given
        LZWStreamingDecoder decoder = new LZWStreamingDecoder(16);
        byte[] data = new byte[(((1 << 16) - 1) - 256) * 2];
        for (int i = 0; i <= data.length / 2; i++) {

            if (i == 256)
                continue; // skip reset code

            byte high = (byte)((i >>> 8) & 0xff);
            byte low = (byte)(i & 0xff);

            data[i < 256 ? i * 2 : i * 2 - 2] = high;
            data[i < 256 ? i * 2 + 1 : i * 2 - 1] = low;
        }

        // when
        decoder.data(data);

        // then
        decoder.data(new byte[]{0x01, 0x01, 0x01, 0x02});
    }
}
