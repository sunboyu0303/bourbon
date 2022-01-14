package com.github.bourbon.base.lang.cache;

import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 23:54
 */
public class SimpleCache<K, V> implements Iterable<Map.Entry<K, V>> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Map<K, V> cache = MapUtils.newHashMap();

    public V get(K k) {
        lock.readLock().lock();
        try {
            return cache.get(k);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V get(K k, Supplier<V> supplier) {
        return get(k, null, supplier);
    }

    public V get(K k, Predicate<V> p, Supplier<V> s) {
        V v = get(k);
        if (ObjectUtils.isNull(v) && ObjectUtils.nonNull(s)) {
            lock.writeLock().lock();
            try {
                v = cache.get(k);
                if (ObjectUtils.isNull(v) || ObjectUtils.nonNull(p) && !p.test(v)) {
                    v = s.get();
                    put(k, v);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return v;
    }

    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            return cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V computeIfAbsent(K key, Function<K, V> f) {
        lock.writeLock().lock();
        try {
            return cache.computeIfAbsent(key, f);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V remove(K key) {
        lock.writeLock().lock();
        try {
            return cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return cache.entrySet().iterator();
    }
}