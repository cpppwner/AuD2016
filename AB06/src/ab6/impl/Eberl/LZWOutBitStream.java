package ab6.impl.Eberl;

import java.util.Arrays;

/**
 * Simple "Output Bit Stream" data class storing data-words from LZW algorithm as bits.
 *
 * <p>
 *     This class is just public for testability, normally it would be package internal.
 *     Since it's used internally only possible use cases are implemented and covered.
 * </p>
 */
public class LZWOutBitStream {

    /**
     * The default size of the data buffer which is 4KB
     */
    public static final int DEFAULT_DATA_SIZE_IN_BYTES = 4 * 1024;

    /**
     * The number of bits which are needed for one data-word.
     */
    private final int numBits;

    /**
     * The raw byte data.
     */
    private byte[] data;

    /**
     * The current byte offset in {@link LZWInBitStream#data}.
     */
    private int byteOffset;

    /**
     * Additional bit offset in {@link LZWInBitStream#data}.
     */
    private int bitOffset;

    /**
     * Constructor.
     * @param numBits The number of bits forming one data word.
     */
    public LZWOutBitStream(int numBits) {

        this.numBits = numBits;
        data = new byte[DEFAULT_DATA_SIZE_IN_BYTES];
        byteOffset = 0;
        bitOffset = 0;
    }

    /**
     * Write one data word to internal buffer.
     *
     * <p>
     *     Note: only the last relevant bits ({@link LZWOutBitStream#numBits}) are stored.
     *     Additional bits which might be set are ignored.
     * </p>
     *
     * @param data The data word to write.
     */
    public void write(int data) {

        if (!isEnoughSpaceLeft()) {
            this.data = Arrays.copyOf(this.data, (int)Math.min(Integer.MAX_VALUE, this.data.length * 2L));
        }

        // store initial bits, if it's not a full byte
        int numBitsLeft = numBits;
        if (bitOffset > 0) {
            // some bits to store data
            int shiftBits = numBitsLeft - (LZWStreamingHandler.BITS_PER_BYTE - bitOffset);
            byte tmp = (byte)((data >>> shiftBits) & 0xff);
            this.data[byteOffset++] |= tmp;
            numBitsLeft -= (LZWStreamingHandler.BITS_PER_BYTE - bitOffset);
            bitOffset = 0;
        }

        // store full bytes, as long as there are full bytes
        while (numBitsLeft >= LZWStreamingHandler.BITS_PER_BYTE) {
            int shiftBits = numBitsLeft - LZWStreamingHandler.BITS_PER_BYTE;
            this.data[byteOffset++] = (byte)((data >>> shiftBits) & 0xff);
            numBitsLeft -= LZWStreamingHandler.BITS_PER_BYTE;
        }

        // store remaining bits from data.
        if (numBitsLeft > 0) {
            int shiftBits = LZWStreamingHandler.BITS_PER_BYTE - numBitsLeft;
            byte tmp = (byte)((data << shiftBits) & 0xff);
            this.data[byteOffset] = tmp;
            bitOffset = numBitsLeft;
        }
    }

    /**
     * Helper method used to check if there is enough space left to store the next data word.
     *
     * @return {@code true} if enough space is left in {@link LZWOutBitStream#data}, {@code false} otherwise.
     */
    private boolean isEnoughSpaceLeft() {

        int numBits = ((data.length - byteOffset) * LZWStreamingHandler.BITS_PER_BYTE);
        numBits -= bitOffset; // subtract already consumed bits

        return numBits >= this.numBits;
    }

    /**
     * Helper method to get the output buffer byte array.
     *
     * <p>
     *     This method copies the internal buffer into a new byte array. The new byte array has exactly the size
     *     to store all data words written so far, but not more.
     * </p>
     *
     * @return A byte array containing a copy of the data words written to this buffer so far.
     */
    public byte[] toByteArray() {

        return Arrays.copyOf(data, bitOffset != 0 ? byteOffset + 1 : byteOffset);
    }
}
