package ab6.test;

import ab6.LZW;
import ab6.impl.Eberl.LZWImpl;
import ab6.impl.Eberl.LZWInBitStream;
import ab6.impl.Eberl.LZWOutBitStream;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;


@Ignore("Long running integration tests")
public class IntegrationTests {

    private static final int ONE_MEGABYTE = 1024 * 1024; // bytes;

    @Test
    public void testLZWInAndOutBuffers() {

        // just go to 27, otherwise VM gives me an OutOfMemoryError
        for (int size = 8; size < 28; size++) {

            System.out.println("Testing with size = " + size);

            LZWOutBitStream outBuffer = new LZWOutBitStream(size);
            for (int i = 0; i < (1 << size); i++) {

                outBuffer.write(i);
            }

            byte[] buffer = outBuffer.toByteArray();
            long expectedLength = (long)Math.ceil(((1 << size) * (long)size) / 8.0);
            Assert.assertEquals(expectedLength, buffer.length);

            LZWInBitStream inBuffer = new LZWInBitStream(size);
            inBuffer.append(buffer);

            for (int i = 0; i < (1 << size); i++) {

                Assert.assertTrue(inBuffer.hasNext());
                Assert.assertEquals(i, inBuffer.getNext());
            }

            Assert.assertFalse(inBuffer.hasNext());
        }
    }

    @Test
    public void encodeDecodeIntegrationTest() {

        LZW lzw = new LZWImpl();

        for (int size = 9; size < 20; size++) {

            System.out.println("Code table size = " + size);

            // whenever code table size is changed, reset it here
            lzw.setCodetableSize(size);

            for (int length = ONE_MEGABYTE; length < 100 * ONE_MEGABYTE; length += ONE_MEGABYTE) {

                Random random = new Random();

                // initialize raw data with some random values
                byte[] rawData = new byte[length];
                random.nextBytes(rawData);

                // first encode
                lzw.setMode(true);
                lzw.data(rawData);
                byte[] encoded = lzw.finish();

                // then decode
                lzw.setMode(false);
                lzw.data(encoded);
                byte[] decoded = lzw.finish();

                Assert.assertArrayEquals(rawData, decoded);
            }
        }
    }

    @Test
    public void encodeDecodeIntegrationTestWithStreaming() {

        LZW lzw = new LZWImpl();

        for (int size = 9; size < 20; size++) {

            System.out.println("Code table size = " + size);

            // whenever code table size is changed, reset it here
            lzw.setCodetableSize(size);

            for (int length = ONE_MEGABYTE; length < 100 * ONE_MEGABYTE; length += ONE_MEGABYTE) {

                Random random = new Random();

                // initialize raw data with some random values
                byte[] rawData = new byte[length];
                random.nextBytes(rawData);


                // first encode
                lzw.setMode(true);

                int currentOffset = 0;
                while (currentOffset < rawData.length) {
                    int len = Math.min(random.nextInt(1024), rawData.length - currentOffset);
                    byte[] subArray = new byte[len];
                    System.arraycopy(rawData, currentOffset, subArray, 0, len);
                    lzw.data(subArray);

                    currentOffset += len;
                }
                byte[] encoded = lzw.finish();

                // then decode
                lzw.setMode(false);

                currentOffset = 0;
                while (currentOffset < encoded.length) {
                    int len = Math.min(random.nextInt(1024), encoded.length - currentOffset);
                    byte[] subArray = new byte[len];
                    System.arraycopy(encoded, currentOffset, subArray, 0, len);
                    lzw.data(subArray);

                    currentOffset += len;
                }

                byte[] decoded = lzw.finish();

                Assert.assertArrayEquals(rawData, decoded);
            }
        }
    }
}
