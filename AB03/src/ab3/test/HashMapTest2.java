package ab3.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ab3.AuDHashMap;
import ab3.impl.Eberl.AuDHashMapImpl;

public class HashMapTest2 {

	private AuDHashMap<Integer, String> hashMap = new AuDHashMapImpl<Integer, String>();

	@Test
	public void testInsertionsBasic() {

		hashMap = new AuDHashMapImpl<Integer, String>();
		hashMap.setMapSize(7);

		hashMap.put(1, "1. ");
		hashMap.put(2, "2. ");
		hashMap.put(9, "3. ");
		printer();

	}

	@Test
	public void testFirstInsertionConflict() {
		hashMap = new AuDHashMapImpl<Integer, String>();
		hashMap.setMapSize(7);

		hashMap.put(1, "1. ");
		hashMap.put(2, "2. ");
		hashMap.put(9, "3. ");
		hashMap.put(8, "4. ");
		printer();
	}

	@Test
	public void testChangeSizeToPrim() {
		hashMap = new AuDHashMapImpl<Integer, String>();
		hashMap.setMapSize(294);

		// Erste Primzahl ist 307
		Assert.assertEquals(307, hashMap.size());
	}

	@Test
	public void testFillHashMapCompletely() {
		hashMap = new AuDHashMapImpl<Integer, String>();
		hashMap.setMapSize(7);

		hashMap.put(1, "1. ");
		hashMap.put(2, "2. ");
		hashMap.put(9, "3. ");
		hashMap.put(8, "4. ");
		hashMap.put(15, "5. ");
		hashMap.put(22, "6. ");
		hashMap.put(3, "7. ");

		Assert.assertEquals("1. ", hashMap.get(1));

		printer();
	}

	@Test(expected = IllegalStateException.class)
	public void testHashMapAll9() {
		hashMap = new AuDHashMapImpl<Integer, String>();
		hashMap.setMapSize(6);

		// Erste Primzahl ist 7
		Assert.assertEquals(7, hashMap.size());

		Assert.assertEquals(false, hashMap.isFull());

		Assert.assertEquals(true, hashMap.isEmpty());

		hashMap.put(1, "1. ");
		hashMap.put(2, "2. ");
		hashMap.put(9, "3. ");
		hashMap.put(8, "4. ");
		hashMap.put(15, "5. ");
		hashMap.put(22, "6. ");
		hashMap.put(3, "7. ");

		Assert.assertEquals(false, hashMap.isEmpty());
		Assert.assertEquals("1. ", hashMap.get(1));
		Assert.assertEquals("6. ", hashMap.get(22));
		Assert.assertEquals(7, hashMap.elementCount());
		Assert.assertEquals(true, hashMap.containsKey(1));
		Assert.assertEquals(true, hashMap.containsKey(15));
		Assert.assertEquals(false, hashMap.containsKey(7));

		printer();

		// sollte voll sein
		Assert.assertEquals(true, hashMap.isFull());
		// Einen Wert zu viel einfügen
		hashMap.put(10, "8. String");
	}

	private void printer() {
		Collection<String> values = hashMap.values();
		Set<Integer> keys = hashMap.keySet();
		System.out.println(values.size());
		System.out.print("Keys:    ");
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			// System.out.print(" "+keys.iterator().next());
			System.out.print(" " + iterator.next() + " ");
		}
		System.out.println("");
		System.out.print("Values: ");
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			// System.out.print(" "+keys.iterator().next());
			System.out.print(" " + iterator.next());
		}
		System.out.println("");

	}

}