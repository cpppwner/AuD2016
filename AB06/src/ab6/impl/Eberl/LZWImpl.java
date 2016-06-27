package ab6.impl.Eberl;

import ab6.LZW;

/**
 * Implementation of LZW compression/decompression algorithm.
 */
public class LZWImpl implements LZW{

	/**
	 * Minimum code table size.
	 */
	public static final int MIN_CODETABLE_SIZE = 9;

	/**
	 * Special size specifying the size when uninitialized.
	 */
	public static final int NOT_SET_SIZE = -1;

	/**
	 * Bit size for encoding/decoding which can be set via {@link LZWImpl#setCodetableSize(int)}.
	 */
	private int size;

	/**
	 * Boolean flag indicating the mode.
	 *
	 * <p>
     *     {@code true} means "Encoder mode", {@code false} "Decoder mode" and {@code null} not yet set.
	 * </p>
	 */
	private Boolean mode;

	/**
	 * Streaming handler instance which receives the {@link LZWImpl#data(byte[])} and {@link LZWImpl#finish()} calls.
	 */
	private LZWStreamingHandler streamingHandler;

	/**
	 * Default constructor initializing the LZW encoder/decoder.
	 *
	 * <p>
	 *     This CTOR will just restore the initial state by calling the {@link LZWImpl#clear()} method.
	 * </p>
	 */
	public LZWImpl() {

		clear();
	}

	@Override
	public void setCodetableSize(int size) throws IllegalStateException, IllegalArgumentException {

		if (hasEncodingOrDecodingStarted())
			throw new IllegalStateException("encoding/decoding is ongoing and not finished");

		if (size < MIN_CODETABLE_SIZE)
			throw new IllegalArgumentException("size must be greater than or equal to " + MIN_CODETABLE_SIZE);

		this.size = size;
	}

	@Override
	public void setMode(boolean cmode) throws IllegalStateException {

		if (hasEncodingOrDecodingStarted())
			throw new IllegalStateException("encoding/decoding is ongoing and not finished");

		mode = cmode;
	}

	@Override
	public void data(byte[] data) throws IllegalStateException, IllegalArgumentException {

		if (!isInitialized())
			throw new IllegalStateException("mode or size is not set");

		if (data == null)
			throw new IllegalArgumentException("data is null");

		getStreamingHandler().data(data);
	}

	@Override
	public byte[] finish() throws IllegalStateException {

		if (!isInitialized())
			throw new IllegalStateException("mode or size not set");

		byte[] data = getStreamingHandler().finish();

		streamingHandler = null;

		return data;
	}

    /**
     * Helper method to get the {@link LZWStreamingHandler} instanace.
     *
     * <p>
     *     If no Streaming Handler has been instantiated a new one will be
     *     created and cached, otherwise the existing instance is returned.
     * </p>
     *
     * @return The {@link LZWStreamingHandler} instance.
     */
	private LZWStreamingHandler getStreamingHandler() {

		if (streamingHandler == null) {
			// first call to data or finish
			streamingHandler = mode
					? new LZWStreamingEncoder(size)
					: new LZWStreamingDecoder(size);
		}

		return streamingHandler;
	}

	@Override
	public void clear() {

		mode = null;
		streamingHandler = null;
		size = NOT_SET_SIZE;
	}

    /**
     * Helper method to check if the LZW Encoder/Decoder is initialized.
     *
     * <p>
     *     The Encoder/Decoder is initialized if {@link LZWImpl#setMode(boolean)} and
     *     {@link LZWImpl#setCodetableSize(int)} were called before.
     * </p>
     *
     * @return {@code true} if LZW Encoder/Decoder is initialized, {@code false} otherwise.
     */
	private boolean isInitialized() {

		return ((mode != null) && (size != NOT_SET_SIZE));
	}

    /**
     * Helper method to check if Encoding/Decoding has been started.
     *
     * <p>
     *     Encoding/Decoding has been started, if at least one successful call to {@link LZWImpl#data(byte[])}
     *     has been made.
     * </p>
     *
     * @return {@code true} if encoding/decoding has been started, {@code false} otherwise.
     */
	private boolean hasEncodingOrDecodingStarted() {

		return streamingHandler != null;
	}
}
