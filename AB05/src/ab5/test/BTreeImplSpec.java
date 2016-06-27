package ab5.test;

import ab5.BNode;
import ab5.BTree;
import ab5.impl.Eberl.BNodeImpl;
import ab5.impl.Eberl.BTreeImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class BTreeImplSpec {

    @Test(expected = IllegalArgumentException.class)
    public void setMinDegreeThrowsAnExceptionIfDegreeIsLessThanTwo() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // then
        tree.setMinDegree(1);
    }

    @Test(expected = IllegalStateException.class)
    public void callingSetMinDegreeTwiceThrowsAnException() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // when
        try {
            tree.setMinDegree(2);
        } catch(Exception e) {
            Assert.fail(e.getMessage());
        }

        // then
        tree.setMinDegree(2);
    }

    @Test
    public void setMinDegreeAfterCallIsLegal() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // when
        try {
            tree.setMinDegree(2);
            tree.clear();
        } catch(Exception e) {
            Assert.fail(e.getMessage());
        }

        tree.setMinDegree(3);
    }

    @Test(expected = IllegalStateException.class)
    public void getRootThrowsExceptionIfSetMinDegreeWasNotCalled() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // then
        tree.getRoot();
    }

    @Test
    public void getRootReturnsRootNode() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        // when
        BNode<Integer> root = tree.getRoot();

        // then
        Assert.assertNotNull(root);
        Assert.assertNotNull(root.getValues());
        Assert.assertNotNull(root.getChildren());
        Assert.assertTrue(root.getValues().isEmpty());
        Assert.assertTrue(root.getChildren().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void addingValueToBTreeWithoutSettingDegreeThrowsException() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // then
        tree.add(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingNullValueToBTreeThrowsException() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        // then
        tree.add(null);
    }

    @Test
    public void addValueAddsToRootNodeAsLongAsSplitConditionIsNotReached() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(10);
        Integer[] values = new Integer[19];

        // when
        boolean success = true;
        for (int i = 1; i < 20; i++) {
            success &= tree.add(i);
            values[i - 1] = i;
        }

        // then
        Assert.assertTrue(success);
        Assert.assertNotNull(tree.getRoot());
        Assert.assertArrayEquals(values, tree.getRoot().getValues().toArray());
        Assert.assertNotNull(tree.getRoot().getChildren());
        Assert.assertArrayEquals(new Object[0], tree.getRoot().getChildren().toArray());
    }

    @Test
    public void addValueAddsWorksWithSplittingTheRootNode() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(3);
        Integer[] values = new Integer[5];

        // when
        boolean success = true;
        for (int i = 1; i < 6; i++) {
            success &= tree.add(i);
            values[i - 1] = i;
        }

        // then
        Assert.assertTrue(success);
        Assert.assertNotNull(tree.getRoot());
        Assert.assertArrayEquals(values, tree.getRoot().getValues().toArray());
        Assert.assertNotNull(tree.getRoot().getChildren());
        Assert.assertArrayEquals(new Object[0], tree.getRoot().getChildren().toArray());

        // and when adding one more value
        success = tree.add(6);

        // then
        Assert.assertTrue(success);
        Assert.assertNotNull(tree.getRoot());
        Assert.assertArrayEquals(new Integer[]{3}, tree.getRoot().getValues().toArray());

        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        Assert.assertArrayEquals(new Integer[]{1, 2}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertArrayEquals(new Integer[]{4, 5, 6}, tree.getRoot().getChildren().get(1).getValues().toArray());
    }

    @Test
    public void addValuesFailsWhenAddingSameValuesAgain() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(4);

        // when
        boolean success = true;
        for (int i = 1; i <= 100; i++) {
            success &= tree.add(i);
        }

        // then inserting all values succeeded first time
        Assert.assertTrue(success);

        // but fails on second time
        for (int i = 1; i <= 100; i++) {
            Assert.assertFalse(tree.add(i));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void containsThrowsExceptionIfSetMinDegreeWasNotCalled() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // then
        tree.contains(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsThrowsExceptionValueToSearchForIsNull() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        // then
        tree.contains(null);
    }

    @Test
    public void containsReturnsNullIfValuesAreNotFound() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        // then
        for (int i = -10; i <= 10; i++) {
            Assert.assertNull(tree.contains(i));
        }

        // and when adding some values
        boolean success = true;
        for (int i = -10; i <= 10; i++) {
            success &= tree.add(i);
        }

        // then
        Assert.assertTrue(success);

        for (int i = -20; i < -10; i++) {
            Assert.assertNull(tree.contains(i));
            Assert.assertNull(tree.contains(i * -1));
        }
    }

    @Test
    public void containsReturnsTheValuesIfValuesAreFound() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        // and when adding some values
        boolean success = true;
        for (int i = -100; i <= 100; i++) {
            success &= tree.add(i);
        }

        // then
        Assert.assertTrue(success);
        for (int i = -100; i <= 100; i++) {
            Assert.assertEquals(Integer.valueOf(i), tree.contains(i));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void toListThrowsExceptionIfTreeIsNotInitialized() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // then
        tree.toList();
    }

    @Test
    public void toListGivesAnEmptyArrayIfNoValuesHaveBeenAddedToTheTree() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(5);

        // when
        List<Integer> obtained = tree.toList();

        // then
        Assert.assertArrayEquals(new Integer[]{}, obtained.toArray());
    }

    @Test
    public void toListGivesBackValuesInSortedOrder() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(4);

        // generate some random numbers and store them in an array
        Random random = new Random();
        Integer[] expected = new Integer[10000];
        for (int i = 0; i < expected.length; i++) {
            int nr = random.nextInt();
            expected[i] = nr;
            tree.add(nr);
        }

        // when
        List<Integer> obtained = tree.toList();

        // then
        Arrays.sort(expected);
        Assert.assertArrayEquals(expected, obtained.toArray());
    }

    @Test(expected = IllegalStateException.class)
    public void callingDeleteOnUninitializedTreeThrowsException() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        // then
        tree.delete(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeletingANullValueAnExceptionIsThrown() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        // then
        tree.delete(null);
    }

    @Test
    public void deletingValuesFromRootNodeWorks() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(4);
        for (int i = 1; i <= 5; i++)
            tree.add(i);

        // when
        boolean obtained = tree.delete(3);

        // then
        Assert.assertTrue(obtained);
        Assert.assertTrue(tree.getRoot().getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[] {1, 2, 4, 5}, tree.getRoot().getValues().toArray());

        // when
        obtained = tree.delete(1);

        // then
        Assert.assertTrue(obtained);
        Assert.assertTrue(tree.getRoot().getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[] {2, 4, 5}, tree.getRoot().getValues().toArray());

        // when deleting 1 again
        obtained = tree.delete(1);

        // then
        Assert.assertFalse(obtained);
        Assert.assertTrue(tree.getRoot().getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[] {2, 4, 5}, tree.getRoot().getValues().toArray());

        // when
        obtained = tree.delete(5);

        // then
        Assert.assertTrue(obtained);
        Assert.assertTrue(tree.getRoot().getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[] {2, 4}, tree.getRoot().getValues().toArray());

        // when removing the last entries
        obtained = tree.delete(2);
        obtained &= tree.delete(4);

        // then
        Assert.assertTrue(obtained);
        Assert.assertTrue(tree.getRoot().getChildren().isEmpty());
        Assert.assertTrue(tree.getRoot().getValues().isEmpty());
    }

    @Test
    public void deletingValuesFromLeafNodesWorks() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);
        boolean obtained = tree.add(3);
        obtained &= tree.add(4);
        obtained &= tree.add(5);
        obtained &= tree.add(1);
        obtained &= tree.add(6);
        obtained &= tree.add(2);
        obtained &= tree.add(7);

        // then
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{4}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertArrayEquals(new Integer[]{5, 6, 7}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().isEmpty());

        // when deleting a value from left sub tree and one from right sub tree
        obtained = tree.delete(1);
        obtained &= tree.delete(7);

        // then
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{4}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        Assert.assertArrayEquals(new Integer[]{2, 3}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertArrayEquals(new Integer[]{5, 6}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().isEmpty());
    }

    @Test
    public void deletingValuesFromTreeThatDoNotExistInTreeGivesFalse() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(4);

        for (int i = 1; i < 10000; i += 2) {
            // insert odd numbers into tree
            tree.add(i);
        }

        // then check if the BTree is valid
        Assert.assertTrue(isValidBTree(tree));

        // when trying to remove even number from tree
        boolean obtained = false;
        for (int i = 0; i <= 10000; i+= 2) {
            obtained |= tree.delete(i);
        }

        // then
        Assert.assertFalse(obtained);
    }

    @Test
    public void addingAndDeletingValuesFromTree() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(4);

        // when/then adding
        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue(tree.add(i));
            Assert.assertTrue(isValidBTree(tree));
        }
        // when/then deleting
        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue(tree.delete(i));
            Assert.assertTrue(isValidBTree(tree));
        }

        // and when working with negative values only
        tree.clear();
        tree.setMinDegree(2);

        // when/then adding
        for (int i = Integer.MIN_VALUE; i < Integer.MIN_VALUE + 1000; i++) {
            Assert.assertTrue(tree.add(i));
            Assert.assertTrue(isValidBTree(tree));
        }
        // when/then deleting
        for (int i = Integer.MIN_VALUE + 1000; i > Integer.MIN_VALUE; i--) {
            boolean obtained = tree.delete(i - 1);
            Assert.assertTrue(obtained);
            Assert.assertTrue(isValidBTree(tree));
        }

        // and when working with positive values only
        tree.clear();
        tree.setMinDegree(2);

        // when/then adding
        for (int i = Integer.MAX_VALUE - 1000; i < Integer.MAX_VALUE; i++) {
            Assert.assertTrue(tree.add(i));
            Assert.assertTrue(isValidBTree(tree));
        }
        // when/then deleting
        for (int i = Integer.MAX_VALUE; i > Integer.MAX_VALUE - 1000; i--) {
            boolean obtained = tree.delete(i - 1);
            Assert.assertTrue(obtained);
            Assert.assertTrue(isValidBTree(tree));
        }
    }

    @Test
    public void deleteReorganizesTreeEvenIfValueToDeleteWasNotFound() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(2);

        boolean obtained = tree.add(2);
        obtained &= tree.add(3);
        obtained &= tree.add(4);
        obtained &= tree.add(1);

        // and remove 1 again (just wanted to split the root node up)
        obtained &= tree.delete(1);

        // then verify the tree built up
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{3}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        Assert.assertArrayEquals(new Integer[]{2}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertArrayEquals(new Integer[]{4}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().isEmpty());

        // when deleting a value that does not exist in the tree
        obtained = tree.delete(1);

        // then
        Assert.assertFalse(obtained);
        Assert.assertArrayEquals(new Integer[] {2, 3, 4}, tree.getRoot().getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().isEmpty());
    }

    @Test
    public void addDeleteTestCoveringCases() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();
        tree.setMinDegree(3);

        // when adding some values
        boolean obtained = tree.add(1);
        obtained &= tree.add(3);
        obtained &= tree.add(7);
        obtained &= tree.add(10);
        obtained &= tree.add(11);
        obtained &= tree.add(13);
        obtained &= tree.add(14);
        obtained &= tree.add(15);
        obtained &= tree.add(18);
        obtained &= tree.add(16);
        obtained &= tree.add(19);
        obtained &= tree.add(24);
        obtained &= tree.add(25);
        obtained &= tree.add(26);
        obtained &= tree.add(21);
        obtained &= tree.add(4);
        obtained &= tree.add(5);
        obtained &= tree.add(20);
        obtained &= tree.add(22);
        obtained &= tree.add(2);
        obtained &= tree.add(17);
        obtained &= tree.add(12);
        obtained &= tree.add(6);

        // then verify that the tree was built up correctly
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{16}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        // left subtree
        Assert.assertArrayEquals(new Integer[]{3, 7, 13}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertEquals(4, tree.getRoot().getChildren().get(0).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{1, 2}, tree.getRoot().getChildren().get(0).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{4, 5, 6}, tree.getRoot().getChildren().get(0).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{10, 11, 12}, tree.getRoot().getChildren().get(0).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(2).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{14, 15}, tree.getRoot().getChildren().get(0).getChildren().get(3).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(3).getChildren().isEmpty());
        // right subtree
        Assert.assertArrayEquals(new Integer[]{20, 24}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertEquals(3, tree.getRoot().getChildren().get(1).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{17, 18, 19}, tree.getRoot().getChildren().get(1).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{21, 22}, tree.getRoot().getChildren().get(1).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{25, 26}, tree.getRoot().getChildren().get(1).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(2).getChildren().isEmpty());

        // when deleting a value from a leaf node - lecture slides case 1
        obtained = tree.delete(6);

        // then verify the tree
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{16}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        // left subtree
        Assert.assertArrayEquals(new Integer[]{3, 7, 13}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertEquals(4, tree.getRoot().getChildren().get(0).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{1, 2}, tree.getRoot().getChildren().get(0).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{4, 5}, tree.getRoot().getChildren().get(0).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{10, 11, 12}, tree.getRoot().getChildren().get(0).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(2).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{14, 15}, tree.getRoot().getChildren().get(0).getChildren().get(3).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(3).getChildren().isEmpty());
        // right subtree
        Assert.assertArrayEquals(new Integer[]{20, 24}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertEquals(3, tree.getRoot().getChildren().get(1).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{17, 18, 19}, tree.getRoot().getChildren().get(1).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{21, 22}, tree.getRoot().getChildren().get(1).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{25, 26}, tree.getRoot().getChildren().get(1).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(2).getChildren().isEmpty());

        // and when deleting a value from an inner node with predecessor child having enough elements - case 2a
        obtained = tree.delete(13);

        // then verify the tree
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{16}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        // left subtree
        Assert.assertArrayEquals(new Integer[]{3, 7, 12}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertEquals(4, tree.getRoot().getChildren().get(0).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{1, 2}, tree.getRoot().getChildren().get(0).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{4, 5}, tree.getRoot().getChildren().get(0).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{10, 11}, tree.getRoot().getChildren().get(0).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(2).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{14, 15}, tree.getRoot().getChildren().get(0).getChildren().get(3).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(3).getChildren().isEmpty());
        // right subtree
        Assert.assertArrayEquals(new Integer[]{20, 24}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertEquals(3, tree.getRoot().getChildren().get(1).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{17, 18, 19}, tree.getRoot().getChildren().get(1).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{21, 22}, tree.getRoot().getChildren().get(1).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{25, 26}, tree.getRoot().getChildren().get(1).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(2).getChildren().isEmpty());

        // and when deleting a value from and inner node with predecessor and successor child being down to minimum - case 2c
        obtained = tree.delete(7);

        // then verify the tree
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{16}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(2, tree.getRoot().getChildren().size());
        // left subtree
        Assert.assertArrayEquals(new Integer[]{3, 12}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertEquals(3, tree.getRoot().getChildren().get(0).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{1, 2}, tree.getRoot().getChildren().get(0).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{4, 5, 10, 11}, tree.getRoot().getChildren().get(0).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{14, 15}, tree.getRoot().getChildren().get(0).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(2).getChildren().isEmpty());
        // right subtree
        Assert.assertArrayEquals(new Integer[]{20, 24}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertEquals(3, tree.getRoot().getChildren().get(1).getChildren().size());
        Assert.assertArrayEquals(new Integer[]{17, 18, 19}, tree.getRoot().getChildren().get(1).getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{21, 22}, tree.getRoot().getChildren().get(1).getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(1).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{25, 26}, tree.getRoot().getChildren().get(1).getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(1).getChildren().get(2).getChildren().isEmpty());

        // and when deleting a value from and inner node with predecessor being down to min - case 2b (note also covers 3b)
        obtained = tree.delete(3);

        // then verify the tree
        Assert.assertTrue(obtained);
        Assert.assertArrayEquals(new Integer[]{4, 12, 16, 20, 24}, tree.getRoot().getValues().toArray());
        Assert.assertEquals(6, tree.getRoot().getChildren().size());

        Assert.assertArrayEquals(new Integer[]{1, 2}, tree.getRoot().getChildren().get(0).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{5, 10, 11}, tree.getRoot().getChildren().get(1).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{14, 15}, tree.getRoot().getChildren().get(2).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{17, 18, 19}, tree.getRoot().getChildren().get(3).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{21, 22}, tree.getRoot().getChildren().get(4).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
        Assert.assertArrayEquals(new Integer[]{25, 26}, tree.getRoot().getChildren().get(5).getValues().toArray());
        Assert.assertTrue(tree.getRoot().getChildren().get(0).getChildren().isEmpty());
    }

    @Test
    @Ignore("Long running integration test")
    public void integrationTests() {

        // given
        BTree<Integer> tree = new BTreeImpl<>();

        for (int degree = 2; degree < 200; degree++) {

            System.out.println("Testing tree with degree " + degree + " ... ");

            // re-initialize the tree
            tree.clear();
            tree.setMinDegree(degree);

            Set<Integer> values = new HashSet<>();
            Random random = new Random();

            // build up the tree with random values and remember the values which were added
            System.out.println("Testing adding values ... ");
            while (values.size() < 100000) {
                int valueToAdd = random.nextInt();
                boolean obtained = tree.add(valueToAdd);

                // verify the tree is still valid after each insert (no matter whether it fails or not)
                Assert.assertTrue(isValidBTree(tree));

                if (values.contains(valueToAdd)) {
                    Assert.assertFalse(obtained);
                } else {
                    Assert.assertTrue(obtained);
                    values.add(valueToAdd);
                }
                // after the value was added - or already existed, it must be contained
                Assert.assertEquals(Integer.valueOf(valueToAdd), tree.contains(valueToAdd));

                // verify tree traversal
                Integer[] tmp = new Integer[values.size()];
                values.toArray(tmp);
                Arrays.sort(tmp);

                Assert.assertArrayEquals(tmp, tree.toList().toArray());

                if ((values.size() % 10000) == 0)
                    System.out.println("Testing adding values [" + values.size() + "]");
            }

            // now delete some values again
            System.out.println("Testing deleting values ... ");
            Object[] valuesArray = values.toArray();
            for (int i = 0; i < valuesArray.length; i++) {

                // take even values from previously inserted values, odd values shall be randomly generated
                int valueToDelete = ((i % 2) == 0) ? (int)(valuesArray[i]) : random.nextInt();

                boolean obtained = tree.delete(valueToDelete);


                // verify the tree is still valid after each delete (no matter whether it fails or not)
                Assert.assertTrue(isValidBTree(tree));

                if (values.contains(valueToDelete)) {
                    Assert.assertTrue(obtained);
                    values.remove(valueToDelete);
                } else {
                    Assert.assertFalse(obtained);
                }
                // after the value was delete - or did not exist before, it must no longer be contained
                Assert.assertNull(tree.contains(valueToDelete));

                // verify tree traversal
                Integer[] tmp = new Integer[values.size()];
                values.toArray(tmp);
                Arrays.sort(tmp);

                Assert.assertArrayEquals(tmp, tree.toList().toArray());

                if (i > 0 && (i % 10000) == 0)
                    System.out.println("Testing deleting values [" + values.size() + "]");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <V extends Comparable<V>> boolean isValidBTree(BTree<V> tree) {

        // test root node constraints
        if (tree.getRoot().getValues().isEmpty())
            return tree.getRoot().getChildren().isEmpty(); // empty BTree -> no values -> no children allowed

        if (tree.getRoot().getValues().size() < 1 || tree.getRoot().getValues().size() > ((((BNodeImpl<?>)tree.getRoot()).getMinDegree() * 2) -1))
            return false;

        if (!tree.getRoot().getChildren().isEmpty()) {
            if (tree.getRoot().getChildren().size() < 2 || tree.getRoot().getChildren().size() > (((BNodeImpl<?>)tree.getRoot()).getMinDegree() * 2))
                return false;
        }

        if (!tree.getRoot().getChildren().isEmpty() && tree.getRoot().getValues().size() != (tree.getRoot().getChildren().size() - 1))
            return false;

        if (!areValueConstraintsValid((BNodeImpl<V>)tree.getRoot())) {
            return false;
        }

        for (BNode<?> node : tree.getRoot().getChildren()) {
            if (!isValidBTreeNode((BNodeImpl<V>) node, ((BNodeImpl<V>)tree.getRoot()).getMinDegree()))
                return false;
        }

        return checkHeightConstraints(tree.getRoot());
    }

    private static boolean checkHeightConstraints(BNode<?> node) {

        if (node.getChildren().isEmpty())
            return true; // leaf node

        int height = getHeight(node.getChildren().get(0)); // reference height
        for (int i = 0; i < node.getChildren().size(); i++) {
            if (height != getHeight(node.getChildren().get(i)))
                return false;
        }

        for (BNode<?> childNode : node.getChildren())
            if (!checkHeightConstraints(childNode))
                return false;

        return true;
    }

    private static int getHeight(BNode<?> node) {

        if (node.getChildren().isEmpty())
            return 0;

        return node.getChildren().stream().mapToInt(BTreeImplSpec::getHeight).max().getAsInt() + 1;
    }

    private static <V extends Comparable<V>> boolean isValidBTreeNode(BNodeImpl<V> node, int minDegree) {

        if (node.getMinDegree() != minDegree)
            return false;

        if (node.getValues().size() < (minDegree - 1) || node.getValues().size() > ((minDegree * 2) -1))
            return false;

        if (!node.getChildren().isEmpty()) {
            if (node.getChildren().size() < minDegree || node.getChildren().size() > (minDegree * 2))
                return false;
        }

        if (!node.getChildren().isEmpty() && node.getValues().size() != (node.getChildren().size() - 1))
            return false;

        if (!areValueConstraintsValid(node)) {
            return false;
        }

        for (BNode<V> child : node.getChildren())
            if (!isValidBTreeNode((BNodeImpl<V>)child, minDegree))
                return false;

        return true;
    }

    private static <V extends Comparable<V>> boolean areValueConstraintsValid(BNodeImpl<V> node) {

        // test values - must be in ascending order
        for (int i = 1; i < node.getValues().size(); i++) {
            if (node.getValues().get(i - 1).compareTo(node.getValues().get(i)) >= 0)
                return false;
        }

        if (node.getChildren().isEmpty()) // leaf node
            return true;

        for (int i = 0; i < node.getValues().size(); i++) {

            V value = node.getValues().get(i);
            if (node.getChildren().get(i).getValues().stream().anyMatch(childValue -> childValue.compareTo(value) >= 0))
                return false;
        }

        V value = node.getValues().get(node.getValues().size() - 1);
        return node.getChildren().get(node.getValues().size()).getValues().stream().allMatch(childValue -> childValue.compareTo(value) >= 0);
    }
}
