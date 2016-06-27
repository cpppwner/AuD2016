package ab3.test;


import org.junit.Assert;
import org.junit.Test;

import ab3.AuDHashMap;
import ab3.impl.Eberl.AuDHashMapImpl;


public class HashMapTest {

	private AuDHashMap<Integer, String> hashMap = new AuDHashMapImpl<Integer, String> ();
	
	@Test
	public void testHashMap()
	{
		hashMap.setMapSize(1000);
		
		//Erste Primzahl ist 1009
		Assert.assertEquals(1009, hashMap.size());
		
		Assert.assertEquals(false, hashMap.isFull());
		
		Assert.assertEquals(true, hashMap.isEmpty());
		
		
		hashMap.put(1, "Erster String");
		hashMap.put(2, "Zweiter String");
		hashMap.put(1000, "Dritter String");
		
		
		Assert.assertEquals(false, hashMap.isEmpty());
		
		Assert.assertEquals("Erster String", hashMap.get(1));
		
		Assert.assertEquals(null, hashMap.get(10));
	}

}