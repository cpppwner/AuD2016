package ab6.test;

import ab6.LZW;
import ab6.impl.Eberl.LZWImpl;
import org.junit.Assert;
import org.junit.Test;

public class LZWSpecs {

    @Test
    public void settingCodeTableSizeLessThanNineThrowsAnException() {

        // given
        LZW lzw = new LZWImpl();

        // then - setting code table size from 9 and upwards
        for (int codeTableSize = 9; codeTableSize <= 20; codeTableSize++) {
            try {
                lzw.setCodetableSize(codeTableSize);
            } catch (IllegalArgumentException e) {
                Assert.fail("IllegalArgumentException not expected");
            }
        }

        // then - setting code table size up to (including) 8
        // also including negative values
        for (int codeTableSize = -10; codeTableSize < 9; codeTableSize++) {
            try {
                lzw.setCodetableSize(codeTableSize);
                Assert.fail("IllegalArgumentException expected");
            } catch (IllegalArgumentException e) {
                // intentionally left empty
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void callingDataWithoutSettingSizeOrModeThrowsAnException() {

        // given
        LZW lzw = new LZWImpl();

        // then the following statement must throw
        lzw.data("foobar".getBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void callingDataWithSetSizeOnly() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setCodetableSize(9);

        // then the following statement must throw
        lzw.data("foobar".getBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void callingDataWithSetModeOnly() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(true);

        // then the following statement must throw
        lzw.data("foobar".getBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void callingFinishWithoutSettingSizeOrModeThrowsAnException() {

        // given
        LZW lzw = new LZWImpl();

        // then the following statement must throw
        lzw.finish();
    }

    @Test(expected = IllegalStateException.class)
    public void callingFinishWithSetSizeOnly() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setCodetableSize(9);

        // then the following statement must throw
        lzw.finish();
    }

    @Test(expected = IllegalStateException.class)
    public void callingFinishWithSetModeOnly() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(false);

        // then the following statement must throw
        lzw.finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void callingDataWithNullArgumentThrowsAnException() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(true);
        lzw.setCodetableSize(9);

        // then the following statement throws
        lzw.data(null);
    }

    @Test(expected = IllegalStateException.class)
    public void callingSetCodetableSizeAfterCompressionHasBeenStartedThrowsAnException() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(true);
        lzw.setCodetableSize(9);

        // when starting compression
        lzw.data("Hello".getBytes());

        // then setting the code table size is not allowed any more
        lzw.setCodetableSize(10);
    }

    @Test(expected = IllegalStateException.class)
    public void callingSetModeAfterCompressionHasBeenStartedThrowsAnException() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(true);
        lzw.setCodetableSize(9);

        // when starting compression
        lzw.data("Hello".getBytes());

        // then setting the code table size is not allowed any more
        lzw.setMode(true);
    }

    @Test
    public void finishingCompressionWithoutAddingDataGivesEmptyArray() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(true);
        lzw.setCodetableSize(9);

        // when
        byte[] obtained = lzw.finish();

        // then
        Assert.assertArrayEquals(new byte[]{}, obtained);
    }

    @Test
    public void finishingDecompressionWithoutAddingDataGivesEmptyArray() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(false);
        lzw.setCodetableSize(9);

        // when
        byte[] obtained = lzw.finish();

        // then
        Assert.assertArrayEquals(new byte[]{}, obtained);
    }

    @Test
    public void callingDataFirstTimeWithNullDoesNotStartCompression() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(true);
        lzw.setCodetableSize(9);

        // when
        try {
            lzw.data(null);
        }catch (IllegalArgumentException e) {
            // intentionally left blank
        }

        // then
        lzw.setMode(false);
        lzw.setCodetableSize(12);
    }

    @Test
    public void callingDataFirstTimeWithNullDoesNotStartDecompression() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(false);
        lzw.setCodetableSize(12);

        // when
        try {
            lzw.data(null);
        }catch (IllegalArgumentException e) {
            // intentionally left blank
        }

        // then
        lzw.setMode(true);
        lzw.setCodetableSize(24);
    }

    @Test
    public void passingNullAfterPassingDataWillNotChangeTheState() {

        // given
        LZW lzw = new LZWImpl();
        lzw.setMode(false);
        lzw.setCodetableSize(12);

        // when
        lzw.data(new byte[]{});
        try {
            lzw.data(null);
        }catch (IllegalArgumentException e) {
            // intentionally left blank
        }

        // then
        try {
            lzw.setMode(true);
            Assert.fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // intentionally left blank
        }
        try {
            lzw.setCodetableSize(24);
            Assert.fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // intentionally left blank
        }
    }
}
