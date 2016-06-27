package ab2.test;

import ab2.AuDHeap;
import ab2.impl.Eberl.AuDHeapImpl;
import org.junit.Assert;
import org.junit.Test;

public class HeapSpec {

    /**
     * Regarding to the interface, we should create an empty heap with size 37
     * when default CTOR is called, for whatever reason, but let's make sure it's implemented
     */
    @Test
    public void defaultConstructorCreatesAnEmptyHeapWithSizeThirtySeven() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // then
        Assert.assertEquals(0, heap.elementCount());
        Assert.assertEquals(37, heap.size());
        Assert.assertArrayEquals(new int[0], heap.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingAnEmptyHeapWithANegativeValueThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // then
        heap.createEmptyHeap(-1);
    }

    @Test
    public void creatingAnEmptyHeapWithGivenValueDoesWhatItSays() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // when
        heap.createEmptyHeap(42);

        // then
        Assert.assertEquals(0, heap.elementCount());
        Assert.assertEquals(42, heap.size());
        Assert.assertArrayEquals(new int[0], heap.toArray());
    }

    @Test
    public void creatingAHeapWithZeroSizeIsAllowed() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // when
        heap.createEmptyHeap(0);

        // then
        Assert.assertEquals(0, heap.elementCount());
        Assert.assertEquals(0, heap.size());
        Assert.assertArrayEquals(new int[0], heap.toArray());
    }

    @Test(expected = IllegalStateException.class)
    public void addingAnElementToAnEmptyHeapThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        heap.createEmptyHeap(0);

        // then
        heap.addElement(1);
    }

    @Test(expected = IllegalStateException.class)
    public void addingTwoElementsToHeapWithSizeOneThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        heap.createEmptyHeap(1);

        // when adding the first element
        heap.addElement(1);

        // then ensure everything went ok so far
        Assert.assertEquals(1, heap.elementCount());
        Assert.assertArrayEquals(new int[]{1}, heap.toArray());

        // and then an exception is thrown when adding the second element
        heap.addElement(2);
    }

    @Test
    public void addingElementsToAHeapBuildsUpTheHeapCorrectly() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // when adding the first element
        heap.addElement(7);

        // then
        Assert.assertEquals(1, heap.elementCount());
        Assert.assertArrayEquals(new int[]{7}, heap.toArray());

        // and when adding the second element
        heap.addElement(6);

        // then
        Assert.assertEquals(2, heap.elementCount());
        Assert.assertArrayEquals(new int[]{6, 7}, heap.toArray());

        // and when adding the third element
        heap.addElement(5);

        // then
        Assert.assertEquals(3, heap.elementCount());
        Assert.assertArrayEquals(new int[]{5, 7, 6}, heap.toArray());

        // and when adding the fourth element
        heap.addElement(4);

        // then
        Assert.assertEquals(4, heap.elementCount());
        Assert.assertArrayEquals(new int[]{4, 5, 6, 7}, heap.toArray());

        // and when adding the fifth element
        heap.addElement(3);

        // then
        Assert.assertEquals(5, heap.elementCount());
        Assert.assertArrayEquals(new int[]{3, 4, 6, 7, 5}, heap.toArray());

        // and when adding the sixth element
        heap.addElement(2);

        // then
        Assert.assertEquals(6, heap.elementCount());
        Assert.assertArrayEquals(new int[]{2, 4, 3, 7, 5, 6}, heap.toArray());

        // and when adding the seventh element
        heap.addElement(1);

        // then
        Assert.assertEquals(7, heap.elementCount());
        Assert.assertArrayEquals(new int[]{1, 4, 2, 7, 5, 6, 3}, heap.toArray());
    }

    @Test
    public void addingSameValuesBuildsUpTheHeapCorrectly() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // when adding multiple elements to the heap where each level (each siblings) are equal
        heap.addElement(4);
        heap.addElement(4);
        heap.addElement(4);
        heap.addElement(4);
        heap.addElement(3);
        heap.addElement(2);
        heap.addElement(4);
        heap.addElement(2);
        heap.addElement(3);
        heap.addElement(4);
        heap.addElement(3);
        heap.addElement(4);
        heap.addElement(3);
        heap.addElement(4);
        heap.addElement(1);

        Assert.assertArrayEquals(new int[]{1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4}, heap.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void passingNullArrayToCreateHeapThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // then
        heap.createHeap(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passingHeapSizeLessThanInputArrayThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();

        // then
        heap.createHeap(new int[]{1, 2, 3, 4, 5}, 0);
    }

    @Test
    public void creatingAHeapFromGivenArrayBuildsUpTheHeap() {

        // given
        int[] data = {7, 6, 5, 3, 4, 2, 1};
        AuDHeap heap = new AuDHeapImpl();

        // when
        heap.createHeap(data, data.length);

        // then
        Assert.assertEquals(data.length, heap.size());
        Assert.assertEquals(data.length, heap.elementCount());
        Assert.assertArrayEquals(new int[]{1, 3, 2, 6, 4, 7, 5}, heap.toArray());
    }

    @Test
    public void creatingAHeapFromGivenArrayWithDoubleSizeBuildsUpTheHeap() {

        // given
        int[] data = {4, 4, 4, 4, 3, 3, 2};
        int size = data.length * 2;
        AuDHeap heap = new AuDHeapImpl();

        // when
        heap.createHeap(data, size);

        // then
        Assert.assertEquals(size, heap.size());
        Assert.assertEquals(data.length, heap.elementCount());
        Assert.assertArrayEquals(new int[]{2, 3, 3, 4, 4, 4, 4}, heap.toArray());
    }

    @Test(expected = IllegalStateException.class)
    public void removingFirstElementFromAnEmptyHeapThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        heap.createEmptyHeap(0);

        // then
        heap.removeFirst();
    }

    @Test
    public void removingFirstElementFromHeapReturnsElement() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        heap.addElement(42);

        // when
        int obtained = heap.removeFirst();

        // then
        Assert.assertEquals(42, obtained);
    }

    @Test
    public void removingFirstElementFromHeapAdjustsElementCountAccordingly() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 2, 2, 3, 3, 3, 3};
        heap.createHeap(data, 42);

        // when
        int obtained = heap.removeFirst();

        // then
        Assert.assertEquals(data[0], obtained);
        Assert.assertEquals(data.length - 1, heap.elementCount());
        Assert.assertEquals(42, heap.size());
    }

    @Test
    public void removingFirstElementFromHeapRebuildsHeapAccordingly() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 2, 3, 4, 5, 6, 7};
        heap.createHeap(data, 42);

        // when removing first
        int obtained = heap.removeFirst();

        // then
        Assert.assertEquals(1, obtained);
        Assert.assertArrayEquals(new int[]{2, 4, 3, 7, 5, 6}, heap.toArray());

        // and when removing first again
        obtained = heap.removeFirst();

        // then
        Assert.assertEquals(2, obtained);
        Assert.assertArrayEquals(new int[]{3, 4, 6, 7, 5}, heap.toArray());

        // and when removing first again
        obtained = heap.removeFirst();

        // then
        Assert.assertEquals(3, obtained);
        Assert.assertArrayEquals(new int[]{4, 5, 6, 7}, heap.toArray());

        // and when removing first again
        obtained = heap.removeFirst();

        // then
        Assert.assertEquals(4, obtained);
        Assert.assertArrayEquals(new int[]{5, 7, 6}, heap.toArray());

        // and when removing first again
        obtained = heap.removeFirst();

        // then
        Assert.assertEquals(5, obtained);
        Assert.assertArrayEquals(new int[]{6, 7}, heap.toArray());

        // and when removing first again
        obtained = heap.removeFirst();

        // then
        Assert.assertEquals(6, obtained);
        Assert.assertArrayEquals(new int[]{7}, heap.toArray());

        // and when removing first again (last element)
        obtained = heap.removeFirst();

        // then
        Assert.assertEquals(7, obtained);
        Assert.assertArrayEquals(new int[]{}, heap.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removingNegativeIndexFromHeapThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 2, 3, 4, 5, 6, 7};
        heap.createHeap(data, data.length);

        // then
        heap.remove(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removingIndexEqualToElementCountThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 2, 3, 4, 5, 6, 7};
        heap.createHeap(data, data.length);

        // then
        heap.remove(data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removingIndexGreaterThanElementCountThrowsAnException() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 2, 3, 4, 5, 6, 7};
        heap.createHeap(data, data.length);

        // then
        heap.remove(data.length + 1);
    }

    @Test
    public void removingGivenIndexFromHeapAdjustsElementCountAccordingly() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 2, 3, 4, 5};
        heap.createHeap(data, data.length);

        // when
        int obtained = heap.remove(4);

        // then
        Assert.assertEquals(5, obtained);
        Assert.assertEquals(4, heap.elementCount());

        // and when
        obtained = heap.remove(2);

        // then
        Assert.assertEquals(3, obtained);
        Assert.assertEquals(3, heap.elementCount());

        // and when
        obtained = heap.remove(1);

        // then
        Assert.assertEquals(2, obtained);
        Assert.assertEquals(2, heap.elementCount());

        // and when
        obtained = heap.remove(0);

        // then
        Assert.assertEquals(1, obtained);
        Assert.assertEquals(1, heap.elementCount());

        // and when
        obtained = heap.remove(0);

        // then
        Assert.assertEquals(4, obtained);
        Assert.assertEquals(0, heap.elementCount());
    }

    @Test
    public void removingGivenIndexFromHeapEnsuresHeapConstraintsAreNotViolated() {

        // given
        AuDHeap heap = new AuDHeapImpl();
        int[] data = {1, 5, 2, 6, 7, 3, 4};
        heap.createHeap(data, data.length);

        // when removing an element where sift-up is required
        int obtained = heap.remove(3);

        // then
        Assert.assertEquals(6, obtained);
        Assert.assertArrayEquals(new int[]{1, 4, 2, 5, 7, 3}, heap.toArray());

        // and when removing an element where sift-down is required
        obtained = heap.remove(0);

        // then
        Assert.assertEquals(1, obtained);
        Assert.assertArrayEquals(new int[]{2, 4, 3, 5, 7}, heap.toArray());

        // and when removing an element where nothing is required
        obtained = heap.remove(2);

        // then
        Assert.assertEquals(3, obtained);
        Assert.assertArrayEquals(new int[]{2, 4, 7, 5}, heap.toArray());

        // and when removing the last element
        obtained = heap.remove(3);

        // then
        Assert.assertEquals(5, obtained);
        Assert.assertArrayEquals(new int[]{2, 4, 7}, heap.toArray());

        // and when sift-down is required again
        obtained = heap.remove(0);

        // then
        Assert.assertEquals(2, obtained);
        Assert.assertArrayEquals(new int[]{4, 7}, heap.toArray());

        // and when only swapping is performed
        obtained = heap.remove(0);

        // then
        Assert.assertEquals(4, obtained);
        Assert.assertArrayEquals(new int[]{7}, heap.toArray());

        // and when removing the last element
        obtained = heap.remove(0);

        // then
        Assert.assertEquals(7, obtained);
        Assert.assertArrayEquals(new int[]{}, heap.toArray());
    }
}
