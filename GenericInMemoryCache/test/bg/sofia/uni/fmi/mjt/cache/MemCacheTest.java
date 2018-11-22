package bg.sofia.uni.fmi.mjt.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;


public class MemCacheTest {
	private MemCache<String, Integer> cache;
	
	@Before
	public void setUp() {
		cache = new MemCache<>();
	}

    /**
     * Adds the value to the cache and associates it with the key, if both key and
     * value are not null. If the key is already present in the cache, replaces the
     * old value with the currently supplied. If the cache is full but contains at
     * least one expired item, exactly one arbitrary expired item is removed before
     * adding the current one. If the cache is full and there is not a single
     * expired item, CapacityExceededException is thrown
     *
     * @param expiresAt
     *            The point in time after which the item must be considered expired.
     *            If expiresAt is null, the value should never expire.
     * @throws CapacityExceededException
     *             if the key is not present in the cache, the number of items
     *             (before adding the current one) is already equal to the maximum
     *             cache capacity and there is not a single expired item currently
     *             in the cache
     */
	@Test
	public void testSetKeyOrValueNull() {
		cache.set(null, 2, LocalDateTime.now().plusDays(5));
		assertNull(cache.get(null));
		cache.set("sample", null, LocalDateTime.now().plusDays(5));
		assertNull(cache.get("sample"));
		cache.set(null, null, LocalDateTime.now().plusDays(5));
		assertNull(cache.get(null));
	}
	@Test
	public void replaceOldValue() {
		cache.set("asdf", 2,  LocalDateTime.now().plusDays(5) );
		assertEquals(Integer.valueOf(2),cache.get("asdf"));
		cache.set("asdf", 3, LocalDateTime.now().plusDays(5) );
		assertEquals(Integer.valueOf(3),cache.get("asdf"));
	}
	@Test(expected=CapacityExceededException.class)
	public void testFullCacheThrowsCapacityException() {
		cache = new MemCache<>(2);
		cache.set("sample", 2,  null );
		cache.set("sample2", 3,  null );
		cache.set("sample3", 4,  null);
		
	}
	@Test
	public void testCapacityWhenContainingExpired() {
		cache = new MemCache<>(2);
		cache.set("sample", 3,  LocalDateTime.now().plusDays(5) );
		cache.set("expired", 2,  LocalDateTime.now().minusDays(5) );
		cache.set("successful", 5, LocalDateTime.now().plusDays(5));
		assertNotNull(cache.get("successful"));
	}
	@Test
	public void testNullExpiration() {
		
		cache.set("sample", 2,  null);
		assertNotNull(cache.get("sample"));
	}
	@Test
	public void testZeroCapacity() {
		cache = new MemCache<>(0);
		cache.set("s", 1, null);
		assertNull(cache.get("s"));
	}
	 /** -----------------------------------GET-------------------------------------
     * Returns the value associated with the key, if it is present in the cache and
     * is not expired, or null otherwise. If the value is present in the cache but
     * is expired, it is also removed from the cache
     */
	@Test
	public void testGetNonPresent() {
		assertNull(cache.get("sampleKey"));
	}
	@Test
	public void testGetExpiredReturnsNull() {
		cache.set("expired", 2, LocalDateTime.MIN);
		assertNull(cache.get("expired"));
	}
	
	/**
     * Removes the item associated with the specified key from the cache. Returns
     * true, if an item with the specified key was found and false otherwise.
     */
	@Test
	public void testRemoveExisting() {
		cache.set("existing", 2, LocalDateTime.MAX);
		assertTrue(cache.remove("existing"));
	}
	@Test
	public void testRemoveNonExisting() {
		cache.set("existing", 2, LocalDateTime.MAX);
		assertFalse(cache.remove("notExisting"));
	}
	@Test
	public void testCacheSize() {
		cache.set("existing", 2, LocalDateTime.MAX);
		cache.set("expired", 3, LocalDateTime.MIN);
		cache.set("expired2", 4, LocalDateTime.MIN);
		assertEquals(3, cache.size());
	}
	
	 /** --------------------------------------- HIT RATE ----------------------------------------
     * Return the percentage of the successful hits for this cache. It is a
     * double-precision number in the interval [0.0, 1.0] and is equal to the ratio
     * of get(K, V) calls that returned a non-null value versus the calls that
     * returned null. If there is not a single successful hit, the hit rate is 0.0.
     * If there is at least one successful hit and the missed hits are zero, the hit
     * rate is 1.0
     */
	
	
	@Test
	public void testEmptyCacheHitRate() {
		assertEquals(0, cache.getHitRate(),0.01);
	}
	
	@Test
	public void testExpiredSingleHit() {
		cache.set("sample", 3, LocalDateTime.MIN);
		cache.get("sample");
		assertEquals(0, cache.getHitRate(),0.01);
	}
	
	@Test
	public void testSingleSuccessfulHit() {
		cache.set("sample",3,null);
		cache.get("sample");
		assertEquals(1, cache.getHitRate(),0.01);
	}
	@Test
	public void testMultipleSuccessfulHits() {
		cache.set("sample",3,null);
		cache.set("sample2",4,null);
		cache.set("sample3",5,null);
		cache.get("sample2");
		cache.get("sample");
		cache.get("sample3");
		assertEquals(1, cache.getHitRate(),0.01);
	}
	@Test
	public void testHitRate() {
		cache.set("sample",3,null);
		cache.set("sample2",4,null);
		cache.set("sample3",5,null);
		//Successful hits
		cache.get("sample2");
		cache.get("sample");
		//cache.get("sample3");
		//Unsuccessful hits
		cache.get("Unsuccessful");
		cache.get("Unsuccessful");
		cache.get("Unsuccessful");
		
		assertEquals(0.4, cache.getHitRate(),0.01);
		
		cache.get("Unsuccessful");
		assertEquals(0.33, cache.getHitRate(),0.01);
		}
	@Test
	public void testHitRateOneMiss() {
		cache.set("sample",3,null);
		cache.set("sample2",4,null);
		cache.set("sample3",5,null);
		cache.get("sample2");
		cache.get("sample");
		cache.get("sample2");
		cache.get("sample");
		cache.get("Unsuccessful");
		assertEquals(0.8, cache.getHitRate(),0.01);
	}
	@Test
	public void testClearCache() {
		cache.set("sample",3,null);
		cache.set("sample2",4,LocalDateTime.MIN);
		cache.clear();
		assertEquals(0, cache.getHitRate(),0.01);
		assertNull(cache.get("sample"));
		assertNull(cache.get("sample2"));
	}
	
	@Test
	public void testGetExpiration() {
		LocalDateTime now = LocalDateTime.now();
		cache.set("sample2",4,now);
		assertNull(cache.getExpiration("nonexisting"));
		assertEquals(now,cache.getExpiration("sample2"));
	}
}









