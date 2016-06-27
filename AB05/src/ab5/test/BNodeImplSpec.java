package ab5.test;

import ab5.BNode;
import ab5.impl.Eberl.BNodeImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BNodeImplSpec {

    @Test
    public void aDefaultConstructedBNodeInitializesValuesAndChildren() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);

        // then
        Assert.assertNotNull(node.getValues());
        Assert.assertNotNull(node.getChildren());
        Assert.assertTrue(node.getValues().isEmpty());
        Assert.assertTrue(node.getChildren().isEmpty());
    }

    @Test
    public void getMinDegreeReturnsValueProvidedInCtor() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(25);

        // then
        Assert.assertEquals(25, node.getMinDegree());
    }

    @Test
    public void aNodeWithNoChildrenIsALeafNode() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);
        node.getValues().addAll(Arrays.asList(1, 2)); // just some dummy data

        // then
        Assert.assertTrue(node.isLeafNode());
    }

    @Test
    public void aNodeWithAtLeastOneChildIsNotALeafNode() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);
        node.setValues(Arrays.asList(1, 2)); // just some dummy data

        // when
        node.getChildren().add(new BNodeImpl<>(2));

        // then
        Assert.assertFalse(node.isLeafNode());
    }

    @Test
    public void hasReachedMinWorks() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(3);

        // when node is still empty
        boolean obtained = node.hasReachedMin();

        // then
        Assert.assertFalse(obtained);

        // and when adding a value
        node.getValues().add(1);
        obtained = node.hasReachedMin();

        // then
        Assert.assertFalse(obtained);

        // and when adding a second value
        node.getValues().add(2);
        obtained = node.hasReachedMin();

        // then
        Assert.assertTrue(obtained);

        // and when adding a third value
        node.getValues().add(3);
        obtained = node.hasReachedMin();

        // then
        Assert.assertFalse(obtained);
    }

    @Test
    public void hasReachedMaxWorks() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);

        // when node is still empty
        boolean obtained = node.hasReachedMax();

        // then
        Assert.assertFalse(obtained);

        // and when adding a value
        node.getValues().add(1);
        obtained = node.hasReachedMax();

        // then
        Assert.assertFalse(obtained);

        // and when adding a second value
        node.getValues().add(2);
        obtained = node.hasReachedMax();

        // then
        Assert.assertFalse(obtained);

        // and when adding a third value
        node.getValues().add(3);
        obtained = node.hasReachedMax();

        // then
        Assert.assertTrue(obtained);

        // and when adding a fourth value
        node.getValues().add(4);
        obtained = node.hasReachedMax();

        // then
        Assert.assertFalse(obtained);
    }

    @Test
    public void numberOfValues() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);

        // then
        Assert.assertEquals(0, node.numberOfValues());

        // and when
        node.getValues().addAll(Arrays.asList(100, 200, 300));

        // then
        Assert.assertEquals(3, node.numberOfValues());
    }

    @Test
    public void hasValues() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);

        // then
        Assert.assertFalse(node.hasValues());

        // and when
        node.getValues().addAll(Arrays.asList(100, 200, 300));

        // then
        Assert.assertTrue(node.hasValues());
    }

    @Test
    public void numberOfChildren() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);

        // then
        Assert.assertEquals(0, node.numberOfChildren());

        // and when
        node.getChildren().addAll(Arrays.asList(new BNodeImpl<>(2), new BNodeImpl<>(2), new BNodeImpl<>(2)));

        // then
        Assert.assertEquals(3, node.numberOfChildren());
    }

    @Test
    public void hasChildren() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);

        // then
        Assert.assertFalse(node.hasChildren());

        // and when
        node.getChildren().addAll(Arrays.asList(new BNodeImpl<>(2), new BNodeImpl<>(2), new BNodeImpl<>(2)));

        // then
        Assert.assertTrue(node.hasChildren());
    }

    @Test
    public void valueAtReturnsValueAtGivenIndex() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);
        node.getValues().addAll(Arrays.asList(10, 20, 30));

        // then
        Assert.assertEquals(Integer.valueOf(10), node.valueAt(0));
        Assert.assertEquals(Integer.valueOf(20), node.valueAt(1));
        Assert.assertEquals(Integer.valueOf(30), node.valueAt(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void valueAtThrowsIndexOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);
        node.getValues().addAll(Arrays.asList(10, 20, 30));

        // then
        node.valueAt(3);
    }

    @Test
    public void valueAtSetsValueAtGivenIndex() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);
        node.getValues().addAll(Arrays.asList(10, 20, 30));

        // when
        node.valueAt(0, 11);
        node.valueAt(1, 22);
        node.valueAt(2, 33);

        // then
        Assert.assertArrayEquals(new Integer[]{11, 22, 33}, node.getValues().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void settingValueAttThrowsIndexOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(2);
        node.getValues().addAll(Arrays.asList(10, 20, 30));

        // then
        node.valueAt(3, 40);
    }

    @Test
    public void addValueInsertsValueAtGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // when
        node.addValue(0, 10);
        node.addValue(0, 5);
        node.addValue(2, 15);
        node.addValue(1, 7);

        // then
        Assert.assertArrayEquals(Arrays.asList(5, 7, 10, 15).toArray(), node.getValues().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addValueAtOutOfBoundsPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // when
        node.addValue(1, 10);
    }

    @Test
    public void addValuesInsertsValueAtGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // when
        node.addValues(0, Arrays.asList(10, 11));
        node.addValues(0, Arrays.asList(5, 6));
        node.addValues(4, Arrays.asList(13, 14, 15));
        node.addValues(2, Arrays.asList(8, 9));

        // then
        Assert.assertArrayEquals(new Integer[]{5, 6, 8, 9, 10, 11, 13, 14, 15}, node.getValues().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addValuesAtOutOfBoundsPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // when
        node.addValues(1, Arrays.asList(1, 2, 3));
    }

    @Test
    public void removeValueRemovesValueFromGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getValues().addAll(0, Arrays.asList(1, 2, 3, 4, 5));

        // when
        int obtained = node.removeValue(2);

        // then
        Assert.assertEquals(3, obtained);
        Assert.assertArrayEquals(new Integer[]{1, 2, 4, 5}, node.getValues().toArray());

        // and when
        obtained = node.removeValue(3);

        // then
        Assert.assertEquals(5, obtained);
        Assert.assertArrayEquals(new Integer[]{1, 2, 4}, node.getValues().toArray());

        obtained = node.removeValue(0);

        // then
        Assert.assertEquals(1, obtained);
        Assert.assertArrayEquals(new Integer[]{2, 4}, node.getValues().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeValueOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getValues().addAll(Arrays.asList(1, 2, 3));

        // then
        node.removeValue(4);
    }

    @Test
    public void removeValuesRemovesValueFromGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getValues().addAll(0, Arrays.asList(1, 2, 3, 4, 5));

        // when
        List<Integer> obtained = node.removeValues(2, 4);

        // then
        Assert.assertArrayEquals(new Integer[]{3, 4}, obtained.toArray());
        Assert.assertArrayEquals(new Integer[]{1, 2, 5}, node.getValues().toArray());

        // and when
        obtained = node.removeValues(1, 3);

        // then
        Assert.assertArrayEquals(Arrays.asList(2, 5).toArray(), obtained.toArray());
        Assert.assertArrayEquals(new Integer[]{1}, node.getValues().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeValuesOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getValues().addAll(Arrays.asList(1, 2, 3));

        // then
        node.removeValues(2, 10);
    }

    @Test
    public void childAtGivesChildAtIndex() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        List<BNode<Integer>> children = Arrays.asList(new BNodeImpl<>(2), new BNodeImpl<>(3), new BNodeImpl<>(4));
        node.getChildren().addAll(children);

        // then
        Assert.assertSame(children.get(0), node.childAt(0));
        Assert.assertSame(children.get(1), node.childAt(1));
        Assert.assertSame(children.get(2), node.childAt(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void childAtThrowsExceptionIfIndexIsOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getChildren().addAll(Arrays.asList(new BNodeImpl<>(2), new BNodeImpl<>(3), new BNodeImpl<>(4)));

        // then
        node.childAt(3);
    }

    @Test
    public void addChildInsertsValueAtGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        BNodeImpl<Integer> childOne = new BNodeImpl<>(24);
        BNodeImpl<Integer> childZero = new BNodeImpl<>(24);
        BNodeImpl<Integer> childThree = new BNodeImpl<>(24);
        BNodeImpl<Integer> childTwo = new BNodeImpl<>(24);

        // when
        node.addChild(0, childTwo);
        node.addChild(0, childZero);
        node.addChild(2, childThree);
        node.addChild(1, childOne);

        // then
        Assert.assertArrayEquals(new Object[] { childZero, childOne, childTwo, childThree}, node.getChildren().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addChildAtOutOfBoundsPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // when
        node.addChild(1, new BNodeImpl<>(24));
    }

    @Test
    public void addChildrenInsertsValueAtGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        List<BNode<Integer>> childrenOne = Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24));
        List<BNode<Integer>> childrenZero = Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24));
        List<BNode<Integer>> childrenThree = Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24));
        List<BNode<Integer>> childrenTwo = Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24));

        node.addChildren(0, childrenTwo);
        node.addChildren(0, childrenZero);
        node.addChildren(4, childrenThree);
        node.addChildren(2, childrenOne);

        // then
        List<BNode<Integer>> expected = new LinkedList<>();
        expected.addAll(childrenZero);
        expected.addAll(childrenOne);
        expected.addAll(childrenTwo);
        expected.addAll(childrenThree);
        Assert.assertArrayEquals(expected.toArray(), node.getChildren().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addChildrenAtOutOfBoundsPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // when
        node.addChildren(1, Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24)));
    }

    @Test
    public void removeChildRemovesChildFromGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        BNodeImpl<Integer> childOne = new BNodeImpl<>(24);
        BNodeImpl<Integer> childTwo = new BNodeImpl<>(24);
        BNodeImpl<Integer> childThree = new BNodeImpl<>(24);
        BNodeImpl<Integer> childFour = new BNodeImpl<>(24);
        BNodeImpl<Integer> childFive = new BNodeImpl<>(24);

        node.getChildren().addAll(0, Arrays.asList(childOne, childTwo, childThree, childFour, childFive));

        // when
        BNode<Integer> obtained = node.removeChild(1);

        // then
        Assert.assertSame(childTwo, obtained);
        Assert.assertArrayEquals(new Object[]{childOne, childThree, childFour, childFive}, node.getChildren().toArray());

        // and when
        obtained = node.removeChild(0);

        // then
        Assert.assertSame(childOne, obtained);
        Assert.assertArrayEquals(new Object[]{childThree, childFour, childFive}, node.getChildren().toArray());

        // and when
        obtained = node.removeChild(2);

        // then
        Assert.assertSame(childFive, obtained);
        Assert.assertArrayEquals(new Object[]{childThree, childFour}, node.getChildren().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeChildOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getChildren().addAll(Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24), new BNodeImpl<>(24)));

        // then
        node.removeChild(3);
    }

    @Test
    public void removeChildrenRemovesChildrenFromGivenPosition() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        BNodeImpl<Integer> childOne = new BNodeImpl<>(24);
        BNodeImpl<Integer> childTwo = new BNodeImpl<>(24);
        BNodeImpl<Integer> childThree = new BNodeImpl<>(24);
        BNodeImpl<Integer> childFour = new BNodeImpl<>(24);
        BNodeImpl<Integer> childFive = new BNodeImpl<>(24);

        node.getChildren().addAll(0, Arrays.asList(childOne, childTwo, childThree, childFour, childFive));

        // when
        List<BNode<Integer>> obtained = node.removeChildren(2, 4);

        // then
        Assert.assertArrayEquals(new Object[]{childThree, childFour}, obtained.toArray());
        Assert.assertArrayEquals(new Object[]{childOne, childTwo, childFive}, node.getChildren().toArray());

        // and when
        obtained = node.removeChildren(1, 3);

        // then
        Assert.assertArrayEquals(new Object[]{childTwo, childFive}, obtained.toArray());
        Assert.assertArrayEquals(new Object[]{childOne}, node.getChildren().toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeChildrenOutOfBounds() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getChildren().addAll(Arrays.asList(new BNodeImpl<>(24), new BNodeImpl<>(24), new BNodeImpl<>(24)));

        // then
        node.removeChildren(2, 10);
    }

    @Test
    public void findIndexWithGreaterOrEqualValueIfValuesAreEmpty() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);

        // then
        Assert.assertEquals(~0, node.findIndexWithGreaterOrEqualValue(1));
        Assert.assertEquals(~0, node.findIndexWithGreaterOrEqualValue(0));
        Assert.assertEquals(~0, node.findIndexWithGreaterOrEqualValue(-5));
    }

    @Test
    public void findIndexWithGreaterOrEqualValue() {

        // given
        BNodeImpl<Integer> node = new BNodeImpl<>(24);
        node.getValues().addAll(Arrays.asList(-5, -3, -1, 1, 3, 5));

        // then
        Assert.assertEquals(~0, node.findIndexWithGreaterOrEqualValue(-6));
        Assert.assertEquals(0, node.findIndexWithGreaterOrEqualValue(-5));
        Assert.assertEquals(~1, node.findIndexWithGreaterOrEqualValue(-4));
        Assert.assertEquals(1, node.findIndexWithGreaterOrEqualValue(-3));
        Assert.assertEquals(~2, node.findIndexWithGreaterOrEqualValue(-2));
        Assert.assertEquals(2, node.findIndexWithGreaterOrEqualValue(-1));
        Assert.assertEquals(~3, node.findIndexWithGreaterOrEqualValue(0));
        Assert.assertEquals(3, node.findIndexWithGreaterOrEqualValue(1));
        Assert.assertEquals(~4, node.findIndexWithGreaterOrEqualValue(2));
        Assert.assertEquals(4, node.findIndexWithGreaterOrEqualValue(3));
        Assert.assertEquals(~5, node.findIndexWithGreaterOrEqualValue(4));
        Assert.assertEquals(5, node.findIndexWithGreaterOrEqualValue(5));
        Assert.assertEquals(~6, node.findIndexWithGreaterOrEqualValue(6));
        Assert.assertEquals(~6, node.findIndexWithGreaterOrEqualValue(7));
    }
}
