package com.pfe.security.validation.cache;

import com.pfe.security.validation.error.TokenValidatorException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Map with cache,
 *
 * @author al.sghaier
 */
public class MapCache<K, V> {

  private Function<Long, Boolean> isAlive = deathTime -> deathTime - System.currentTimeMillis() > 0;
  private ConcurrentHashMap<K, Object[]> cache = new ConcurrentHashMap<>();
  private ScheduledExecutorService executor;
  private final long timeToLiveSeconds;

  public MapCache(long timeToLiveSeconds) {
    this.timeToLiveSeconds = timeToLiveSeconds * 1000;

    Runnable periodicTask = this::clearCache;
    this.executor = Executors.newSingleThreadScheduledExecutor();
    this.executor.scheduleAtFixedRate(periodicTask, 0, timeToLiveSeconds + 1, TimeUnit.SECONDS);
  }

  private void clearCache() {
    this.cache.entrySet()
        .removeIf(kEntry -> {
          Long deathTime = (Long) kEntry.getValue()[1];
          return !this.isAlive.apply(deathTime);
        });
  }

  /**
   * Put the object
   *
   * @param key   - The key to associate with the cache
   * @param value - The actual value to store in the cache
   */
  public void put(K key, V value) {
    long deathTime = System.currentTimeMillis() + this.timeToLiveSeconds;
    if (key == null) {
      throw new TokenValidatorException("Key cannot be null!");
    }
    this.cache.put(key, new Object[]{value, deathTime});
  }

  /**
   * Returns the Object in the cache that is associated with passed key, or NULL if no value is
   * associated with the key
   *
   * @param key - The key associated with the value to retrieve
   */
  public V get(K key) {
    if (this.cache.containsKey(key)) {
      Long deathTime = (Long) this.cache.get(key)[1];
      if (Boolean.TRUE.equals(this.isAlive.apply(deathTime))) {
        return (V) this.cache.get(key)[0];
      } else {
        remove(key);
      }
    }
    return null;
  }

  public boolean remove(K key) {
    return removeAndGet(key) != null;
  }

  private V removeAndGet(K key) {
    Object[] entry = this.cache.remove(key);
    if (entry != null) {
      return (V) entry[0];
    }
    return null;
  }
}
