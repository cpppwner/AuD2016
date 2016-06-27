package ab3.impl.Eberl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ab3.AuDHashMap;
import ab3.Helper;

public class AuDHashMapImpl<K,V> implements AuDHashMap<K, V> {

	private static final int SMALLEST_PRIME_NUMBER = 2;
	private static final int DEFAULT_HASH_MAP_SIZE = 37;

	private int elementCount;
	private Object[] data;

	/**
	 * Default constructor.
	 *
	 * <p>
	 *     This CTOR creates an empty hash map with space for 37 entries as stated in the interface.
	 * </p>
	 */
	public AuDHashMapImpl() {

		elementCount = 0;
		data = new Object[DEFAULT_HASH_MAP_SIZE];
	}

	@Override
	public void setMapSize(int mapSize) {

		int capacity = Helper.getPrimeGreaterOrEqualThan(Math.max(mapSize, SMALLEST_PRIME_NUMBER));

		data = new Object[capacity];
		elementCount = 0;
	}

	@Override
	public void clear() {

		for (int i = 0; i < data.length; i++)
			data[i] = null;
		elementCount = 0;
	}

	@Override
	public boolean containsKey(K key) {

		int keyHashCode = key.hashCode();

		for (int j = 0; j < size(); j++) {

			int index = doubleHash(j, keyHashCode);
			@SuppressWarnings("unchecked")
			Map.Entry<K, V> entry = ((Map.Entry<K, V>)data[index]);

			if (entry == null) {
				// not found
				return false;
			} else if (entry.getKey().equals(key)) {
				// found
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsValue(V value) {

		// since there is no mapping from value to key, do a sequential search
		return stream()
				.filter(entry -> entry != null)
				.anyMatch(entry -> entry.getValue().equals(value));
	}

	@Override
	public V get(K key) {

		int keyHashCode = key.hashCode();

		for (int j = 0; j < size(); j++) {

			int index = doubleHash(j, keyHashCode);
			@SuppressWarnings("unchecked")
			Map.Entry<K, V> entry = ((Map.Entry<K, V>)data[index]);

			if (entry == null) {
				// not found
				return null;
			} else if (entry.getKey().equals(key)) {
				// found
				return entry.getValue();
			}
		}

		return null;
	}

	@Override
	public boolean isEmpty() {

		return elementCount() == 0;
	}

	@Override
	public Set<K> keySet() {

		return stream()
				.filter(entry -> entry != null)
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	@Override
	public V put(K key, V value) {

		int keyHashCode = key.hashCode();

		for (int j = 0; j < size(); j++) {

			int index = doubleHash(j, keyHashCode);
			@SuppressWarnings("unchecked")
			Map.Entry<K, V> entry = ((Map.Entry<K, V>)data[index]);

			if (entry == null) {
				data[index] = new AuDHashMapEntry(key, value);
				elementCount++;
				return null;
			} else if (entry.getKey().equals(key)) {
				// can overwrite
				return entry.setValue(value);
			}
		}

		throw new IllegalStateException("map is full");
	}

	@Override
	public int elementCount() {

		return elementCount;
	}

	@Override
	public int size() {

		return data.length;
	}

	@Override
	public Collection<V> values() {

		return stream()
				.filter(entry -> entry != null)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isFull() {

		return elementCount() == size();
	}

	/**
	 * Helper method for getting back a {@link Stream} of {@link java.util.Map.Entry}.
     */
	@SuppressWarnings("unchecked")
	private Stream<Map.Entry<K, V>> stream() {

		return StreamSupport.stream(Arrays.spliterator(data), false).map(entry -> (Map.Entry<K, V>)entry);
	}

	private int doubleHash(int j, int key) {

		int m = size();

		int primaryHash = key % m;
		int secondaryHash = (1 + (key % (m - 2)));

		int tmp = ((primaryHash - j * secondaryHash) % m);

		return tmp < 0 ? tmp + m : tmp;
	}

	/**
	 * Implementation of Map.Entry class.
	 *
	 * <p>
	 *     This class is used as container object for the AuD Hash Map.
	 *     Since it's not passed to the outside world, it's implemented as private class.
	 * </p>
	 */
	private final class AuDHashMapEntry implements Map.Entry<K, V> {

		/**
		 * Key of this entry.
		 *
		 * <p>
		 *     Since the key cannot change, make it immutable.
		 * </p>
		 */
		private final K key;

		/**
		 * Value of this entry.
		 */
		private V value;

		/**
		 * Constructor of {@link AuDHashMapEntry}
		 * @param key The key for this entry.
		 * @param value The value for this entry.
		 */
		private AuDHashMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
	}
}
