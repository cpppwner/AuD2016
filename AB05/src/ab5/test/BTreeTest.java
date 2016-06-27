package ab5.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import ab5.BTree;
import ab5.impl.Eberl.BTreeImpl;

public class BTreeTest {

	private BTree<Integer> btree = new BTreeImpl<Integer>();

	@Test
	public void testInsertBasic() {
		btree.clear();
		btree.setMinDegree(2);

		for (int i = 0; i < 10; i++)
			btree.add(i);
	}

	@Test
	public void testInsertAdvanced() {
		btree.clear();
		btree.setMinDegree(2);

		for (int i = 0; i < 10; i++)
			btree.add(i);

		Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), btree.toList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInsertNull() {
		btree.clear();
		btree.setMinDegree(2);

		btree.add(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testMissingMinDegree() {
		btree.clear();

		btree.add(2);
	}

	@Test(expected = IllegalStateException.class)
	public void testMinDegree() {
		btree.clear();
		btree.setMinDegree(2);
		btree.setMinDegree(2);

	}

	@Test
	public void testDeleteBasic() {
		btree.clear();
		btree.setMinDegree(2);

		for (int i = 0; i < 10; i++)
			btree.add(i);

		for (int i = 0; i < 10; i++)
			btree.delete(i);
	}

	@Test
	public void testDeleteAdvanced() {
		btree.clear();
		btree.setMinDegree(2);

		for (int i = 0; i < 10; i++)
			btree.add(i);

		for (int i = 0; i < 5; i++)
			btree.delete(i);

		Assert.assertEquals(Arrays.asList(5, 6, 7, 8, 9), btree.toList());
	}

	@Test
	public void testContains() {
		btree.clear();
		btree.setMinDegree(2);

		for (int i = 0; i < 10; i++)
			btree.add(i);

		for (int i = 0; i < 10; i++)
			Assert.assertEquals(Integer.valueOf(i), btree.contains(i));
	}

}