package ab2.impl.Eberl;

import ab2.SearchAlgos;

public class SearchAlgosImpl implements SearchAlgos {

	public static final int NUMBER_NOT_FOUND_INDEX = -1;

	/**
	 * Counters to count number of value comparisons - not comparisons in total
	 */
	public long binarySearchValueCompCounter = 0;
	public long interpolationSearchValueCompCounter = 0;

	@Override
	public int BinarySearch(int[] data, int x) throws IllegalArgumentException {

		if (data == null)
			throw new IllegalArgumentException("data is null");

		int low = 0;
		int high = data.length - 1;
		int middle, value;

		int index = NUMBER_NOT_FOUND_INDEX;

		// search for x in [low, high].
		// NOTE: The algorithm was specified recursively in the slides,
		// but this implementation uses an iterative approach
		while (low <= high) {

			// first determine the middle
			middle = (low + high + 1) / 2;
			value = data[middle];

			// check if we already hit the key that we are looking for
			binarySearchValueCompCounter++;
			if (value == x) {
				// found it
				index = middle;
				break;
			}

			// if x is less than our middle, search on the left hand side
			binarySearchValueCompCounter++;
			if (x < value) {
				high = middle - 1;
			} else {
				// since x is neither equal to value in the middle nor less than
				// the only thing it can be is greater than -> search on right hand side
				low = middle + 1;
			}
		}

        return index;
	}

	@Override
	public int InterpolationSearch(int[] data, int x)
			throws IllegalArgumentException {

		if (data == null)
			throw new IllegalArgumentException("data is null");

		if (data.length == 0)
			return NUMBER_NOT_FOUND_INDEX;

		int low = 0;
		int high = data.length - 1;
		int middle, value;

		// note this hack with the method is only done to be able to make exact countings
		// otherwise i'd prefer to "inline" this method directly into the loop-condition
		while (stayInLoop(data, x, low, high)) {

			// determine the middle value as specified in the slides
			// NOTE: The casts are only performed to avoid special underflow conditions
			// To see what is meant, check InterpolationSearchSpec.searchingAlsoWorksForSpecialArithmeticUnderflowCondition
			// test
			middle = (int) (low + ((((long)x - data[low])  * (high - low)) / ((long)data[high] - data[low])));
			value = data[middle];

			// check if we already hit the key that we are looking for
			interpolationSearchValueCompCounter++;
			if (value == x) {
				// found it
				return middle;
			}

			// if x is less than our middle, search on the left hand side
			interpolationSearchValueCompCounter++;
			if (x < value) {
				high = middle - 1;
			} else {
				// since x is neither equal to value in the middle nor less than
				// the only thing it can be is greater than -> search on right hand side
				low = middle + 1;
			}
		}

		// some differences than binary search, due to loop-conditions
		interpolationSearchValueCompCounter++;
		return x == data[low] ? low : NUMBER_NOT_FOUND_INDEX;
	}

	private boolean stayInLoop(int[] data, int x, int low, int high) {

		// don't count this comparison
		if(data[high] == data[low])
			return false;

		interpolationSearchValueCompCounter++;
		if (x < data[low])
			return false;

		interpolationSearchValueCompCounter++;
		return x <= data[high];
	}
}
