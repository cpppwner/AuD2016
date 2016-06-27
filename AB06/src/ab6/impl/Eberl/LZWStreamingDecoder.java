package ab6.impl.Eberl;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link LZWStreamingHandler} for decoding operations.
 */
public class LZWStreamingDecoder implements LZWStreamingHandler {

    private static final int LAST_NOT_INITIALIZED = -1;

    /**
     * The Input Bit Stream used during decoding process.
     */
    private final LZWInBitStream inBuffer;

    /**
     * The Output Bit Stream used during decoding process.
     */
    private final LZWOutBitStream outBuffer;

    /**
     * Code table used for LZW decoding.
     */
    private final Map<Integer, String> codeTable;

    /**
     * The number of bits for one input word.
     */
    private final int size;

    /**
     * The last read data word.
     */
    private int last;

    /**
     * Constructor.
     *
     * @param size The number of bits forming a data word from input buffer.
     */
    public LZWStreamingDecoder(int size) {

        this.size = size;
        inBuffer = new LZWInBitStream(size);
        outBuffer = new LZWOutBitStream(BITS_PER_BYTE);
        codeTable = new HashMap<>();
        last = LAST_NOT_INITIALIZED;

        initializeCodeTable();
    }

    @Override
    public void data(byte[] data) {

        if (data.length == 0)
            return; // nothing to decode

        inBuffer.append(data);

        decode();
    }

    @Override
    public byte[] finish() {

        return outBuffer.toByteArray();
    }

    /**
     * Method implementing the LZW decoding algorithm.
     */
    private void decode() {

        if(last == LAST_NOT_INITIALIZED && inBuffer.hasNext()) {
            // last was not yet read
            last = inBuffer.getNext();
            if (!codeTable.containsKey(last))
                throw new IllegalStateException("illegal character encountered"); // message must start with something known

            outBuffer.write(codeTable.get(last).charAt(0));
        }

        while(inBuffer.hasNext()) {

            int next = inBuffer.getNext();

            if (next == LZWStreamingHandler.RESET_CODE_TABLE_CODE) {
                // special character indicating that the code table must be reset
                // also reset last in this case and re-read it, if possible
                reset();
                if (!inBuffer.hasNext())
                    return;

                last = inBuffer.getNext();
                if (!codeTable.containsKey(last))
                    throw new IllegalStateException("illegal character encountered"); // message must re-start with something known

                outBuffer.write(codeTable.get(last).charAt(0));
                continue;
            }

            String nextEntry;
            if (codeTable.containsKey(next)) {
                nextEntry = codeTable.get(last) + codeTable.get(next).charAt(0);
            } else if (next == (codeTable.size() + 1)){
                nextEntry = codeTable.get(last) + codeTable.get(last).charAt(0);
            } else {
                // bad data case
                throw new IllegalStateException("illegal character encountered");
            }

            // add next entry to code table, but make sure it has space
            if (!codeTableHasSpace())
                throw new IllegalStateException("code table out of space");

            codeTable.put(codeTable.size() + 1, nextEntry);
            codeTable.get(next).codePoints().forEach(outBuffer::write);
            last = next;
        }
    }

    /**
     * Initialize and pre-fill the code table with the alphabet.
     */
    private void initializeCodeTable() {

        for (int i = 0; i < LZWStreamingHandler.RESET_CODE_TABLE_CODE; i++) {
            codeTable.put(i, "" + (char)i);
        }
    }

    /**
     * Reset decoder when a special reset character was encountered.
     *
     * <p>
     *     This method just resets and re-initializes the code table and resets the last.
     * </p>
     */
    private void reset() {

        codeTable.clear();
        initializeCodeTable();

        last = -1;
    }

    /**
     * Check if the code table has enough space to add another mapping.
     *
     * <p>
     *     The code table has enough space if
     *     codeTable.size() + 1 is less than 2^(this.size)
     * </p>
     *
     * @return {@code true} if code table has space to add at least one more entry, {@code false} otherwise.
     */
    private boolean codeTableHasSpace() {

        return (((codeTable.size() + 1) >> size) == 0);
    }
}
