package ab3.test;


import ab3.AuDHashMap;
import ab3.impl.Eberl.AuDHashMapImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;


public class AuDHashMapImplSpec {

    @Test
    public void aDefaultConstructedHashMapHasSpaceForThirtySevenElements() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // then
        Assert.assertEquals(37, target.size());
        Assert.assertEquals(0, target.elementCount());
    }

    @Test
    public void aDefaultConstructedHashMapIsNotFull() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // then
        Assert.assertFalse(target.isFull());
    }

    @Test
    public void aDefaultConstructedHashMapIsEmpty() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // then
        Assert.assertTrue(target.isEmpty());
    }

    @Test
    public void aDefaultConstructedHashMapGivesAnEmptyKeySet() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // then
        Assert.assertTrue(target.keySet().isEmpty());
    }

    @Test
    public void aDefaultConstructedHashMapGivesAnEmptyValueCollection() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // then
        Assert.assertTrue(target.values().isEmpty());
    }

    @Test
    public void setMapSizeSetsTheHashMapsSizeToTheSameSizeIfGivenSizeIsAPrimeNumber() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // when
        target.setMapSize(17);

        // then
        Assert.assertEquals(17, target.size());

        // when
        target.setMapSize(31);

        // then
        Assert.assertEquals(31, target.size());
    }

    @Test
    public void setMapSizeSetsTheHashMapsSizeToTheNextGreaterPrimeNumber() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // when
        target.setMapSize(16);

        // then
        Assert.assertEquals(17, target.size());

        // when
        target.setMapSize(30);

        // then
        Assert.assertEquals(31, target.size());
    }

    @Test
    public void setMapSizeAlsoWorksWithNegativeValuesOrZero() {

        // given
        AuDHashMap<Object, Object> target = new AuDHashMapImpl<>();

        // when
        target.setMapSize(0);

        // then
        Assert.assertEquals(2, target.size());

        // when
        target.setMapSize(-1);

        // then
        Assert.assertEquals(2, target.size());

        // when
        target.setMapSize(-100);

        // then
        Assert.assertEquals(2, target.size());
    }

    @Test
    public void setMapSizeRecreatesTheHashMapAndRemovesAnyExistingEntry() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");
        target.put("Hallo", "Klagenfurt");

        // when
        target.setMapSize(17);

        // then
        Assert.assertTrue(target.isEmpty());
        Assert.assertEquals(0, target.elementCount());
    }

    @Test
    public void puttingAKeyValuePairIntoTheMapReturnsNullIfNoKeyExisted() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();

        // when
        String obtained = target.put("Hello", "World");

        // then
        Assert.assertNull(obtained);
    }

    @Test
    public void puttingANewValueForAlreadyExistingKeyReturnsPreviousValue() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");

        // when
        String obtained = target.put("Hello", "Klagenfurt");

        // then
        Assert.assertEquals("World", obtained);
    }

    @Test
    public void keySetGetsBackASetContainingAllPreviouslyInsertedKeys() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");

        // when
        Set<String> obtained = target.keySet();

        // then
        Assert.assertEquals(1, obtained.size());
        Assert.assertTrue(obtained.contains("Hello"));

        // and when adding a new entry
        target.put("Hallo", "Welt");
        obtained = target.keySet();

        // then
        Assert.assertEquals(2, obtained.size());
        Assert.assertTrue(obtained.containsAll(Arrays.asList("Hello", "Hallo")));
    }

    @Test
    public void valueGetsBackACollectionContainingAllPreviouslyInsertedValue() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");

        // when
        Collection<String> obtained = target.values();

        // then
        Assert.assertEquals(1, obtained.size());
        Assert.assertTrue(obtained.contains("World"));

        // and when adding a new entry
        target.put("Hallo", "Welt");
        obtained = target.values();

        // then
        Assert.assertEquals(2, obtained.size());
        Assert.assertTrue(obtained.containsAll(Arrays.asList("World", "Welt")));
    }

    @Test
    public void elementCountReturnsTheNumberOfStoredElementsInTheHashMap() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.setMapSize(5);

        // then
        Assert.assertEquals(0, target.elementCount());

        // when
        target.put("H", "H");
        target.put("e", "e");

        // then
        Assert.assertEquals(2, target.elementCount());

        // and when adding the next key/value pairs
        target.put("l", "l");
        target.put("o", "o");

        // then
        Assert.assertEquals(4, target.elementCount());

        // and when adding another key value pair
        target.put("!", "!");

        // then
        Assert.assertEquals(5, target.elementCount());
    }

    @Test
    public void aHashMapIsNoLongerEmptyIfAtLeastOneElementIsIn() {

        // given
        AuDHashMap<Integer, Integer> target = new AuDHashMapImpl<>();

        // then
        Assert.assertTrue(target.isEmpty());

        // and when adding a single element
        target.put(1, 100);

        // then
        Assert.assertFalse(target.isEmpty());
    }

    @Test
    public void aHashMapIsFullIfItDoesNotHaveSpaceForAnyFurtherElement() {

        // given
        AuDHashMap<Integer, Integer> target = new AuDHashMapImpl<>();
        target.setMapSize(5);

        for (int i = 0; i < target.size(); i++)
            target.put(i, i * 2);

        // then
        Assert.assertTrue(target.isFull());
    }

    @Test(expected = IllegalStateException.class)
    public void puttingAnEntryIntoAnAlreadyFullHashMapThrowsAnException() {

        // given
        AuDHashMap<Integer, Integer> target = new AuDHashMapImpl<>();
        target.setMapSize(5);

        for (int i = 0; i < target.size(); i++)
            target.put(i, i * 2);

        // then
        Assert.assertTrue(target.isFull());

        // and when
        target.put(666, 42);
    }

    @Test
    public void clearingAHashMapRemovesAllEntries() {

        // given
        AuDHashMap<Integer, Integer> target = new AuDHashMapImpl<>();
        target.setMapSize(5);

        for (int i = 0; i < target.size(); i++)
            target.put(i, i * 2);

        // then
        Assert.assertFalse(target.isEmpty());

        // and when
        target.clear();

        // then
        Assert.assertTrue(target.isEmpty());
        Assert.assertEquals(0, target.elementCount());
        Assert.assertEquals(5, target.size());
        Assert.assertTrue(target.keySet().isEmpty());
        Assert.assertTrue(target.values().isEmpty());
    }

    @Test
    public void containsKeyGivesABooleanIndicatingWhetherTheKeyExistsOrNot() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");
        target.put("foo", "bar");

        // then
        Assert.assertTrue(target.containsKey("Hello"));
        Assert.assertTrue(target.containsKey("foo"));

        Assert.assertFalse(target.containsKey("asdf"));
        Assert.assertFalse(target.containsKey("World"));
        Assert.assertFalse(target.containsKey("bar"));
    }

    @Test
    public void containsValueGivesABooleanIndicatingWhetherTheValueExistsOrNot() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");
        target.put("foo", "bar");

        // then
        Assert.assertTrue(target.containsValue("World"));
        Assert.assertTrue(target.containsValue("bar"));

        Assert.assertFalse(target.containsValue("asdf"));
        Assert.assertFalse(target.containsValue("Hello"));
        Assert.assertFalse(target.containsValue("foo"));
    }

    @Test
    public void getReturnsTheValueIfKeyExists() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");
        target.put("foo", "bar");

        // then
        Assert.assertEquals("World", target.get("Hello"));
        Assert.assertEquals("bar", target.get("foo"));
    }

    @Test
    public void getReturnsNullIfKeyIsNotFound() {

        // given
        AuDHashMap<String, String> target = new AuDHashMapImpl<>();
        target.put("Hello", "World");
        target.put("foo", "bar");

        // then
        Assert.assertNull(target.get("World"));
        Assert.assertNull(target.get("bar"));
    }

    @Test
    public void hashMapAlsoWorksIfImplementedHashCodeIsCrap() {

        // given
        // a class with a really crappy hash code impl.
        class CrappyHashCode {
            @Override
            public int hashCode() {
                return 31;
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        }

        AuDHashMap<CrappyHashCode, Integer> target = new AuDHashMapImpl<>();
        target.setMapSize(5);

        CrappyHashCode instanceOne = new CrappyHashCode();
        CrappyHashCode instanceTwo = new CrappyHashCode();
        CrappyHashCode instanceThree = new CrappyHashCode();
        CrappyHashCode instanceFour = new CrappyHashCode();
        CrappyHashCode instanceFive = new CrappyHashCode();
        CrappyHashCode instanceSix = new CrappyHashCode();

        target.put(instanceOne, 1);
        target.put(instanceTwo, 2);
        target.put(instanceThree, 3);
        target.put(instanceFour, 4);
        target.put(instanceFive, 5);

        // then
        Assert.assertEquals(5, target.elementCount());

        // when/then
        try {
            target.put(instanceSix, 6);
            Assert.fail("adding something to full hash map");
        } catch (IllegalStateException e) {
            // intentionally empty
        }

        // then
        Assert.assertTrue(target.containsKey(instanceOne));
        Assert.assertTrue(target.containsKey(instanceTwo));
        Assert.assertTrue(target.containsKey(instanceThree));
        Assert.assertTrue(target.containsKey(instanceFour));
        Assert.assertTrue(target.containsKey(instanceFive));
        Assert.assertFalse(target.containsKey(instanceSix));

        Assert.assertEquals(Integer.valueOf(1), target.get(instanceOne));
        Assert.assertEquals(Integer.valueOf(2), target.get(instanceTwo));
        Assert.assertEquals(Integer.valueOf(3), target.get(instanceThree));
        Assert.assertEquals(Integer.valueOf(4), target.get(instanceFour));
        Assert.assertEquals(Integer.valueOf(5), target.get(instanceFive));
        Assert.assertNull(target.get(instanceSix));

        // and when
        Integer obtained = target.put(instanceOne, 10);

        // then
        Assert.assertEquals(Integer.valueOf(1), obtained);

        // and when
        obtained = target.put(instanceFive, 50);

        // then
        Assert.assertEquals(Integer.valueOf(5), obtained);
    }

    @Test
    public void testGetSomeMorePoints() {

        // given
        AuDHashMap<String, Integer> target = new AuDHashMapImpl<>();
        target.setMapSize(52);

        // when
        for (int i = 0; i < 26; i++) {
            Assert.assertNull(target.put(new StringBuilder().append((char)(i + 'a')).toString(), i));
        }

        // then
        Assert.assertEquals(26, target.keySet().size());
        Assert.assertEquals(26, target.elementCount());

        // and when
        for (int i = 0; i < 26; i++) {
            Assert.assertNotNull(target.put(new StringBuilder().append((char)(i + 'a')).toString(), i));
        }

        // then
        Assert.assertEquals(26, target.keySet().size());
        Assert.assertEquals(26, target.elementCount());
    }
}
