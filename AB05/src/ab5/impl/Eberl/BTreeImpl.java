package ab5.impl.Eberl;

import ab5.BNode;
import ab5.BTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the BTree interface.
 *
 * @param <V> The Value type (generic type) which is stored in the the BTree.
 */
public class BTreeImpl<V extends Comparable<V>> implements BTree<V>{

	/**
	 * The minimum degree which is allowed to be set.
	 *
	 * <p>
	 *     Setting a degree less than this results in an exception.
	 * </p>
	 */
	private static final int MINIMUM_MIN_DEGREE = 2;

	/**
	 * Root node of BTree.
	 */
	private BNodeImpl<V> root;

	/**
	 * The number of elements stored in this BTree.
	 */
	private int numElements;

	/**
	 * Default constructor.
	 *
	 * <p>
	 *     This CTOR initializes the BTree to it's initial state, in other words it just calls the clear method.
	 * </p>
	 */
	public BTreeImpl() {

		clear();
	}


	@Override
	public void setMinDegree(int t) throws IllegalStateException {

		if (t < MINIMUM_MIN_DEGREE)
			throw new IllegalArgumentException("t < " + MINIMUM_MIN_DEGREE);

		if (isTreeInitialized())
			throw new IllegalStateException("minDegree already set");

		root = new BNodeImpl<>(t);
	}

	@Override
	public boolean add(V value) throws IllegalStateException, IllegalArgumentException {

		if (value == null)
			throw new IllegalArgumentException("value is null");

		if (!isTreeInitialized())
			throw new IllegalStateException("BTree not initialized");

		if (root.hasReachedMax()) {

			// root is full, therefore create a new root node,
			// add the current root as one and only child
			// and have it split
			// NOTE: the split method will move up the value accordingly
			BNodeImpl<V> oldRoot = root;
			root = new BNodeImpl<>(root.getMinDegree());
			root.addChild(0, oldRoot);
			split(root, 0);
		}

		// last but not least add the new value starting in root node
		if (!add(root, value))
			return false;

		numElements++;
		return true;
	}

	@Override
	public boolean delete(V value) throws IllegalStateException, IllegalArgumentException {

		if (value == null)
			throw new IllegalArgumentException("value is null");

		if (!isTreeInitialized())
			throw new IllegalStateException("tree not initialized");

		boolean result = delete(root, value);

		// check if root's last entry was moved to a child node
		// in this case replace the root with the one and only child
		// NOTE: do this even if deletion was not successful
		if (!root.hasValues() && !root.isLeafNode()) {
			root = root.childAt(0);
		}

		if (result)
			numElements--;

		return result;
	}

	@Override
	public V contains(V value) throws IllegalStateException, IllegalArgumentException {

		if (value == null)
			throw new IllegalArgumentException("value is null");

		if (!isTreeInitialized())
			throw new IllegalStateException("tree not initialized");

		// search for value starting in root node
		BNodeImpl<V> currentNode = root;
		while (currentNode != null) {

			int index = currentNode.findIndexWithGreaterOrEqualValue(value);
			if (index >=0 && index < currentNode.numberOfValues())
				return currentNode.valueAt(index); // found in BTree

			currentNode = currentNode.isLeafNode()
					? null
					: currentNode.childAt(~index); // index is one's complement of next greater value
		}

		// not found
		return null;
	}

	@Override
	public BNode<V> getRoot() throws IllegalStateException {

		if (!isTreeInitialized())
			throw new IllegalStateException("tree not initialized");

		return root;
	}

	@Override
	public List<V> toList() throws IllegalStateException {

		if (!isTreeInitialized())
			throw new IllegalStateException();

		List<V> result = new ArrayList<>(numElements);
		if (numElements == 0)
			return result;

		traverse(root, result);

		return result;
	}

	/**
	 * Traverse the BTree in-order.
	 *
	 * @param node The node to traverse.
	 * @param result The resulting list where to store the elements.
     */
	private void traverse(BNodeImpl<V> node, List<V> result) {

		if (node.isLeafNode()) {
			// in case of leaf node, just add all values
			result.addAll(node.getValues());
			return;
		}

		// not a leaf node
		for (int i = 0; i < node.numberOfValues(); i++) {
			traverse(node.childAt(i), result);
			result.add(node.valueAt(i));
		}
		// traverse last child
		traverse(node.childAt(node.numberOfValues()), result);
	}

	@Override
	public void clear() {

		root = null;
		numElements = 0;
	}

	/**
	 * Helper method to check if this BTree instance is initialized.
	 *
	 * <p>
	 *     A BTree is considered to be initialized if the {@link BTreeImpl#root} is not {@code null}.
	 * </p>
	 *
	 * @return {@code true} if this BTree instance is initialized, {@code false} otherwise.
     */
	private boolean isTreeInitialized() {

		return root != null;
	}

	/**
	 * Insert a new value into the BTree, starting from root node.
	 *
	 * @param rootNode The root node of the tree where we want to insert the value.
	 * @param value The value to insert into the BTree
     * @return {@code true} if the value was inserted successfully, {@code false} if the value already exists in tree.
     */
	private static <V extends Comparable<V>> boolean add(BNodeImpl<V> rootNode, V value) {

		BNodeImpl<V> currentNode = rootNode;
		int insertPosition;

		// first search for the leaf node where to insert the value
		while (!currentNode.isLeafNode()) {

			insertPosition = currentNode.findIndexWithGreaterOrEqualValue(value);
			if (insertPosition >=0 && insertPosition < currentNode.numberOfValues())
				return false; // already in BTree

			// value not in tree - so insert position is one's complement of next greater value
			insertPosition = ~insertPosition;

			BNodeImpl<V> child = currentNode.childAt(insertPosition);

			// split child if necessary
			if (child.hasReachedMax()) {
				split(currentNode, insertPosition);

				if (currentNode.valueAt(insertPosition).compareTo(value) < 0)
					insertPosition++;

				currentNode = currentNode.childAt(insertPosition);

			} else {

				currentNode = child;
			}
		}

		insertPosition = currentNode.findIndexWithGreaterOrEqualValue(value);
		if (insertPosition >=0 && insertPosition < currentNode.numberOfValues())
			return false; // already in BTree

		// value not in tree - so insert position is one's complement of next greater value
		insertPosition = ~insertPosition;

		currentNode.addValue(insertPosition, value);

		return true;
	}

	/**
	 * Helper method to split a {@link BNodeImpl} into two nodes.
	 *
	 * @param parentNode The {@link BNodeImpl} parent node.
	 * @param childNodeIndex The child-index in from parent node.
     */
	private static <V extends Comparable<V>> void split(BNodeImpl<V> parentNode, int childNodeIndex) {

		BNodeImpl<V> nodeToSplit = parentNode.childAt(childNodeIndex);
		BNodeImpl<V> newNode = new BNodeImpl<>(nodeToSplit.getMinDegree());

		// insert the "middle value" into the parent
		int splitIndex = nodeToSplit.getMinDegree() - 1;
		parentNode.addValue(childNodeIndex, nodeToSplit.valueAt(splitIndex));
		parentNode.addChild(childNodeIndex + 1, newNode);

		// all values right of split index in the node which is split are moved to the new node
		newNode.addValues(0, nodeToSplit.removeValues(splitIndex + 1, nodeToSplit.numberOfValues()));

		// also remove the value that was moved up to the parent node
		nodeToSplit.removeValue(splitIndex);

		// if the node which was split was not a leaf node also move the children
		if (!nodeToSplit.isLeafNode()) {
			newNode.addChildren(0, nodeToSplit.removeChildren(splitIndex + 1, nodeToSplit.numberOfChildren()));
		}
	}

	/**
	 * Private delete method used to delete a value from a BTree node.
	 *
	 * <p>
	 *     The value might be found in given node, meaning it can be deleted from the given node using
	 *     {@link BTreeImpl#deleteFromNode(BNodeImpl, Comparable, int)}
	 *     -or-
	 *     if not found, traversal continues using the {@link BTreeImpl#deleteFromSubTree(BNodeImpl, Comparable, int)}
	 *     method.
	 * </p>
	 *
	 * @param node The node from where to delete.
	 * @param value The value which shall be deleted.
	 *
	 * @return {@code true} If the given value was found and therefore deleted, {@code false} otherwise.
	 */
	private static <V extends Comparable<V>> boolean delete(BNodeImpl<V> node, V value) {

		// try to find the value which shall be deleted
		int index = node.findIndexWithGreaterOrEqualValue(value);
		if (index >=0 && index < node.numberOfValues()) {
			// found the value in a node
			// regarding to lecture slides, this is now case 1 or case 2
			return deleteFromNode(node, value, index);
		}

		// value was not found - if it's not a leaf node traverse down in the sub tree
		if (!node.isLeafNode()) {

			// index is one's complement of next greater value
			index = ~index;
			return deleteFromSubTree(node, value, index);
		}

		// value was not found in the node and it must be a leaf node
		// this is now an indicator that the value we are looking for was not found at all
		return false;
	}

	/**
	 * Helper method to delete a given value from the values of a {@link BNodeImpl}.
	 *
	 * <p>
	 *     Use this method for deletion cases 1 and 2 from the lecture slides.
	 * </p>
	 *
	 * @param node The node containing the value to delete.
	 * @param value The value to delete.
	 * @param index The index in {@link BNodeImpl}'s value array at which value can be found.
	 *
     * @return {@code true} if deletion was successful, {@code false} otherwise.
     */
	private static <V extends Comparable<V>> boolean deleteFromNode(BNodeImpl<V> node, V value, int index) {

		if (node.isLeafNode()) {
			// lecture slides - case 1 (value is contained in node and the node is a leaf node)
			// -> just remove the value from the node
			node.removeValue(index);
			return true;
		}

		BNodeImpl<V> predecessorChild = node.childAt(index);
		BNodeImpl<V> successorChild = node.childAt(index + 1);

		if (!predecessorChild.hasReachedMin()) {
			// lecture slides - case 2a (predecessor child has at least #minDegree number of values
			// -> determine the value which is used as replacement from predecessor child
			BNodeImpl<V> child = predecessorChild;
			while (!child.isLeafNode()) {
				child = child.childAt(child.numberOfChildren() - 1);
			}

			// remember the value of the immediate predecessor of the value which will be deleted
			V replacementValue = child.valueAt(child.numberOfValues() - 1);

			// overwrite the value to delete with the found replacement
			node.valueAt(index, replacementValue);

			// delete the replacement value in the subtree
			return delete(predecessorChild, replacementValue);
		}

		if (!successorChild.hasReachedMin()) {
			// lecture slides - case 2b (successor child has at least #minDegree number of values
			// -> determine the value which is used as replacement from predecessor child and remove
			// regarding to lecture slides this is done recursively, but should work without.
			BNodeImpl<V> child = successorChild;
			while (!child.isLeafNode()) {
				child = child.childAt(0);
			}

			// remember the value of the immediate successor of the value which will be deleted
			V replacementValue = child.valueAt(0);

			// overwrite the value to delete with the found replacement
			node.valueAt(index, replacementValue);

			// delete the replacement value in the subtree
			return delete(successorChild, replacementValue);
		}

		// lecture slides - case 2c
		// predecessor child and successor child both reached minimum (= minDegree - 1)
		// so merge value to delete and successor values (+ children) into predecessor child
		predecessorChild.addValue(predecessorChild.numberOfValues(), node.removeValue(index));
		predecessorChild.addValues(predecessorChild.numberOfValues(), successorChild.removeValues(0, successorChild.numberOfValues()));
		predecessorChild.addChildren(predecessorChild.numberOfChildren(), successorChild.removeChildren(0, successorChild.numberOfChildren()));
		node.removeChild(index + 1); // remove successor child, since it's merged already

		// and finally recursively delete the value from new node
		return delete(predecessorChild, value);
	}

	/**
	 * Helper method to delete a given value from a subtree of given {@link BNodeImpl}.
	 *
	 * <p>
	 *     Use this method for deletion cases 3 from the lecture slides.
	 * </p>
	 *
	 * @param node The node containing the value to delete.
	 * @param value The value to delete.
	 * @param index The index in {@link BNodeImpl}'s value array at which value can be found.
	 *              In this method the index is also used to determine the sub tree where value could reside in.
	 *
	 * @return {@code true} if deletion was successful, {@code false} otherwise.
	 */
	private static <V extends Comparable<V>> boolean deleteFromSubTree(BNodeImpl<V> node, V value, int index) {

		// get the child node where value should be in (just use the provided index)
		BNodeImpl<V> childNode = node.childAt(index);

		if (childNode.hasReachedMin()) {

			// so the child node is down to #minDegree - 1
			// get left and right sibling and try to execute case 3a or 3b
			BNodeImpl<V> childLeftSibling = index > 0
					? node.childAt(index - 1)
					: null;
			BNodeImpl<V> childRightSibling = index < node.numberOfChildren() - 1
					? node.childAt(index + 1)
					: null;

			if (childLeftSibling != null && !childLeftSibling.hasReachedMin()) {

				// lecture slides - case 3a
				// child node has reached minimum, but it has a left sibling with enough entries
				// -> move one entry from parent to this child node
				// -> move one entry from left sibling to parent node
				childNode.addValue(0, node.valueAt(index - 1)); // NOTE: index is the index of the next greater element
				node.valueAt(index - 1, childLeftSibling.removeValue(childLeftSibling.numberOfValues() - 1));

				if (!childNode.isLeafNode()) {
					// also move last child from left sibling to first position in child nodes
					childNode.addChild(0, childLeftSibling.removeChild(childLeftSibling.numberOfChildren() - 1));
				}

			} else if (childRightSibling != null && !childRightSibling.hasReachedMin()) {

				// lecture slides - case 3a
				// child node has reached minimum, but it has a right sibling with enough entries
				// -> move one entry from parent to this child node
				// -> move one entry from right sibling to parent node
				childNode.addValue(childNode.numberOfValues(), node.valueAt(index)); // NOTE: index is the index of the next greater element
				node.valueAt(index, childRightSibling.removeValue(0));

				if (!childNode.isLeafNode()) {
					// also move first child from right sibling to last position in child nodes
					childNode.addChild(childNode.numberOfChildren(), childRightSibling.removeChild(0));
				}

			} else {

				// lecture slides - case 3b
				// child node has reached minimum, but all (1 or 2) siblings are also down to minimum
				// -> merge child node and either left or right sibling
				if (childLeftSibling != null) {

					// left sibling exists, so merge child node with left sibling
					childNode.addValue(0, node.removeValue(index - 1));
					childNode.addValues(0, childLeftSibling.removeValues(0, childLeftSibling.numberOfValues()));

					if (!childNode.isLeafNode()) {
						// it it's not a leaf node move all children from left sibling
						childNode.addChildren(0, childLeftSibling.removeChildren(0, childLeftSibling.numberOfChildren()));
					}

					node.removeChild(index - 1); // left sibling can be removed, since it's merged into child


				} else if (childRightSibling != null) {

					// right sibling exists, so merge child node with right sibling
					childNode.addValue(childNode.numberOfValues(), node.removeValue(index));
					childNode.addValues(childNode.numberOfValues(), childRightSibling.removeValues(0, childRightSibling.numberOfValues()));

					if (!childNode.isLeafNode()) {
						// it it's not a leaf node move all children from right sibling
						childNode.addChildren(childNode.numberOfChildren(), childRightSibling.removeChildren(0, childRightSibling.numberOfChildren()));
					}

					node.removeChild(index + 1); // right sibling can be removed, since it's merged into child
				} else {
					// neither left nor right sibling exists - our tree is broken
					throw new IllegalStateException("BTree broken - neither left nor right child exists");
				}
			}
		}

		// last but not least continue recursive deletion in child node
		return delete(childNode, value);
	}
}
