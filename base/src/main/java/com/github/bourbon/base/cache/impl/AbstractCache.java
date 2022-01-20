package com.github.bourbon.base.cache.impl;

import com.github.bourbon.base.cache.Cache;
import com.github.bourbon.base.cache.CacheListener;
import com.github.bourbon.base.utils.ListUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:02
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected Map<K, CacheObj<K, V>> cacheMap;
    protected final StampedLock lock = new StampedLock();
    protected final Map<K, Lock> keyLockMap = new ConcurrentHashMap<>();
    protected int capacity;
    protected long timeout;
    protected boolean existCustomTimeout;
    protected LongAdder hitCount = new LongAdder();
    protected LongAdder missCount = new LongAdder();
    protected CacheListener<K, V> listener;

    @Override
    public void put(K key, V object) {
        put(key, object, timeout);
    }

    @Override
    public void put(K key, V object, long timeout) {
        long stamp = lock.writeLock();
        try {
            putWithoutLock(key, object, timeout);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    private void putWithoutLock(K key, V object, long timeout) {
        CacheObj<K, V> co = new CacheObj<>(key, object, timeout);
        if (timeout != 0L) {
            existCustomTimeout = true;
        }
        if (isFull()) {
            pruneCache();
        }
        cacheMap.put(key, co);
    }

    @Override
    public boolean containsKey(K key) {
        long stamp = lock.readLock();
        try {
            CacheObj<K, V> co = cacheMap.get(key);
            if (co == null) {
                return false;
            }
            if (!co.isExpired()) {
                return true;
            }
        } finally {
            lock.unlockRead(stamp);
        }
        remove(key, true);
        return false;
    }

    public long getHitCount() {
        return hitCount.sum();
    }

    public long getMissCount() {
        return missCount.sum();
    }

    @Override
    public V get(K key, boolean isUpdateLastAccess, Supplier<V> supplier) {
        V v = get(key, isUpdateLastAccess);
        if (null == v && null != supplier) {
            Lock keyLock = keyLockMap.computeIfAbsent(key, k -> new ReentrantLock());
            try {
                keyLock.lock();
                CacheObj<K, V> co = cacheMap.get(key);
                if (null != co && !co.isExpired()) {
                    v = co.getValue(isUpdateLastAccess);
                } else {
                    try {
                        v = supplier.get();
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                    put(key, v, timeout);
                }
            } finally {
                keyLock.unlock();
                keyLockMap.remove(key);
            }
        }
        return v;
    }

    @Override
    public V get(K key, boolean isUpdateLastAccess) {
        long stamp = lock.tryOptimisticRead();
        CacheObj<K, V> co = cacheMap.get(key);
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                co = cacheMap.get(key);
            } finally {
                lock.unlockRead(stamp);
            }
        }
        if (null == co) {
            missCount.increment();
            return null;
        }
        if (!co.isExpired()) {
            hitCount.increment();
            return co.getValue(isUpdateLastAccess);
        }
        remove(key, true);
        return null;
    }

    @Override
    public Iterator<V> iterator() {
        return new CacheValuesIterator<>((CacheObjIterator<K, V>) cacheObjIterator());
    }

    @Override
    public Iterator<CacheObj<K, V>> cacheObjIterator() {
        long stamp = lock.readLock();
        try {
            return new CacheObjIterator<>(ListUtils.newArrayList(cacheMap.values().iterator()).iterator());
        } finally {
            lock.unlockRead(stamp);
        }
    }

    protected abstract int pruneCache();

    @Override
    public final int prune() {
        long stamp = lock.writeLock();
        try {
            return pruneCache();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public long timeout() {
        return timeout;
    }

    protected boolean isPruneExpiredActive() {
        return timeout != 0L || existCustomTimeout;
    }

    @Override
    public boolean isFull() {
        return capacity > 0 && cacheMap.size() >= capacity;
    }

    @Override
    public void remove(K key) {
        remove(key, false);
    }

    @Override
    public void clear() {
        long stamp = lock.writeLock();
        try {
            cacheMap.clear();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    @Override
    public String toString() {
        return cacheMap.toString();
    }

    @Override
    public AbstractCache<K, V> setListener(CacheListener<K, V> listener) {
        this.listener = listener;
        return this;
    }

    public Set<K> keySet() {
        return cacheMap.keySet();
    }

    protected void onRemove(K key, V cachedObject) {
        if (null != listener) {
            listener.onRemove(key, cachedObject);
        }
    }

    private void remove(K key, boolean withMissCount) {
        long stamp = lock.writeLock();
        CacheObj<K, V> co;
        try {
            co = removeWithoutLock(key, withMissCount);
        } finally {
            lock.unlockWrite(stamp);
        }
        if (null != co) {
            onRemove(co.getKey(), co.getValue());
        }
    }

    private CacheObj<K, V> removeWithoutLock(K key, boolean withMissCount) {
        CacheObj<K, V> co = cacheMap.remove(key);
        if (withMissCount) {
            missCount.increment();
        }
        return co;
    }
}