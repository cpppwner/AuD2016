package ab4.test;

import ab4.Node;
import ab4.Tree;
import ab4.impl.Eberl.TreeImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class TreeImplSpec {

    @Test
    public void aDefaultConstructedTreeHasAHeightOfMinusOne() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        Assert.assertEquals(-1, tree.getHeight());
    }

    @Test
    public void gettingTheRootNodeOfADefaultConstructedTreeGivesNull() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        Assert.assertNull(tree.getRoot());
    }

    @Test
    public void aDefaultConstructedTreeHasSizeZero() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        Assert.assertEquals(0, tree.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingNullValueToTreeThrowsIllegalArgumentException() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        tree.addValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removingNullValueFromTreeThrowsIllegalArgumentException() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        tree.removeValue(null);
    }

    @Test
    public void addingValueToEmptyTreeAddsARootNode() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        boolean obtained = tree.addValue(4);

        // then
        Assert.assertTrue(obtained);
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());
    }

    @Test
    public void addingValueToEmptyTreeSetsNumberOfElementsAccordingly() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        tree.addValue(4);

        // then
        Assert.assertEquals(1, tree.size());
    }

    @Test
    public void addingValueToEmptyTreeGetseightAccordingly() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        tree.addValue(4);

        // then
        Assert.assertEquals(0, tree.getHeight());
    }

    @Test
    public void buildingUpATreeWithMultipleValuesBuildsUpTheTreeCorrectly() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes in such an order that it's always an AVL tree
        boolean obtained = tree.addValue(4);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(6);
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates - must be true

        Assert.assertEquals(7, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getRight().getRight().getValue());
    }

    @Test
    public void addingAValueThatAlreadyExistsInTreeGivesFalse() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(6);

        // then
        Assert.assertTrue(obtained); // no duplicates - must be true

        // and then
        Assert.assertFalse(tree.addValue(4));
        Assert.assertFalse(tree.addValue(2));
        Assert.assertFalse(tree.addValue(6));
    }

    @Test
    public void checkIfAValueIsContainedInTreeGivesTrueIfTheValueIsContained() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates - must be true

        // and when checking if all the values which were added are contained
        Assert.assertTrue(tree.containsValue(1));
        Assert.assertTrue(tree.containsValue(2));
        Assert.assertTrue(tree.containsValue(3));
        Assert.assertTrue(tree.containsValue(4));
        Assert.assertTrue(tree.containsValue(5));
        Assert.assertTrue(tree.containsValue(6));
        Assert.assertTrue(tree.containsValue(7));
    }

    @Test
    public void checkIfAValueIsContainedInTreeGivesFalseIfTheValueIsNotContained() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates - must be true

        // and when checking if all the values which were added are contained
        Assert.assertFalse(tree.containsValue(-1));
        Assert.assertFalse(tree.containsValue(0));
        Assert.assertFalse(tree.containsValue(8));
        Assert.assertFalse(tree.containsValue(9));
        Assert.assertFalse(tree.containsValue(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkingIfANullValueIsContainedInTreeThrowsIllegalArgumentException() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        tree.containsValue(null);
    }

    @Test
    public void clearingATreeResetsEverythingToInitialValues() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node

        // then
        Assert.assertTrue(obtained); // no duplicates - must be true
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotEquals(-1, tree.getHeight());
        Assert.assertNotEquals(0, tree.size());

        // and when
        tree.clear();

        // then
        Assert.assertNull(tree.getRoot());
        Assert.assertEquals(-1, tree.getHeight());
        Assert.assertEquals(0, tree.size());
    }

    @Test
    public void addingValueToTreeCanHandleLeftLeftCase() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when - building up a tree like this
        //       5
        //      /
        //     4
        //    /
        //   3
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(4);
        obtained &= tree.addValue(3);

        // then
        Assert.assertTrue(obtained);

        // The tree gets re-balanced (simple rotate clockwise) to the following
        //       4
        //      / \
        //     3   5
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getRight().getValue());
    }

    @Test
    public void addingValueToTreeCanHandleRightRightCase() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when - building up a tree like this
        //       4
        //        \
        //         5
        //          \
        //           6
        boolean obtained = tree.addValue(4);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(6);

        // then
        Assert.assertTrue(obtained);

        // The tree gets re-balanced (simple rotate counter clockwise) to the following
        //       5
        //      / \
        //     4   6
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getRight().getValue());
    }

    @Test
    public void addingValueToTreeCanHandleLeftRightCase() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when - building up a tree like this
        //       3
        //      /
        //     1
        //      \
        //       2
        boolean obtained = tree.addValue(3);
        obtained &= tree.addValue(1);
        obtained &= tree.addValue(2);

        // then
        Assert.assertTrue(obtained);

        // The tree gets re-balanced (double clockwise rotate) to the following
        //       2
        //      / \
        //     1   3
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getRight().getValue());
    }

    @Test
    public void addingValueToTreeCanHandleRightLeftCase() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when - building up a tree like this
        //       7
        //        \
        //         9
        //         /
        //        8
        boolean obtained = tree.addValue(7);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(8);

        // then
        Assert.assertTrue(obtained);

        // The tree gets re-balanced (double rotate counter clockwise) to the following
        //       8
        //      / \
        //     7   9
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getValue());
    }

    @Test
    public void addingAscendingValuesKeepsAvlConstraints() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        boolean obtained = true;
        for (int i = 1; i < 16; i++)
            obtained &= tree.addValue(i);

        // then - the following tree is expected
        //
        //                 8
        //             /       \
        //            4        12
        //          /   \     /   \
        //         2     6   10   14
        //        / \   / \  / \  / \
        //       1   3 5   7 9 11 13 15
        //
        Assert.assertTrue(obtained);

        // tree & root node
        Assert.assertEquals(15, tree.size());
        Assert.assertEquals(3, tree.getHeight());
        Assert.assertNotNull(tree.getRoot());

        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getLeft().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getLeft().getValue());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void addingDescendingValuesKeepsAvlConstraints() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        boolean obtained = true;
        for (int i = 15; i >= 1; i--)
            obtained &= tree.addValue(i);

        // then - the following tree is expected
        //
        //                 8
        //             /       \
        //            4        12
        //          /   \     /   \
        //         2     6   10   14
        //        / \   / \  / \  / \
        //       1   3 5   7 9 11 13 15
        //
        Assert.assertTrue(obtained);

        // tree & root node
        Assert.assertEquals(15, tree.size());
        Assert.assertEquals(3, tree.getHeight());
        Assert.assertNotNull(tree.getRoot());

        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getLeft().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getLeft().getValue());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void addingValuesHandlesComplexDoubleRotateLeft() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(8);
        obtained &= tree.addValue(6);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(7);

        // then the following tree is expected
        //
        //         6
        //        / \
        //       5   8
        //      /   / \
        //     3   7   9
        Assert.assertTrue(obtained);

        Assert.assertEquals(2, tree.getHeight());
        Assert.assertEquals(6, tree.size());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getValue());

        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getValue());

        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getLeft().getValue());

        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(1, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getRight().getValue());

        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void addingValuesHandlesComplexDoubleRotateRight() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        boolean obtained = tree.addValue(8);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(6);
        obtained &= tree.addValue(7);

        // then the following tree is expected
        //
        //         6
        //        / \
        //       5   8
        //      /   / \
        //     2   7   9
        Assert.assertTrue(obtained);

        Assert.assertEquals(2, tree.getHeight());
        Assert.assertEquals(6, tree.size());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getValue());

        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getValue());

        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getLeft().getValue());

        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(1, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getRight().getValue());

        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void singleRotateRightOnRightChild() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the tree as follows
        //
        //         5
        //        / \
        //       2   9
        //          /
        //         8
        //        /
        //       7
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(8);
        obtained &= tree.addValue(7);

        // then the following tree is expected
        //
        //         5
        //        / \
        //       2   8
        //          / \
        //         7   9
        Assert.assertTrue(obtained);

        Assert.assertEquals(2, tree.getHeight());
        Assert.assertEquals(5, tree.size());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getValue());

        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());

        Assert.assertNull(tree.getRoot().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight());

        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getRight().getValue());
        Assert.assertEquals(1, tree.getRoot().getRight().getHeightOfSubtree());

        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getHeightOfSubtree());

        Assert.assertNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getLeft().getRight());

        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getHeightOfSubtree());

        Assert.assertNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getRight().getRight());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void singleRotateLeftOnRightChild() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the tree as follows
        //
        //         5
        //        / \
        //       2   9
        //       \
        //        3
        //         \
        //          4
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(4);

        // then the following tree is expected
        //
        //         5
        //        / \
        //       3   9
        //      / \
        //     2   4
        Assert.assertTrue(obtained);

        Assert.assertEquals(2, tree.getHeight());
        Assert.assertEquals(5, tree.size());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getValue());

        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(0, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getValue());

        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getRight());

        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(1, tree.getRoot().getLeft().getHeightOfSubtree());

        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());

        Assert.assertNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getRight());

        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());

        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingAValueFromADefaultConstructedTreeReturnsFalse() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        boolean obtained = tree.removeValue(5);

        // then
        Assert.assertFalse(obtained);
        Assert.assertEquals(0, tree.size());
    }

    @Test
    public void deletingAValueThatDoesNotExistInTreeGivesFalse() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates
        Assert.assertEquals(7, tree.size());

        // and when
        obtained = tree.removeValue(8);

        // then
        Assert.assertFalse(obtained);
        Assert.assertEquals(7, tree.size());

        // and when
        obtained = tree.removeValue(0);

        // then
        Assert.assertFalse(obtained);
        Assert.assertEquals(7, tree.size());
    }

    @Test
    public void deletingTheLastNodeFromATreeBehavesCorrectly() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        Assert.assertTrue(tree.addValue(1));
        Assert.assertNotNull(tree.getRoot());

        // when
        boolean obtained = tree.removeValue(1);

        // then
        Assert.assertTrue(obtained);
        Assert.assertEquals(0, tree.size());
        Assert.assertEquals(-1, tree.getHeight());
        Assert.assertNull(tree.getRoot());
    }

    @Test
    public void deletingAValueWithNoSuccessorsWorksAsExpected() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // then
        Assert.assertTrue(tree.addValue(2));
        Assert.assertTrue(tree.addValue(1));
        Assert.assertTrue(tree.addValue(3));
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());

        // when
        boolean obtained = tree.removeValue(1);

        // then
        Assert.assertTrue(obtained);
        Assert.assertEquals(2, tree.size());
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getLeft());

        // and when
        obtained = tree.removeValue(3);

        // then
        Assert.assertTrue(obtained);
        Assert.assertEquals(1, tree.size());
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getLeft());

        // and when
        obtained = tree.removeValue(2);

        // then
        Assert.assertTrue(obtained);
        Assert.assertEquals(0, tree.size());
        Assert.assertNull(tree.getRoot());
    }

    @Test
    public void deletingANodeWithNoChildrenAndNecessarySingleLeftRotationAfterDeletion() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         5
        //        / \
        //       2   7
        //            \
        //             9
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(9);
        Assert.assertTrue(obtained);
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when deleting the value 2
        obtained = tree.removeValue(2);

        // then the following tree is expected
        //
        //         7
        //        / \
        //       5   9
        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getRight().getRight());
        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft());

        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingANodeWithNoChildrenAndNecessarySingleRightRotationAfterDeletion() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         5
        //        / \
        //       4   7
        //      /
        //     3
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(4);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(3);
        Assert.assertTrue(obtained);
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when deleting the value 7
        obtained = tree.removeValue(7);

        // then the following tree is expected
        //
        //         4
        //        / \
        //       3   5
        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getRight().getRight());
        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft());

        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingANodeWithNoChildrenAndNecessaryDoubleLeftRotationAfterDeletion() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         5
        //        / \
        //       2   7
        //          /
        //         6
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(6);
        Assert.assertTrue(obtained);
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when deleting the value 2
        obtained = tree.removeValue(2);

        // then the following tree is expected
        //
        //         6
        //        / \
        //       5   7
        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getRight().getRight());
        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft());

        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingANodeWithNoChildrenAndNecessaryDoubleRightRotationAfterDeletion() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         6
        //        / \
        //       4   7
        //        \
        //         5
        boolean obtained = tree.addValue(6);
        obtained &= tree.addValue(4);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(5);
        Assert.assertTrue(obtained);
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when deleting the value 7
        obtained = tree.removeValue(7);

        // then the following tree is expected
        //
        //         5
        //        / \
        //       4   6
        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getRight().getRight());
        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft());

        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingAValueWithASingleLeftChildWorks() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when the following tree is constructed
        //
        //               4
        //              / \
        //             3   6
        //            /
        //           2
        Assert.assertTrue(tree.addValue(4));
        Assert.assertTrue(tree.addValue(3));
        Assert.assertTrue(tree.addValue(6));
        Assert.assertTrue(tree.addValue(2));
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when 3 is deleted
        boolean obtained = tree.removeValue(3);

        // then the following tree is expected
        //
        //               4
        //              / \
        //             2   6
        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingAValueWithASingleRightChildWorks() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when the following tree is constructed
        //
        //               4
        //              / \
        //             2   6
        //             \
        //              3
        Assert.assertTrue(tree.addValue(4));
        Assert.assertTrue(tree.addValue(2));
        Assert.assertTrue(tree.addValue(6));
        Assert.assertTrue(tree.addValue(3));
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when 3 is deleted
        boolean obtained = tree.removeValue(2);

        // then the following tree is expected
        //
        //               4
        //              / \
        //             3   6
        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingRootNodeWithASingleLeftChildWorks() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when the following tree is constructed
        //
        //               4
        //              /
        //             3
        Assert.assertTrue(tree.addValue(4));
        Assert.assertTrue(tree.addValue(3));
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        // when 4 is deleted
        boolean obtained = tree.removeValue(4);

        // then the following tree is expected
        Assert.assertTrue(obtained);
        Assert.assertEquals(1, tree.size());
        Assert.assertEquals(0, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNull(tree.getRoot().getParent());
        Assert.assertNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getLeft());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getValue());
    }

    @Test
    public void deletingRootNodeWithASingleRightChildWorks() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when the following tree is constructed
        //
        //               4
        //                \
        //                 6
        Assert.assertTrue(tree.addValue(4));
        Assert.assertTrue(tree.addValue(6));
        Assert.assertNotNull(tree.getRoot());
        Assert.assertNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        // when 4 is deleted
        boolean obtained = tree.removeValue(4);

        // then the following tree is expected
        Assert.assertTrue(obtained);
        Assert.assertEquals(1, tree.size());
        Assert.assertEquals(0, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNull(tree.getRoot().getParent());
        Assert.assertNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getLeft());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getValue());
    }

    @Test
    public void deletingRootNodeWithTwoChildrenWorks() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         5
        //        / \
        //       2   8
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(8);

        Assert.assertTrue(obtained);
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        // when
        obtained = tree.removeValue(5);

        // then the following tree is expected
        //         2
        //          \
        //           8
        Assert.assertTrue(obtained);
        Assert.assertEquals(2, tree.size());
        Assert.assertEquals(1, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getLeft());

        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getRight().getValue());
    }

    @Test
    public void deletingARightNodeWithTwoChildrenWhenLeftChildIsLargestInSubTree() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         5
        //        / \
        //       2   8
        //          / \
        //         7   9
        boolean obtained = tree.addValue(5);
        obtained &= tree.addValue(2);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(8);
        obtained &= tree.addValue(7);
        Assert.assertTrue(obtained);
        Assert.assertEquals(5, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when deleting the value 8
        obtained = tree.removeValue(8);

        // then the following tree is expected
        //
        //         5
        //        / \
        //       2   7
        //            \
        //             9
        Assert.assertTrue(obtained);
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void deletingALeftNodeWithTwoChildrenWhenLeftChildIsLargestInSubTree() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when building up the following tree
        //
        //         8
        //        / \
        //       5   9
        //      / \
        //     4   6
        boolean obtained = tree.addValue(8);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(9);
        obtained &= tree.addValue(4);
        obtained &= tree.addValue(6);
        Assert.assertTrue(obtained);
        Assert.assertEquals(5, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        // when deleting the value 5
        obtained = tree.removeValue(5);

        // then the following tree is expected
        //
        //         8
        //        / \
        //       4   9
        //        \
        //         6
        Assert.assertTrue(obtained);
        Assert.assertEquals(4, tree.size());
        Assert.assertEquals(2, tree.getHeight());

        Assert.assertNotNull(tree.getRoot());
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertNull(tree.getRoot().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getRight());
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getValue());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    public void subsequentDeletionOfRootNode() {

        // given
        Tree<Integer> tree = new TreeImpl<>();
        for (int i = 1; i < 16; i++)
            Assert.assertTrue(tree.addValue(i));

        // then check the root node's value
        Assert.assertEquals(Integer.valueOf(8), tree.getRoot().getValue());
        Assert.assertEquals(15, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // when deleting the root node's value
        boolean obtained = tree.removeValue(8);

        // then check the tree and full constraints
        //
        //                 7
        //             /       \
        //            4        12
        //          /   \     /   \
        //         2     6   10   14
        //        / \   /    / \  / \
        //       1   3 5     9 11 13 15
        Assert.assertTrue(obtained);
        Assert.assertEquals(14, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(7), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getLeft().getValue());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());

        // and when deleting the root node's value again
        obtained = tree.removeValue(7);

        // then check the tree and full constraints
        //
        //                 6
        //             /       \
        //            4        12
        //          /   \     /   \
        //         2     5   10   14
        //        / \        / \  / \
        //       1   3      9 11 13 15
        Assert.assertTrue(obtained);
        Assert.assertEquals(13, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(6), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getLeft().getValue());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());

        // and when deleting the root node's value again
        obtained = tree.removeValue(6);

        // then check the tree and full constraints
        //
        //                 5
        //             /       \
        //            2        12
        //          /   \     /   \
        //         1     4   10   14
        //              /    / \  / \
        //             3    9 11 13 15
        Assert.assertTrue(obtained);
        Assert.assertEquals(12, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(5), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getRight().getLeft().getValue());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());

        // and when deleting the root node's value again
        obtained = tree.removeValue(5);

        // then check the tree and full constraints
        //
        //                 4
        //             /       \
        //            2        12
        //          /   \     /   \
        //         1     3   10   14
        //                   / \  / \
        //                  9 11 13 15
        Assert.assertTrue(obtained);
        Assert.assertEquals(11, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(4), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());

        // and when deleting the root node's value again
        obtained = tree.removeValue(4);

        // then check the tree and full constraints
        //
        //              3
        //             / \
        //            2   12
        //          /    /   \
        //         1    10   14
        //              / \  / \
        //             9  11 13 15
        Assert.assertTrue(obtained);
        Assert.assertEquals(10, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(3), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(1, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getLeft());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(2, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getRight().getLeft().getRight().getValue());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getRight().getLeft().getLeft().getValue());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());

        // and when deleting the root node's value again
        obtained = tree.removeValue(3);

        // then check the tree and full constraints
        //
        //              12
        //            /   \
        //           2     14
        //          / \   /   \
        //         1  10 13   15
        //            / \
        //           9  11
        Assert.assertTrue(obtained);
        Assert.assertEquals(9, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(12), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getLeft().getRight().getLeft().getValue());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getLeft().getRight().getRight().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight().getRight());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(1, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getValue());
        Assert.assertNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getRight().getRight());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());

        // and when deleting the root node's value again
        obtained = tree.removeValue(12);

        // then check the tree and full constraints
        //
        //              11
        //            /   \
        //           2     14
        //          / \   /   \
        //         1  10 13   15
        //            /
        //           9
        Assert.assertTrue(obtained);
        Assert.assertEquals(8, tree.size());
        Assert.assertEquals(3, tree.getHeight());

        // root
        Assert.assertNotNull(tree.getRoot());
        Assert.assertEquals(Integer.valueOf(11), tree.getRoot().getValue());

        // left sub tree
        Assert.assertNotNull(tree.getRoot().getLeft());
        Assert.assertEquals(2, tree.getRoot().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(2), tree.getRoot().getLeft().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getLeft());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(1), tree.getRoot().getLeft().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getLeft().getRight());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(10), tree.getRoot().getLeft().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getLeft().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getRight());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(9), tree.getRoot().getLeft().getRight().getLeft().getValue());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getLeft().getRight().getLeft().getRight());

        // right sub tree
        Assert.assertNotNull(tree.getRoot().getRight());
        Assert.assertEquals(1, tree.getRoot().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(14), tree.getRoot().getRight().getValue());
        Assert.assertNotNull(tree.getRoot().getRight().getLeft());
        Assert.assertNotNull(tree.getRoot().getRight().getRight());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getHeightOfSubtree());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getHeightOfSubtree());
        Assert.assertEquals(Integer.valueOf(13), tree.getRoot().getRight().getLeft().getValue());
        Assert.assertEquals(Integer.valueOf(15), tree.getRoot().getRight().getRight().getValue());
        Assert.assertNull(tree.getRoot().getRight().getLeft().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getLeft().getRight());
        Assert.assertNull(tree.getRoot().getRight().getRight().getLeft());
        Assert.assertNull(tree.getRoot().getRight().getRight().getRight());

        // then also check references
        Assert.assertNull(tree.getRoot().getParent());
        checkReferences(tree.getRoot());
    }

    @Test
    @Ignore("Long running integration test")
    public void randomizedAddDeleteTest() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when generating lots of random numbers to insert
        Set<Integer> valueSet = new HashSet<>();
        int size = 0;

        Random random = new Random();
        for (int i = 0; i < 10000000; i++) {

            Assert.assertEquals(size, tree.size());

            int nextNr = random.nextInt();
            if (valueSet.contains(nextNr)) {
                Assert.assertFalse(tree.addValue(nextNr));
            } else {
                Assert.assertTrue(tree.addValue(nextNr));
                valueSet.add(nextNr);
                Assert.assertEquals(++size, tree.size());
            }

            Assert.assertTrue(isAVLTree(tree.getRoot()));
            Assert.assertNull(tree.getRoot().getParent());
            checkReferences(tree.getRoot());
        }

        // and when generating lot's of random numbers to delete
        Object[] valuesInTree = valueSet.toArray();
        Arrays.sort(valuesInTree);
        for (int i = 0; i < 2000000; i++) {
            int nextNr = (i % 2 == 0) ? (int)valuesInTree[i / 2] : random.nextInt();
            if (!valueSet.contains(nextNr)) {
                Assert.assertFalse(tree.removeValue(nextNr));
            } else {
                Assert.assertTrue(tree.removeValue(nextNr));
                valueSet.remove(nextNr);
                Assert.assertEquals(--size, tree.size());
            }

            Assert.assertTrue(isAVLTree(tree.getRoot()));
            Assert.assertNull(tree.getRoot().getParent());
            checkReferences(tree.getRoot());
        }
    }

    @Test
    public void aDefaultConstructedTreeGivesEmptyWLRList() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        List<Integer> obtained = tree.toWLR();

        // then
        Assert.assertNotNull(obtained);
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void givenWLRIsAppropriate() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedWLR = tree.toWLR();

        // then
        Assert.assertNotNull(obtainedWLR);
        Assert.assertArrayEquals(new Integer[]{4, 2, 1, 3, 6, 5, 7}, obtainedWLR.toArray());
    }

    @Test
    public void givenWLRIsAppropriateIfTreeIsIncomplete() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //                 \ /
        //                 3 5
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedWLR = tree.toWLR();

        // then
        Assert.assertNotNull(obtainedWLR);
        Assert.assertArrayEquals(new Integer[]{4, 2, 3, 6, 5}, obtainedWLR.toArray());
    }

    @Test
    public void aDefaultConstructedTreeGivesEmptyWRLList() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        List<Integer> obtained = tree.toWRL();

        // then
        Assert.assertNotNull(obtained);
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void givenWRLIsAppropriate() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedWRL = tree.toWRL();

        // then
        Assert.assertNotNull(obtainedWRL);
        Assert.assertArrayEquals(new Integer[]{4, 6, 7, 5, 2, 3, 1}, obtainedWRL.toArray());
    }

    @Test
    public void givenWRLIsAppropriateIfTreeIsIncomplete() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //                 \ /
        //                 3 5
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedWRL = tree.toWRL();

        // then
        Assert.assertNotNull(obtainedWRL);
        Assert.assertArrayEquals(new Integer[]{4, 6, 5, 2, 3}, obtainedWRL.toArray());
    }

    @Test
    public void aDefaultConstructedTreeGivesEmptyLWRList() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        List<Integer> obtained = tree.toLWR();

        // then
        Assert.assertNotNull(obtained);
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void givenLWRIsAppropriate() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedLWR = tree.toLWR();

        // then
        Assert.assertNotNull(obtainedLWR);
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7}, obtainedLWR.toArray());
    }

    @Test
    public void givenLWRIsAppropriateIfTreeIsIncomplete() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //                 \ /
        //                 3 5
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedLWR = tree.toLWR();

        // then
        Assert.assertNotNull(obtainedLWR);
        Assert.assertArrayEquals(new Integer[]{2, 3, 4, 5, 6}, obtainedLWR.toArray());
    }

    @Test
    public void aDefaultConstructedTreeGivesEmptyRWLList() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        List<Integer> obtained = tree.toRWL();

        // then
        Assert.assertNotNull(obtained);
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void givenRWLIsAppropriate() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedRWL = tree.toRWL();

        // then
        Assert.assertNotNull(obtainedRWL);
        Assert.assertArrayEquals(new Integer[]{7, 6, 5, 4, 3, 2, 1}, obtainedRWL.toArray());
    }

    @Test
    public void givenRWLIsAppropriateIfTreeIsIncomplete() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //                 \ /
        //                 3 5
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedRWL = tree.toRWL();

        // then
        Assert.assertNotNull(obtainedRWL);
        Assert.assertArrayEquals(new Integer[]{6, 5, 4, 3, 2}, obtainedRWL.toArray());
    }

    @Test
    public void aDefaultConstructedTreeGivesEmptyLRWList() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        List<Integer> obtained = tree.toLRW();

        // then
        Assert.assertNotNull(obtained);
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void givenLRWIsAppropriate() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedLRW = tree.toLRW();

        // then
        Assert.assertNotNull(obtainedLRW);
        Assert.assertArrayEquals(new Integer[]{1, 3, 2, 5, 7, 6, 4}, obtainedLRW.toArray());
    }

    @Test
    public void givenLRWIsAppropriateIfTreeIsIncomplete() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //                 \ /
        //                 3 5
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedLRW = tree.toLRW();

        // then
        Assert.assertNotNull(obtainedLRW);
        Assert.assertArrayEquals(new Integer[]{3, 2, 5, 6, 4}, obtainedLRW.toArray());
    }

    @Test
    public void aDefaultConstructedTreeGivesEmptyRLWList() {

        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when
        List<Integer> obtained = tree.toRLW();

        // then
        Assert.assertNotNull(obtained);
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void givenRLWIsAppropriate() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //               / \ / \
        //              1  3 5  7
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);
        obtained &= tree.addValue(7);
        obtained &= tree.addValue(1);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedRLW = tree.toRLW();

        // then
        Assert.assertNotNull(obtainedRLW);
        Assert.assertArrayEquals(new Integer[]{7, 5, 6, 3, 1, 2, 4}, obtainedRLW.toArray());
    }

    @Test
    public void givenRLWIsAppropriateIfTreeIsIncomplete() {
        // given
        Tree<Integer> tree = new TreeImpl<>();

        // when adding some tree nodes and building up a tree like
        //
        //                  4
        //                 / \
        //                2   6
        //                 \ /
        //                 3 5
        boolean obtained = tree.addValue(4); // root node
        obtained &= tree.addValue(2); // left child of root node
        obtained &= tree.addValue(6); // right child of root node
        obtained &= tree.addValue(3);
        obtained &= tree.addValue(5);

        // then
        Assert.assertTrue(obtained); // no duplicates

        // when
        List<Integer> obtainedRLW = tree.toRLW();

        // then
        Assert.assertNotNull(obtainedRLW);
        Assert.assertArrayEquals(new Integer[]{5, 6, 3, 2, 4}, obtainedRLW.toArray());
    }


    /**
     * "Borrowed" from TreeTest.
     * @param node The node for which to check if it's an AVL tree.
     * @return {@code true} if given node is an AVL-(sub)tree, {@code false otherwise}.
     */
    private static boolean isAVLTree(Node<?> node) {
        if (node == null)
            return true;

        int leftH = getHeightOfSubtree(node.getLeft());
        int rightH = getHeightOfSubtree(node.getRight());

        if (Math.abs(leftH - rightH) > 1)
            return false;

        return isAVLTree(node.getLeft()) && isAVLTree(node.getRight());
    }

    /**
     * Calculate the height of the sub tree. (Borrowed also from TreeTest).
     * @param node The node for which to calculate the height of the sub tree.
     * @return Height of sub tree.
     */
    private static int getHeightOfSubtree(Node<?> node) {
        if (node == null)
            return -1;

        int leftH = getHeightOfSubtree(node.getLeft());
        int rightH = getHeightOfSubtree(node.getRight());

        return 1 + Math.max(leftH, rightH);
    }

    /**
     * Check if all parent-child references are correct.
     * @param node The node for which to check references.
     */
    private static void checkReferences(Node<?> node) {

        if (node.getLeft() != null) {
            Assert.assertSame(node, node.getLeft().getParent());
            checkReferences(node.getLeft());
        }
        if (node.getRight() != null) {
            Assert.assertSame(node, node.getRight().getParent());
            checkReferences(node.getRight());
        }
    }
}
