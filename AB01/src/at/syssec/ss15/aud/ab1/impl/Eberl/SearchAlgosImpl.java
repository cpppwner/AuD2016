package at.syssec.ss15.aud.ab1.impl.Eberl;

import at.syssec.ss15.aud.ab1.SearchAlgos;

import java.util.Stack;

public class SearchAlgosImpl implements SearchAlgos {

	private static final int QUICKSORT_THRESHOLD = 5;

	@Override
	public void BubbleSort(int[] data) {

		// bullsh*t in (null) -> stop immediately
		// furthermore an array containing at most one element is sorted by definition -> stop immediately
		if (data == null || data.length <= 1)
			return;

		// some optimized version of bubble sort
		// remembers where the last swap occurred, so in this case we need to re-run
		// the inner loop up to (excluding) this last position
		// this means for already sorted arrays, this algorithm runs in O(n), since only comparison are performed.
		int n = data.length;
		do {
			int newn = 1;
			for (int i = 0; i < n - 1; i++){
				if (data[i] > data[i + 1]){
					swap(data, i, i + 1);
					newn = i + 1;
				}
			}
			n = newn;
		} while (n > 1);
	}

	@Override
	public void QuickSort1(int[] data) {

		QuickSort(data, (d, low, high) -> high);
	}

	@Override
	public void QuickSort3(int[] data) {

		QuickSort(data, SearchAlgosImpl::MedianOfThree);
	}

	@Override
	public void QuickSort5(int[] data) {

		QuickSort(data, SearchAlgosImpl::MedianOfFive);
	}

	/**
	 * An iterative  quick sort algorithm.
	 * @param data The array to sort.
	 * @param pivotFunction A function taking three args and returning the index of the pivot element.
	 *
	 * <p>
	 *     The original implementation of the quick sort would be recursive, but to avoid stack overflows
	 *     my implementation is using an iterative approach.
	 * </p>
     */
	private static void QuickSort(int[] data, TriFunction<int[], Integer, Integer, Integer> pivotFunction) {

		// bullsh*t in (null) -> stop immediately
		// furthermore an array containing at most one element is sorted by definition -> stop immediately
		if (data == null || data.length <= 1)
			return;

		// so we know we have at least two elements in
		// now push the initial high low position onto the stack
		Stack<QuickSortPosTuple> stack = new Stack<>();
		stack.push(new QuickSortPosTuple(0, data.length - 1));

		while (!stack.isEmpty()) {

			// get the low-high indexes from the stack again
			QuickSortPosTuple pos = stack.pop();
			int low = pos.low;
			int high = pos.high;

			// get the index of the pivot element
			int pivotIndex = pivotFunction.apply(data, low, high);

			// do partitioning phase and get the new pivot index
			pivotIndex = Partition(data, low, high, pivotIndex);

			// instead of recursively calling quick sort
			// just add the sub array indexes to the stack (if needed)
			if (pivotIndex - 1 > low) {
				// have sub array on the left hand side to sort
				stack.push(new QuickSortPosTuple(low, pivotIndex - 1));
			}
			if (pivotIndex + 1 < high) {
				// have sub array on the right hand side to sort
				stack.push(new QuickSortPosTuple(pivotIndex + 1, high));
			}
		}
	}

	/**
	 * Calcualte median of three, swap also values accordingly.
     */
	private static int MedianOfThree(int[] data, int low, int high) {

		int n = (high - low + 1);
		int half = n / 2;

		// the thing i'll try to accomplish is the following c++ comparison
		//max(min(low,mid), min(max(low,mid),high));
		int minLowMid = data[low] < data[half] ? low : half;
		int maxLowMid = minLowMid == low ? half : low;
		int minMaxLowMidHigh = data[maxLowMid] < data[high] ? maxLowMid : high;

		return data[minLowMid] > data[minMaxLowMidHigh] ? minLowMid : minMaxLowMidHigh;
	}

	/**
	 * Calculate median of five, swap also values accordingly.
	 *
	 * <p>
	 *     Special note: If the data to consider is too small, it falls
	 *     back to median of three, since otherwise wrong data might be calculated.
	 * </p>
	 */
	private static int MedianOfFive(int[] data, int low, int high) {

		int n = (high - low + 1);
		int a = low, b = n / 4, c = n / 2, d = (3 * n) / 4, e = high;

		// sort a and b
		if(data[a] > data[b]) // comparison # 1
		{
			int tmp = a;
			a = b;
			b = tmp;
		}

		// sort c and d
		if(data[c] > data[d])  // comparison # 2
		{
			int tmp = c;
			c = d;
			d = tmp;
		}

		// replace the lower of a and c with e
		// because the lowest of the first four cannot be the median
		if(data[a] < data[c]) // comparison # 3
		{
			a = e;
			// re-sort a and b
			if(data[a] > data[b]) // comparison # 4
			{
				int tmp = a;
				a = b;
				b = tmp;
			}
		}
		else
		{
			c = e;
			// re-sort c and d
			if(data[c] > data[d])  // comparison # 4
			{
				int tmp = c;
				c = d;
				d = tmp;
			}
		}

		// eliminate a or c, because the lowest
		// of the remaining four can't be the median either
		if(data[a] < data[c]) // comparison #5
		{
			return data[b] < data[c] ? c : b;
		}

		return data[d] < data[a] ? a : d;
	}


	/**
	 * Median of 3 partitioning schema for quicksort algorithm.
	 *
	 * <p>
	 *     This schema takes the last element as the pivot element.
	 * </p>
	 *
	 * @param data The input array.
	 * @param low Index of lowest element to consider for partitioning.
	 * @param high Index of highest element to consider for partitioning.
	 * @return Partition index.
     */
	private static int Partition(int[] data,
								 int low,
								 int high,
								 int pivotIndex) {

		// first step ... get the pivot element out of sight and swap it with the highest element
		if (pivotIndex != high) {
			swap(data, pivotIndex, high);
		}

		// get the pivot value, required for comparison later
		int pivot = data[high];

		// partitioning phase
		int lowPtr = low - 1; // first element
		int highPtr = high ;  // index of pivot

		while(lowPtr < highPtr)
		{
			while(data[++lowPtr] < pivot);  // find values that are bigger than pivot on the left hand side
			while(data[--highPtr] >= pivot && highPtr > low); // find values that are smaller than pivot on right hand side

			// low and high position did not cross, so we have to swap them
			if (lowPtr < highPtr)
				swap(data, lowPtr, highPtr);
		}

		// finally put the pivot into the correct place
		swap(data, lowPtr, high);

		// and return the index of the pivot element
		return lowPtr;
	}

	/**
	 * Helper method to swap two array elements.
	 * @param array The input array where to swap two elements
	 * @param indexOne First index
	 * @param indexTwo Second index
     */
	private static void swap(int[] array, int indexOne, int indexTwo)
	{
		int tmp = array[indexTwo];
		array[indexTwo] = array[indexOne];
		array[indexOne] = tmp;
	}

	/**
	 * Some functional utility interface for having functions with 3 args (required for partitioning).
	 *
	 * <p>
	 *     C#/.NET has such things by default using Func :)
	 * </p>
	 *
	 * @param <A> First argument type
	 * @param <B> Second argument type
	 * @param <C> Third argument type
     * @param <R> Result type
     */
	@FunctionalInterface
	interface TriFunction<A,B,C,R> {

		R apply(A a, B b, C c);
	}

	/**
	 * Some high/low position helper class for the quick sort
	 */
	private static final class QuickSortPosTuple
	{
		public final int low;
		public final int high;

		public QuickSortPosTuple(int low, int high) {
			this.low = low;
			this.high = high;
		}
	}

}
