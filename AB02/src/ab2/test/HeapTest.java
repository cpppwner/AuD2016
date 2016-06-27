package ab2.test;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import ab2.AuDHeap;
import ab2.impl.Eberl.AuDHeapImpl;

public class HeapTest {

	public static final int MAX_HEAP_SIZE = 1000;

	AuDHeap tools = new AuDHeapImpl();
	//int[] heap = { 1, 2, 4, 5, 2, 3, 4, 9, 7, 5, 6, 3, 4, 8, 10, 11, 3, 2, 78, 5, 67, 8 };
	int[] heap = { 8, 2, 4, 3, 6, 7, 5 };

	@Test
	public void testCreateHeap() throws Exception {
		tools.createEmptyHeap(MAX_HEAP_SIZE);
		int result = tools.size();
		int emptiness = tools.elementCount();
		Assert.assertEquals(MAX_HEAP_SIZE, result);
		Assert.assertEquals(0, emptiness);
	}

	@Test
	public void testElementCount() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		int result = tools.elementCount();
		Assert.assertEquals(heap.length, result);
	}

	@Test
	public void testSize() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		int result = tools.size();
		Assert.assertEquals(MAX_HEAP_SIZE, result);
	}

	@Test
	public void testToArrayLength() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		Assert.assertEquals(heap.length, tools.toArray().length);
	}

	@Test
	public void testToArray() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		Assert.assertEquals(true, checkHeap(tools));
	}

	@Test
	public void testCreateEmptyHeap() throws Exception {
		tools.createEmptyHeap(MAX_HEAP_SIZE);
		int result = tools.size();
		int emptiness = tools.elementCount();
		Assert.assertEquals(MAX_HEAP_SIZE,result);
		Assert.assertEquals(0,emptiness);
	}

	@Test
	public void testAddElement() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		tools.addElement(1);
		tools.addElement(20);
		tools.addElement(1);
		Assert.assertEquals(true, checkHeap(tools));
	}

	@Test
	public void testRemoveFirst() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		tools.removeFirst();
		Assert.assertEquals(true, checkHeap(tools));
	}

	@Test
	public void testRemove() throws Exception {
		tools.createHeap(heap, MAX_HEAP_SIZE);
		//eine zufällig gewähltes Element löschen
		tools.remove(new Random(System.currentTimeMillis()).nextInt(heap.length));
		Assert.assertEquals(true, checkHeap(tools));
	}

	@Test
	public void isMinHeap() throws Exception {
		tools.createHeap(new int[] {8,2,4,3,6,7,5}, 8);
		int[] array = tools.toArray();

		for (int i=0; i<array.length; i++)
			System.out.print(array[i] + " ");

		int[] result = {2,3,4,8,6,7,5};

		Assert.assertArrayEquals(result, array);
	}

	public static boolean checkHeap(AuDHeap heap) {
		int[] heapArray = heap.toArray();
		for (int i = 0; i < heapArray.length; i++) {

			//Bestimmung der Indizes ... 0-basierte Indizierung betrachten
			int actIdx = i;
			int nextIdx1 = 2*(actIdx+1) - 1;
			int nextIdx2 = nextIdx1+1;

			//Es ist schon ein Blatt
			if(nextIdx1 >= heapArray.length)
				return true;

			if (heapArray[actIdx] > heapArray[nextIdx1])
				return false;

			//Jetzt kommen nur noch Blätter.
			if(nextIdx2 >= heapArray.length)
				return true;

			if (heapArray[actIdx] > heapArray[nextIdx2])
				return false;
		}
		return true;
	}
}