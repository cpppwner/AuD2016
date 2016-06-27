package ab5.impl.Eberl;

import ab5.BNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customized BTree node implementation.
 *
 * <p>
 *     This class is basically a wrapper for the BNode with some additional useful
 *     container management methods, wrapping add/remove/find stuff.
 *     It just helps to not clutter up the BTree code with container management.
 * </p>
 *
 * @param <V> The value of the BTree node.
 */
public final class BNodeImpl<V extends Comparable<V>> extends BNode<V> {

    /**
     * Minimum degree of this node.
     */
    private final int minDegree;

    /**
     * Constructor taking the minimum degree.
     * @param minDegree Minimum degree of a BTree node.
     */
    public BNodeImpl(int minDegree) {

        this.minDegree = minDegree;

        setValues(new ArrayList<>((2 * minDegree) - 1));
        setChildren(new ArrayList<>(2 * minDegree));
    }

    /**
     * Get the minimum degree of the BTree node.
     * @return Minimum degree.
     */
    public int getMinDegree() {

        return minDegree;
    }

    /**
     * Get a boolean flag indicating whether this BTree node is a leaf node or not.
     *
     * <p>
     *     A BTree node is considered to be a leaf node, if it does not have any children.
     * </p>
     *
     * @return {@code true} if it is a leaf node, {@code false} otherwise.
     */
    public boolean isLeafNode() {

        return getChildren().isEmpty();
    }

    /**
     * Get a boolean flag indicating whether this BTree node has reached the minimum allowed values.
     *
     * <p>
     *     A BTRee node has reached the minimum when the number of values
     *     stored are equal to {@link BNodeImpl#getMinDegree()} - 1.
     * </p>
     * <p>
     *     Special note: Since the root can have less than {@link BNodeImpl#getMinDegree()} - 1 keys,
     *     don't use this method on the root node.
     * </p>
     *
     * @return {@code true} if the BTree node has reached the minimum number of values, {@code false} otherwise.
     */
    public boolean hasReachedMin() {

        return numberOfValues() == (getMinDegree() - 1);
    }

    /**
     * Get a boolean flag indicating whether this BTree node has reached the maximum allowed values.
     *
     * <p>
     *     A BTRee node has reached the minimum when the number of values
     *     stored are equal to 2 * {@link BNodeImpl#getMinDegree()} - 1.
     * </p>
     *
     * @return {@code true} if the BTree node has reached the maximum number of values, {@code false} otherwise.
     */
    public boolean hasReachedMax() {

        return numberOfValues() == ((2 * getMinDegree()) - 1);
    }

    /**
     * Get the number of values stored in this BTree node.
     *
     * @return Number of values stored.
     */
    public int numberOfValues() {

        return getValues().size();
    }

    /**
     * Get a boolean flag indicating whether this node contains values or not.
     *
     * @return {@code true} if this {@link BNodeImpl} contains values, {@code false} otherwise.
     */
    public boolean hasValues() {

        return !getValues().isEmpty();
    }

    /**
     * Get the number of children stored in this BTree node.
     *
     * @return Number of children stored.
     */
    public int numberOfChildren() {

        return getChildren().size();
    }

    /**
     * Get a boolean flag indicating whether this node contains children or not.
     *
     * @return {@code true} if this {@link BNodeImpl} contains children, {@code false} otherwise.
     */
    public boolean hasChildren() {

        return !getChildren().isEmpty();
    }

    /**
     * Get the value from this node for a given index.
     *
     * @param index The index for which to get the value.
     * @return Value stored at given index.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfValues()}).
     */
    public V valueAt(int index) throws IndexOutOfBoundsException {

        return getValues().get(index);
    }

    /**
     * Set the value from this node for a given index.
     *
     * @param index The index for which to set the value.
     * @param value The new value to set.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfValues()}).
     */
    public void valueAt(int index, V value) throws IndexOutOfBoundsException {

        getValues().set(index, value);
    }

    /**
     * Add value to internal container at given index.
     *
     * @param index The index at which to add the value.
     * @param value The value to add to the underlying container.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfValues()}).
     */
    public void addValue(int index, V value) throws IndexOutOfBoundsException {

        addValues(index, Collections.singletonList(value));
    }

    /**
     * Add values to internal container at given index.
     *
     * @param index The index at which to add the value.
     * @param values The values to add to the underlying container.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfValues()}).
     */
    public void addValues(int index, List<V> values) throws IndexOutOfBoundsException {

        getValues().addAll(index, values);
    }

    /**
     * Remove value from internal container at given index.
     *
     * @param index The index from which to remove the value.
     * @return The removed value.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfValues()}).
     */
    public V removeValue(int index) throws IndexOutOfBoundsException {

        return removeValues(index, index + 1).get(0);
    }

    /**
     * Remove values from internal container starting at given startIndex (included) until given endIndex (excluded).
     *
     * <p>
     *     Removing removes all values in range [startIndex, endIndex).
     * </p>
     *
     * @param startIndex The index from where to start removing.
     * @param endIndex The end index for removal (excluded).
     * @return The removed values.
     * @throws IndexOutOfBoundsException If both indexes is not within [0, {@link BNodeImpl#numberOfValues()}].
     */
    public List<V> removeValues(int startIndex, int endIndex) throws IndexOutOfBoundsException {

        List<V> returnValues = new ArrayList<>(endIndex - startIndex);
        List<V> subList = getValues().subList(startIndex, endIndex);
        returnValues.addAll(subList);
        subList.clear();

        return returnValues;
    }

    /**
     * Get the child node at given index.
     *
     * @param index The index for which to get the child node.
     * @return The child node at given index.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfChildren()}.
     */
    public BNodeImpl<V> childAt(int index) throws IndexOutOfBoundsException {

        return (BNodeImpl<V>)getChildren().get(index);
    }

    /**
     * Add single child at given index.
     *
     * @param index The index where to add the child.
     * @param childNode The child to add.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfChildren()}.
     */
    public void addChild(int index, BNode<V> childNode) throws IndexOutOfBoundsException {

        addChildren(index, Collections.singletonList(childNode));
    }

    /**
     * Add multiple children at given position.
     *
     * @param index The index where to add the children.
     * @param children Children to add.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfChildren()}.
     */
    public void addChildren(int index, List<BNode<V>> children) throws IndexOutOfBoundsException {

        getChildren().addAll(index, children);
    }

    /**
     * Remove child from internal container at given index.
     *
     * @param index The index from which to remove the child.
     * @return The removed child.
     * @throws IndexOutOfBoundsException If index is not within [0, {@link BNodeImpl#numberOfChildren()}).
     */
    public BNode<V> removeChild(int index) throws IndexOutOfBoundsException {

        return removeChildren(index, index + 1).get(0);
    }

    /**
     * Remove children from internal container starting at given startIndex (included) until given endIndex (excluded).
     *
     * <p>
     *     Removing removes all values in range [startIndex, endIndex).
     * </p>
     *
     * @param startIndex The index from where to start removing.
     * @param endIndex The end index for removal (excluded).
     * @return The removed child nodes.
     * @throws IndexOutOfBoundsException If both indexes is not within [0, {@link BNodeImpl#numberOfValues()}].
     */
    public List<BNode<V>> removeChildren(int startIndex, int endIndex) {

        List<BNode<V>> returnValues = new ArrayList<>(endIndex - startIndex);
        List<BNode<V>> subList = getChildren().subList(startIndex, endIndex);
        returnValues.addAll(subList);
        subList.clear();

        return returnValues;
    }

    /**
     * Searches for the index in {@link BNodeImpl#getValues()} which has an equal value to given one or is the
     * smallest value that is greater than given value.
     *
     * <p>
     *     Internally a binary search algorithm is used allowing fast searches.
     * </p>
     *
     * @param value The value to search for.
     * @return Index of given value or one's complement of smallest value in values that is greater than the argument.
     */
    public int findIndexWithGreaterOrEqualValue(V value) {

        int low = 0;
        int high = numberOfValues() - 1;

        while (low <= high) {

            int median = low + ((high - low) / 2);
            int comp = valueAt(median).compareTo(value);
            if (comp == 0)
                return median;
            if (comp < 0)
                low = median + 1;
            else
                high = median - 1;
        }

        return ~low;
    }
}
