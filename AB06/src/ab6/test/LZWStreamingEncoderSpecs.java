package ab6.test;

import ab6.impl.Eberl.LZWStreamingEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class LZWStreamingEncoderSpecs {

    @Test
    public void passingEmptyArrayWillNotHaveAnyEffect() {

        // given
        LZWStreamingEncoder encoder = new LZWStreamingEncoder(12); // take 16, cause it's easier to calculate

        // when
        encoder.data(new byte[]{});
        byte[] obtained = encoder.finish();

        // then
        Assert.assertArrayEquals(new byte[]{}, obtained);
    }

    @Test
    public void encodingWhenFinishFlushesPrefix() {

        // given
        LZWStreamingEncoder encoder = new LZWStreamingEncoder(10); // take 16, cause it's easier to calculate

        // when
        encoder.data("aab".getBytes(Charset.forName("ISO-8859-1")));
        encoder.data("aa".getBytes(Charset.forName("ISO-8859-1")));
        byte[] obtained = encoder.finish();

        // then
        byte[] expected = {(byte)0x18, (byte)0x46, (byte)0x11, (byte)0x89, (byte)0x01}; // pre-computed using Python
        Assert.assertArrayEquals(expected, obtained);
    }

    @Test
    public void basicEncodingAsInWikipediaWorks() {

        // given
        LZWStreamingEncoder encoder = new LZWStreamingEncoder(16); // take 16, cause it's easier to calculate
        String message = "LZWLZ78LZ77LZCLZMWLZAP";

        // when
        encoder.data(message.getBytes(Charset.forName("ISO-8859-1")));
        byte[] obtained = encoder.finish();

        // then
        byte[] expected = {
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
        Assert.assertArrayEquals(expected, obtained);
    }


    @Test
    public void streamingEncodingAsInWikipediaWorks() {

        // given
        LZWStreamingEncoder encoder = new LZWStreamingEncoder(16); // take 16, cause it's easier to calculate
        String message = "LZWLZ78LZ77LZCLZMWLZAP";

        // when
        for (int i = 1; i <= message.length(); i++)
            encoder.data(message.substring(i - 1, i).getBytes(Charset.forName("ISO-8859-1")));
        byte[] obtained = encoder.finish();

        // then
        byte[] expected = {
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
        Assert.assertArrayEquals(expected, obtained);
    }

    @Test
    public void encodingWorksWithAllByteValues() {

        // given
        LZWStreamingEncoder encoder = new LZWStreamingEncoder(16);
        String data = "";
        for (int i = 0; i < 256; i++) {
            data += (char)i;
        }

        // when
        encoder.data(data.getBytes(Charset.forName("ISO-8859-1")));
        byte[] obtained = encoder.finish();

        // then
        Assert.assertEquals(512, obtained.length);
    }
}
