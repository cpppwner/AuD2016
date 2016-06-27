package ab4.impl.Eberl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ab4.Node;
import ab4.Tree;

/**
 * Implementieren Sie diese Klasse, wenn Sie einen GenericTree implementieren
 * wollen. Verwenden Sie hierfür die compareTo-Methode des Values (dieser ist
 * vom Typ Comparable und besetzt diese Methode).
 * 
 * Möchten Sie keinen GenericTree implementieren, so löschen Sie diese Klasse
 * und implementieren Sie die Klasse CharacterTreeImpl
 */

/**
 * Implementation of the {@link Tree} interface.
 *
 * @param <V> The generic Value parameter to insert into the tree.
 */
public class TreeImpl<V extends Comparable<V>> implements Tree<V> {

	/**
	 * Root node of the tree.
	 */
	private Node<V> rootNode = null;

	/**
	 * Number elements currently stored in the tree.
	 *
	 * <p>
	 *     It would be possible to calculate them on the fly by traversing the whole tree, but
	 *     storing the count and returning this value is much faster (+ more error prone ;)).
	 * </p>
	 */
	private int size = 0;

	@Override
	public int getHeight() {

		return rootNode == null ? -1 : rootNode.getHeightOfSubtree();
	}

	@Override
	public int size() {

		return size;
	}

	@Override
	public boolean addValue(V value) throws IllegalArgumentException {

		// it's not allowed to add null values
		if (value == null)
			throw new IllegalArgumentException("value is null");

		// no root node yet - create it
		if (rootNode == null) {
			rootNode = new Node<>(value);
			rootNode.setHeightOfSubtree(0);
			size = 1;

			return true;
		}

		// already got a root node - insert value
		Node<V> parent = insertValue(value);
		if (parent == null) {
			// same value already exists in tree
			return false;
		}

		rebalanceSubTree(parent);
		size += 1;

		return true;
	}

    /**
     * Insert given value into tree and returns the parent node of the newly created child node.
     *
     * <p>
     *     This method requires having a root node (not intended for completely empty tree)
     *     and the new node is just inserted, without re-balancing the tree.
     * </p>
     *
     * @param value The value to insert into the tree.
     * @return The parent node of the newly created node or {@code null} if same value already exists in tree.
     */
	private Node<V> insertValue(V value) {

		Node<V> currentNode = rootNode;
		while (currentNode != null) {

			int comp = currentNode.getValue().compareTo(value);
			if (comp == 0) {

				// duplicate entry - no duplicates allowed
				return null;
			}

			// either continue on the left hand side or right hand side, depending on
			// whether comp is less than or greater than zero
			if (comp > 0) {
				if (currentNode.getLeft() != null) {
					currentNode = currentNode.getLeft();
				} else {
					// found the place to insert, it's on the left of current node
					currentNode.setLeft(new Node<>(value));
					currentNode.getLeft().setHeightOfSubtree(0); // not needed, but just to make everything clear
					break;
				}

			} else {
				if (currentNode.getRight() != null) {
					currentNode = currentNode.getRight();
				} else {
					// found the place to insert, it's on the right of current node
					Node<V> newNode = new Node<>(value);
					newNode.setHeightOfSubtree(0); // not needed, but just to make everything clear
					currentNode.setRight(newNode);
					currentNode = newNode;
					break; // current's node on right is null - that's the place where we gonna insert the new node
				}
			}
		}

		return currentNode;
	}

	/**
	 * Traverse the tree up starting from given node and restores the AVL balance if required.
	 *
	 * @param currentNode The node from where to start re-balancing.
     */
	private void rebalanceSubTree(Node<V> currentNode) {

		while(currentNode != null) {

			// adjust the height of the current node
			setHeight(currentNode);

			int heightDifference = currentNode.getHeightDiff();

			// get parent node
			// NOTE: this is required due to some issue in the Node implementation
			// inserting left/right nodes will update the left/right node's parent accordingly
			// but the old parent still has a reference to it's child, and will set the old child's  parent to null
			// when doing "re-parenting"
			Node<V> parent = currentNode.getParent();

			// remove the left subtree from parent - avoid any updating issues
			if (parent != null) {
				if (parent.getLeft() == currentNode) // yes, reference equality check is intended
					parent.setLeft(null);
				else
					parent.setRight(null);
			}

			if (heightDifference <= -2) {

				// unbalanced left tree -> re-balance
                if (currentNode.getLeft().getHeightDiff() <= 0) {
					currentNode = singleClockwiseRotation(currentNode);
				} else {
					currentNode = doubleClockwiseRotation(currentNode);
				}

			} else if (heightDifference >= 2) {

				// unbalanced right tree -> re-balance
                if (currentNode.getRight().getHeightDiff() >= 0) {
					currentNode = singleCounterClockwiseRotation(currentNode);
				} else {
					currentNode = doubleCounterClockwiseRotation(currentNode);
				}
			}

			// re-attach new node to parent, if parent is null change the tree's root node
			if (parent != null) {
				if (parent.getValue().compareTo(currentNode.getValue()) > 0)
					parent.setLeft(currentNode);
				else
					parent.setRight(currentNode);
			} else {
				this.rootNode = currentNode;
			}

			// traverse one node up in the tree
			currentNode = parent;
		}
	}

    /**
     * Perform a double clockwise rotation (rotate right).
     *
     * <p>
     *     Actually the name might be slightly misleading, since the first rotation is
     *     counter clockwise and the second one is clockwise.
     * </p>
     *
     * @param currentNode The current node to rotate
     * @param <V> Value
     * @return Returns the new node which replaces the input parameter after rotation.
     */
	private static <V extends Comparable<V>> Node<V> doubleClockwiseRotation(Node<V> currentNode) {

		// get and detach left node from current node
		Node<V> leftNode = currentNode.getLeft();
		currentNode.setLeft(null);

		currentNode.setLeft(singleCounterClockwiseRotation(leftNode));
		return singleClockwiseRotation(currentNode);
	}

    /**
     * Perform a double counter clockwise rotation (rotate left).
     *
     * <p>
     *     Actually the name might be slightly misleading, since the first rotation is
     *     clockwise and the second one is counter clockwise.
     * </p>
     *
     * @param currentNode The current node to rotate
     * @param <V> Value
     * @return Returns the new node which replaces the input parameter after rotation.
     */
	private static <V extends Comparable<V>> Node<V> doubleCounterClockwiseRotation(Node<V> currentNode) {

		// get and detach right node from current node
		Node<V> rightNode = currentNode.getRight();
		currentNode.setRight(null);

		currentNode.setRight(singleClockwiseRotation(rightNode));
		return singleCounterClockwiseRotation(currentNode);
	}

    /**
     * Perform a single clockwise rotation (rotate right).
     *
     * @param currentNode The current node to rotate
     * @param <V> Value
     * @return Returns the new node which replaces the input parameter after rotation.
     */
	private static <V extends Comparable<V>> Node<V> singleClockwiseRotation(Node<V> currentNode) {

		// switch nodes

		// first get the references
		Node<V> tmpLeft = currentNode.getLeft();
		Node<V> tmpRight = tmpLeft.getRight();

		// detach nodes we got previously - just to keep the state correct
		currentNode.setLeft(null);
		tmpLeft.setRight(null);

		// re-attach nodes
		currentNode.setLeft(tmpRight);
		tmpLeft.setRight(currentNode);

		// re-calculate heights of new sub-trees
		setHeight(currentNode);
		setHeight(tmpLeft);

		return tmpLeft;
	}

    /**
     * Perform a single counter clockwise rotation (rotate left).
     *
     * @param currentNode The current node to rotate
     * @param <V> Value
     * @return Returns the new node which replaces the input parameter after rotation.
     */
	private static <V extends Comparable<V>> Node<V> singleCounterClockwiseRotation(Node<V> currentNode) {

		// switch nodes

		// first get the references
		Node<V> tmpRight = currentNode.getRight();
		Node<V> tmpLeft = tmpRight.getLeft();

		// detach nodes we got previously - just to keep the state correct
		currentNode.setRight(null);
		tmpRight.setLeft(null);

		// re-attach nodes
		currentNode.setRight(tmpLeft);
		tmpRight.setLeft(currentNode);

		// re-calculate heights of new sub-trees
		setHeight(currentNode);
		setHeight(tmpRight);

		return tmpRight;
	}

    /**
     * Calculates and adjusts the heigh of the given node.
     *
     * @param currentNode The tree's node for which to adjust the height of the tree.
     * @param <V> The generic value.
     */
	private static <V extends Comparable<V>> void setHeight(Node<V> currentNode) {

		int height = Math.max(currentNode.getLeft() != null ? currentNode.getLeft().getHeightOfSubtree() : -1,
				currentNode.getRight() != null ? currentNode.getRight().getHeightOfSubtree() : -1);

		currentNode.setHeightOfSubtree(height + 1);
	}

	@Override
	public boolean containsValue(V value) throws IllegalArgumentException {

		return findNode(value) != null;
	}

	@Override
	public boolean removeValue(V value) throws IllegalArgumentException {

		// first search for the node to delete
		Node<V> nodeToDelete = findNode(value);
		if (nodeToDelete == null) {
			return false;
		}

		Node<V> parentNode;

		if ((nodeToDelete.getLeft() == null && nodeToDelete.getRight() == null)
			|| (nodeToDelete.getLeft() != null && nodeToDelete.getRight() == null)
			|| (nodeToDelete.getLeft() == null && nodeToDelete.getRight() != null)) {

			// trivial case - the node which will be deleted has no or exactly one child (left or right)
            parentNode = removeNodeWithMaxOneChild(nodeToDelete);

        } else {

			// not so trivial case - delete a node with two children

            // remember the child nodes of the node which will be deleted, since they are detached
            Node<V> left = nodeToDelete.getLeft();
            Node<V> right = nodeToDelete.getRight();
            nodeToDelete.setLeft(null);
            nodeToDelete.setRight(null);

            // first get the node with largest value from left subtree
            Node<V> biggestNodeOnLhs = left;
            while (biggestNodeOnLhs.getRight() != null) {
                biggestNodeOnLhs = biggestNodeOnLhs.getRight();
            }

            // if node with biggest value is left itself (it does not have a right child)
            // then it's a trivial case
            if (biggestNodeOnLhs == left) { // reference equality is intended
                biggestNodeOnLhs.setRight(right);
                parentNode = biggestNodeOnLhs;

            } else {
                // biggest node on lhs becomes the replacement for the node we are going to delete
                // but remove it from the tree and re-parent potentially left child
                parentNode = removeNodeWithMaxOneChild(biggestNodeOnLhs);
                biggestNodeOnLhs.setRight(right);
                biggestNodeOnLhs.setLeft(left);
            }

            // re-attach new node to parent of node which get's deleted, if parent is null change the tree's root node
            if (nodeToDelete.getParent() != null) {
                if (nodeToDelete.getParent().getLeft() == nodeToDelete) {
                    nodeToDelete.getParent().setLeft(biggestNodeOnLhs);
                } else {
                    nodeToDelete.getParent().setRight(biggestNodeOnLhs);
                }

            } else {
                rootNode = biggestNodeOnLhs;
            }
		}

        // last but not least during deletion the tree might have lost the AVL property -> re-balance
        rebalanceSubTree(parentNode);

		size -= 1;

		return true;
	}

    /**
     * Remove a given node from the tree which might have one child, but not more.
     *
     * <p>
     *     This method will not perform any re-balancing of the tree.
     * </p>
     *
     * @param nodeToDelete The node which shall be deleted from the tree.
     * @return The parent's node of the node which was deleted.
     */
    private Node<V> removeNodeWithMaxOneChild(Node<V> nodeToDelete) {

        Node<V> parentNode = nodeToDelete.getParent();

        // remember and detach the single child node (if any) from the node to delete
        Node<V> child = null;
        if (nodeToDelete.getLeft() != null) {
            child = nodeToDelete.getLeft();
            nodeToDelete.setLeft(null);
        } else if (nodeToDelete.getRight() != null){
            child = nodeToDelete.getRight();
            nodeToDelete.setRight(null);
        }

        // re-parent the child node (if any) to the parent of the node we want to delete
        if (parentNode == null) {
            // deleting the last remaining node or a root node with only one child
            rootNode = child;
        } else {
            if (parentNode.getValue().compareTo(nodeToDelete.getValue()) > 0) {
                // detach left child
                parentNode.setLeft(child);
            } else {
                parentNode.setRight(child);
            }
        }

        return parentNode;
    }

    /**
	 * Find the node in the tree that holds the given value.
	 *
	 * @param value The value to search for in the tree.
	 * @return The found {@link Node} or {code null} if {@link Node} with given value does not exist in tree.
	 *
     */
	private Node<V> findNode(V value) throws IllegalArgumentException {

		if (value == null)
			throw new IllegalArgumentException("value is null");

		Node<V> currentNode = rootNode;
		while (currentNode != null) {

			int comp = currentNode.getValue().compareTo(value);
			if (comp == 0) {
				// found it
				break;

			} else if (comp > 0) {
				// traverse on the left side of the tree
				currentNode = currentNode.getLeft();
			} else {
				// traverse on the right side of the tree
				currentNode = currentNode.getRight();
			}
		}

		return currentNode;
	}

	@Override
	public void clear() {

		rootNode = null;
		size = 0;
	}

	@Override
	public List<V> toRWL() {

        // the easiest solution would be to call toLWR method and use Collections.reverse
        // but since I'm not sure if this is allowed, do a similar traverse like in toWLR
        List<V> result = new ArrayList<>(size());

        Node<V> currentNode = getRoot();
        Stack<Node<V>> stack = new Stack<>();

        while (!stack.isEmpty() || currentNode != null) {
            if (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.getRight();
            } else {
                currentNode = stack.pop();
                result.add(currentNode.getValue());
                currentNode = currentNode.getLeft();
            }
        }

        return result;
	}

	@Override
	public List<V> toLWR() {

        List<V> result = new ArrayList<>(size());

        Node<V> currentNode = getRoot();
        Stack<Node<V>> stack = new Stack<>();

        while (!stack.isEmpty() || currentNode != null) {
            if (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.getLeft();
            } else {
                currentNode = stack.pop();
                result.add(currentNode.getValue());
                currentNode = currentNode.getRight();
            }
        }

        return result;
	}

	@Override
	public List<V> toLRW() {

        List<V> result = new ArrayList<>(size());

        Node<V> currentNode = getRoot();
        Node<V> lastVisitedNode = null;

        Stack<Node<V>> stack = new Stack<>();

        while (!stack.isEmpty() || currentNode != null) {

            if (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.getLeft();
            } else {
                Node<V> tmp = stack.peek();
                if (tmp.getRight() != null && lastVisitedNode != tmp.getRight()) {
                    currentNode = tmp.getRight();
                } else {
                    result.add(tmp.getValue());
                    lastVisitedNode = stack.pop();
                }
            }
        }

        return result;
	}

	@Override
	public List<V> toRLW() {
        List<V> result = new ArrayList<>(size());

        Node<V> currentNode = getRoot();
        Node<V> lastVisitedNode = null;

        Stack<Node<V>> stack = new Stack<>();

        while (!stack.isEmpty() || currentNode != null) {

            if (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.getRight();
            } else {
                Node<V> tmp = stack.peek();
                if (tmp.getLeft() != null && lastVisitedNode != tmp.getLeft()) {
                    currentNode = tmp.getLeft();
                } else {
                    result.add(tmp.getValue());
                    lastVisitedNode = stack.pop();
                }
            }
        }

        return result;
    }

	@Override
	public List<V> toWLR() {

		List<V> result = new ArrayList<>(size());
		if (rootNode == null) {
			// don't have a root, return immediately
			return result;
		}

		Stack<Node<V>> nodeStack = new Stack<>();
		nodeStack.push(rootNode);

		while (!nodeStack.isEmpty()) {

			Node<V> currentNode = nodeStack.pop();

			if (currentNode.getRight() != null) {
				nodeStack.push(currentNode.getRight());
			}

			if (currentNode.getLeft() != null) {
				nodeStack.push(currentNode.getLeft());
			}

			result.add(currentNode.getValue());
		}

		return result;
	}

	@Override
	public List<V> toWRL() {

		List<V> result = new ArrayList<>(size());
		if (rootNode == null) {
			// don't have a root, return immediately
			return result;
		}

		Stack<Node<V>> nodeStack = new Stack<>();
		nodeStack.push(rootNode);

		while (!nodeStack.isEmpty()) {

			Node<V> currentNode = nodeStack.pop();

			if (currentNode.getLeft() != null) {
				nodeStack.push(currentNode.getLeft());
			}

			if (currentNode.getRight() != null) {
				nodeStack.push(currentNode.getRight());
			}

			result.add(currentNode.getValue());
		}

		return result;
	}

	@Override
	public Node<V> getRoot() {

		return rootNode;
	}
}