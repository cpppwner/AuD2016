package ab4.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import ab4.Node;
import ab4.Tree;
import ab4.impl.Eberl.TreeImpl;

public class TreeTest {

	private Tree<Character> tree = new TreeImpl<Character>();

	@Test
	public void testLWR1() {
		tree.clear();

		tree.addValue('a');
		tree.addValue('g');
		tree.addValue('f');
		tree.addValue('z');
		tree.addValue('k');

		Assert.assertEquals(Arrays.asList('a', 'f', 'g', 'k', 'z'), tree.toLWR());
	}

	@Test
	public void testLWR2() {
		tree.clear();

		tree.addValue('a');
		tree.addValue('g');
		tree.addValue('f');
		tree.addValue('z');
		tree.addValue('k');
		tree.addValue('b');
		tree.addValue('u');
		tree.addValue('l');
		tree.addValue('c');

		tree.removeValue('k');

		Assert.assertEquals(Arrays.asList('a', 'b', 'c', 'f', 'g', 'l', 'u', 'z'), tree.toLWR());
	}

	@Test
	public void testContains() {
		tree.clear();

		tree.addValue('a');
		tree.addValue('g');
		tree.addValue('f');
		tree.addValue('z');
		tree.addValue('k');
		tree.addValue('b');
		tree.addValue('u');
		tree.addValue('l');
		tree.addValue('c');

		Assert.assertEquals(true, tree.containsValue('u'));
		Assert.assertEquals(true, tree.containsValue('c'));
		Assert.assertEquals(false, tree.containsValue('d'));
		Assert.assertEquals(false, tree.containsValue('w'));
	}

	@Test
	public void testSize() {
		tree.clear();

		tree.addValue('a');
		tree.addValue('g');
		tree.addValue('f');
		tree.addValue('z');
		tree.addValue('k');
		tree.addValue('b');
		tree.addValue('u');
		tree.addValue('l');
		tree.addValue('c');

		Assert.assertEquals(9, tree.size());
	}

	@Test
	public void testClear() {
		tree.clear();

		tree.addValue('a');
		tree.addValue('g');
		tree.addValue('f');
		tree.addValue('z');
		tree.addValue('k');
		tree.addValue('b');
		tree.addValue('u');
		tree.addValue('l');
		tree.addValue('c');

		tree.clear();

		Assert.assertEquals(Arrays.asList(), tree.toLRW());
		Assert.assertEquals(-1, tree.getHeight());
		Assert.assertEquals(0, tree.size());
	}

	@Test
	public void testOrders2() {
		tree.clear();
		Random rand = new Random(System.currentTimeMillis());

		for (int i = 0; i < 1000; i++) {
			char c = ((char) rand.nextInt(10000));
			tree.addValue(c);
		}

		List<Character> lwr2 = tree.toLWR();

		Set<Character> set2 = new TreeSet<Character>(lwr2);

		Assert.assertEquals(set2.size(), lwr2.size());
	}

	@Test
	public void testAVLProperty() {
		Tree<Integer> tree = new TreeImpl<Integer>();

		Random rand = new Random(System.currentTimeMillis());

		for (int i = 0; i < 1000; i++) {
			tree.addValue(rand.nextInt(10000));
		}

		Assert.assertEquals(true, isAVLTree(tree.getRoot()));
	}

	private static boolean isAVLTree(Node<?> node) {
		if (node == null)
			return true;

		int leftH = getHeightOfSubtree(node.getLeft());
		int rightH = getHeightOfSubtree(node.getRight());

		if (Math.abs(leftH - rightH) > 1)
			return false;

		return isAVLTree(node.getLeft()) && isAVLTree(node.getRight());
	}

	private static int getHeightOfSubtree(Node<?> node) {
		if (node == null)
			return -1;

		int leftH = getHeightOfSubtree(node.getLeft());
		int rightH = getHeightOfSubtree(node.getRight());

		return 1 + Math.max(leftH, rightH);
	}
}