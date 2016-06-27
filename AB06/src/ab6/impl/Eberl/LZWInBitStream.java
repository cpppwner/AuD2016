package ab6.impl.Eberl;

/**
 * Simple "BitInputStream"-like class to store incoming bytes for LZW algorithm and read data-words from it.
 *
 * <p>
 *     This class is just public for testability, normally it would be internal.
 *     Since it's used internally only possible use cases are implemented and covered.
 * </p>
 *
 * <p>
 *     Since the data is consumed immediately after appending it, only the original reference + possible
 *     leftovers from previous runs are stored and the data is not copied to prevent waste of memory.
 * </p>
 */
public class LZWInBitStream {

    /**
     * The number of bits which are needed for one data-word.
     */
    private final int numBits;

    /**
     * The raw byte data.
     */
    private byte[] data;
    /**
     * The offset in {@link LZWInBitStream#data}.
     */
    private int byteOffset;
    /**
     * Additional bit offset in {@link LZWInBitStream#data}.
     */
    private int bitOffset;

    /**
     * Some leftover in streaming case.
     */
    int leftover;
    /**
     * The number of bits which are relevant in leftover.
     */
    int leftoverBits;

    /**
     * Constructor.
     *
     * @param numBits The number of bits to represent one data-word.
     */
    public LZWInBitStream(int numBits) {

        this.numBits = numBits;
        data = null;
        byteOffset = 0;
        bitOffset = 0;
        leftover = 0;
        leftoverBits = 0;
    }

    /**
     * Append data to this buffer.
     *
     * <p>
     *     Actually no data is copied. All data which is left-over is stored in an internal integer and
     *     the new data passed to this method is saved by reference.
     * </p>
     *
     * @param data The data to append.
     */
    public void append(byte[] data) {

        if (hasNext())
            throw new IllegalStateException();

        if (this.data != null) {
            storeLeftover();
        }

        this.data = data;
        byteOffset = 0;
        bitOffset = 0;
    }

    /**
     * Store data which is left over from previous call to {@link LZWInBitStream#append}.
     */
    private void storeLeftover() {

        // there is leftover in data

        if (bitOffset > 0) {
            // some bits were not consumed
            leftover <<= (LZWStreamingHandler.BITS_PER_BYTE - bitOffset);
            leftover += (data[byteOffset++] & 0xff);
            leftoverBits = LZWStreamingHandler.BITS_PER_BYTE - bitOffset;

            // mask out only the bits which were not consumed up to now
            leftover &= (0xff >>> bitOffset);
        }

        // consume remaining bytes
        while (byteOffset < data.length) {

            leftover <<= LZWStreamingHandler.BITS_PER_BYTE;
            leftover |= (data[byteOffset++] & 0xff);
            leftoverBits += LZWStreamingHandler.BITS_PER_BYTE;
        }
    }

    /**
     * Get flag indicating whether this buffer has enough data to retrieve the next data word.
     *
     * <p>
     *     Note that the word consists of n bits, where the number n was given in constructor.
     *     see also {@link LZWInBitStream#numBits}
     * </p>
     *
     * @return {@code true} if this buffer contains enough bits for a data word, {@code false} otherwise.
     */
    public boolean hasNext() {

        long numBits = 0;
        if (data != null)
            numBits += ((long)(data.length - byteOffset) * LZWStreamingHandler.BITS_PER_BYTE);

        numBits -= bitOffset; // subtract already consumed bits
        numBits += leftoverBits; // add bits from leftover

        return numBits >= this.numBits;
    }

    /**
     * Get the next data word.
     *
     * @return Next data word.
     * @exception IllegalStateException if {@link LZWInBitStream#hasNext()} gives {@code false}
     */
    public int getNext() {

        if (!hasNext())
            throw new IllegalStateException();

        int result = 0;
        int bitsRemaining = numBits;

        if (leftoverBits != 0) {
            // take what was leftover
            result = leftover;
            bitsRemaining -= leftoverBits;

            leftover = 0;
            leftoverBits = 0;
        } else if (bitOffset != 0){
            //  started to read from a byte, which is not fully consumed, read remaining bytes
            result = (data[byteOffset++] & (0xff >> bitOffset));
            bitsRemaining -= LZWStreamingHandler.BITS_PER_BYTE - bitOffset;
            bitOffset = 0;
        }

        // consume full bytes
        for (; bitsRemaining >= LZWStreamingHandler.BITS_PER_BYTE; bitsRemaining -= LZWStreamingHandler.BITS_PER_BYTE) {
            result <<= LZWStreamingHandler.BITS_PER_BYTE;
            result |= (data[byteOffset++] & 0xff);
        }

        // check if there are some bits remaining to consume
        if (bitsRemaining != 0) {

            // there are
            result <<= bitsRemaining;
            result |= ((data[byteOffset] & 0xff) >>> (LZWStreamingHandler.BITS_PER_BYTE - bitsRemaining));

            bitOffset = bitsRemaining;
        }

        return result;
    }
}
