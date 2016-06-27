package ab6.impl.Eberl;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link LZWStreamingHandler} for encoding operations.
 */
public class LZWStreamingEncoder implements LZWStreamingHandler {

    /**
     * The Input Bit Stream used during encoding process.
     */
    private final LZWInBitStream inBuffer;

    /**
     * The Output Bit Stream used during encoding process.
     */
    private final LZWOutBitStream outBuffer;

    /**
     * Code table used for LZW encoding.
     */
    private final Map<String, Integer> codeTable;

    /**
     * The number of bits for one output code.
     */
    private final int size;

    /**
     * Prefix used during encoding process.
     *
     * <p>
     *     It's instance variable, for supporting the streaming case.
     * </p>
     */
    private String prefix;

    /**
     * Constructor.
     *
     * @param size The number of bits required for one data word in output.
     */
    public LZWStreamingEncoder(int size) {

        inBuffer = new LZWInBitStream(LZWStreamingHandler.BITS_PER_BYTE);
        outBuffer = new LZWOutBitStream(size);
        codeTable = new HashMap<>();
        this.size = size;
        prefix = "";

        initializeCodeTable();
    }

    @Override
    public void data(byte[] data) {

        if (data.length == 0)
            return; // nothing to encode

        inBuffer.append(data);
        encode(false);
    }

    @Override
    public byte[] finish() {

        encode(true);

        return outBuffer.toByteArray();
    }

    /**
     * Real encode method.
     *
     * <p>
     *     This method is called by {@link LZWStreamingEncoder#data} and
     *     {@link LZWStreamingEncoder#finish}, since in streaming case
     *     there might be something left in internal {@link LZWStreamingEncoder#prefix}
     *     when finish is called.
     * </p>
     *
     * @param finish When set to {@code true} flush all what's left in prefix, otherwise
     *               work in regular streaming mode.
     */
    private void encode(boolean finish) {

        // if prefix is not given, read from
        if(prefix.isEmpty() && inBuffer.hasNext())
            prefix += (char)inBuffer.getNext();

        while (inBuffer.hasNext())
        {
            String suffix = "" + (char)inBuffer.getNext();
            String data = prefix + suffix;

            if (codeTable.containsKey(data)) {
                // replace the prefix with prefix and suffix combination
                // NOTE lecture slides say to replace it with code word, but
                // this will be looked up in the else case
                prefix = data;
            } else {
                // write code word to out buffer
                outBuffer.write(codeTable.get(prefix));
                // check if there is space in code table
                if (!codeTableHasSpace()) {
                    // if not reset and go on
                    reset();
                    prefix = suffix;
                    continue;
                }

                // put the next code in
                codeTable.put(data, codeTable.size() + 1);
                prefix = suffix;
            }
        }

        if (!prefix.isEmpty() && finish) {
            // write code word to output buffer, since it should be finalized
            outBuffer.write(codeTable.get(prefix));
        }
    }

    /**
     * Initialize and pre-fill the code table with the alphabet.
     */
    private void initializeCodeTable()
    {
        for (int i = 0; i < LZWStreamingHandler.RESET_CODE_TABLE_CODE; i++) {
            codeTable.put("" + (char)i, i);
        }
    }

    /**
     * Reset encoder when there is not enough space in code table.
     *
     * <p>
     *     This method appends the {@link LZWStreamingHandler#RESET_CODE_TABLE_CODE}
     *     to the output buffer, clears and re-initializes the code table and resets
     *     the prefix.
     * </p>
     */
    private void reset() {

        outBuffer.write(LZWStreamingHandler.RESET_CODE_TABLE_CODE);

        codeTable.clear();
        initializeCodeTable();

        prefix = "";
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
