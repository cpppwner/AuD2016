package ab6.impl.Eberl;

/**
 * LZW Streaming Handler interface.
 */
public interface LZWStreamingHandler {

    /**
     * Special code for Encoder/Decoder indicating a code table reset.
     */
    int RESET_CODE_TABLE_CODE = 256;

    /**
     * Constant describing the number of bits for one byte.
     */
    int BITS_PER_BYTE = 8;

    /**
     * Implement this to perform the streaming operation.
     *
     * @param data The data for the streaming operation.
     */
    void data(byte[] data);

    /**
     * Finish currently ongoing streaming operation.
     *
     * @return Result of all calls made to {@link LZWStreamingHandler#data}.
     */
    byte[] finish();
}
