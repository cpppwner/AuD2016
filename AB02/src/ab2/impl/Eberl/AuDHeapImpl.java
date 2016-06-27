package ab2.impl.Eberl;

import ab2.AuDHeap;

public class AuDHeapImpl implements AuDHeap {

	// default heap size, when CTOR is called
	private static final int INITIAL_HEAP_SIZE = 37;

	// array storing heap data
	private int[] data = new int[INITIAL_HEAP_SIZE];

	// current number of elements in heap
	private int elementCount = 0;

	@Override
	public void createEmptyHeap(int heapSize) throws IllegalArgumentException {

		if (heapSize < 0)
			throw new IllegalArgumentException("heapSize less than zero");

		// re-create the heap and throw away all data
		data = new int[heapSize];
		elementCount = 0;
	}

	@Override
	public int elementCount() {

		// get the number of stored elements in the heap
		return elementCount;
	}

	@Override
	public int size() {

		// get the total available numbers of ints that can be stored in the heap
		// which is equal to the length of the array we allocated
		return data.length;
	}

	@Override
	public int[] toArray() {

		// return the heap elements (copy)
		// not sure, if Arrays.copyOfRange is allowed
		// therefore make the copy by hand

		int[] returnValue = new int[elementCount];
		for (int i = 0; i < elementCount; i++)
			returnValue[i] = data[i];

		return returnValue;
	}

	@Override
	public void createHeap(int[] data, int heapSize)
			throws IllegalArgumentException {

		if (data == null)
			throw new IllegalArgumentException("data is null");

		if (heapSize < data.length)
			throw new IllegalArgumentException("heapSize is less than data's length");

		// the slow way would be to build up the heap by inserting each element from data
		// into internal heap data.
		// instead let's use Robert Floyd's algorithm using the "Heapify" merge procedure

		// first allocate heap and copy data as it is
		// since I'm not sure if built-in array copy is allowed, let's do it by hand
		createEmptyHeap(heapSize);
		elementCount = data.length;
		for (int i = 0; i < data.length; i++)
			this.data[i] = data[i];

		// now use Floyd's algorithm
		for (int i = elementCount/2 - 1; i >= 0; i--)
			siftDown(i);
	}

	@Override
	public void addElement(int element) throws IllegalStateException {

		// if we reached the limits of the heap throw an exception
		if (elementCount == data.length)
			throw new IllegalStateException("Heap overflow");

		// otherwise add the element at the last position in our data array
		// and sift-up
		data[elementCount] = element;
		siftUp(elementCount);

		elementCount += 1;
	}

	@Override
	public int removeFirst() throws IllegalStateException {

		// cannot remove any element, if the heap is empty
		if (elementCount == 0)
			throw new IllegalStateException("Heap underflow");

		// if it's the last element we remove in the heap,
		// just decrement element count and return value accordingly
		if (--elementCount == 0)
			return data[elementCount];

		// swap first index with last index and also decrement element count
		swap(0, elementCount);

		// sift-down new root element of the heap to ensure heap constraints
		siftDown(0);

		return data[elementCount];
	}

	@Override
	public int remove(int idx) throws IllegalArgumentException {

		// check if index is accordingly
		if (idx < 0 || idx >= elementCount)
			throw new IllegalArgumentException("idx out of bounds");

		elementCount -= 1;

		// check if it's the very last element we wanna remove or the element at the very last position
		// in this case just return the value and do nothing, since this will not violate our heap constraints
		if (elementCount == 0 || idx == elementCount)
			return data[elementCount];

		// so, it's some element
		// swap element at given idx with last element in our heap
		swap(idx, elementCount);

		// if the swapped-in value is greater than it's parent value,
		// make sure to sift-up the new value accordingly
		if (data[idx] < data[parent(idx)]) {
			siftUp(idx);
		} else {
			// otherwise call sift-down, since this might be needed
			siftDown(idx);
		}

		// last but not least return the deleted value
		return data[elementCount];
	}

	/**
	 * Sift-up the element at given position into the correct place in the heap.
	 *
	 * @param index The index of the element in the heap to sift-up.
	 *
	 * <p>
	 *     This operation is also known as up-heap, bubble-up, percolate-up, trickle-up, heapify-up, or cascade-up.
	 * </p>
	 */
	private void siftUp(int index) {


		// traverse heap bottom up, as long as child is not root
		// and as long as our heap constraints are violated.
		// since we build a min-heap, the parent must be less than all children
		for (int child = index; child > 0 && data[child] < data[parent(child)]; child = parent(child)) {

			swap(child, parent(child));
		}
	}

	/**
	 * Sift-down the element at given position into the correct place in the heap.
	 *
	 * @param index The index of the element in the heap to sift-down.
	 *
	 * <p>
	 *     This operation is also known as down-heap, bubble-down, percolate-down, trickle-down, heapify-down, or cascade-down.
	 * </p>
	 */
	private void siftDown(int index) {

		while (!isLeaf(index)) {

			// get index of left child of given parent index
			int j = leftChild(index);

			if (( j < (elementCount - 1)) && (data[j] > data [j+1]))
				j++; // j is now index of child with smaller value

			if (data[index] <= data[j])
				return;

			// swap elements
			swap(index, j);
			// and move down
			index = j;
		}
	}

	/**
	 * Swap two values in our heap array.
	 *
	 * @param indexOne First index to swap
	 * @param indexTwo Second index to swap
     */
	private void swap(int indexOne, int indexTwo) {

		int tmp = data[indexOne];
		data[indexOne] = data[indexTwo];
		data[indexTwo] = tmp;
	}

	/**
	 * Helper method to determine the parent index of a given index in the heap.
	 */
	private static int parent(int index) {

		return (index - 1) / 2;
	}

	/**
	 * Determines whether {@code index} is a leaf node in our heap or not.
	 *
	 * @param index The index of the heap node to check.
	 *
	 * @return {@code true} if {@code index} is a leaf node, {@code false} otherwise.
     */
	private boolean isLeaf(int index) {

		return (index >= elementCount / 2) && (index < elementCount);
	}

	/**
	 * Get left child's index of given index.
	 *
	 * @param index The index for which to retrieve left child's index.
	 *
	 * @return left child's index
	 */
	private static int leftChild(int index) {

		return 2 * index + 1;
	}
}
