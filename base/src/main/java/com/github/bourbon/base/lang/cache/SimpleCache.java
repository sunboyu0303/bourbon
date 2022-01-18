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
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private final Map<K, V> cache = MapUtils.newHashMap();

    public V get(K k) {
        try {
            readLock.lock();
            return cache.get(k);
        } finally {
            readLock.unlock();
        }
    }

    public V get(K k, Supplier<V> supplier) {
        return get(k, null, supplier);
    }

    public V get(K k, Predicate<V> p, Supplier<V> s) {
        V v = get(k);
        if (ObjectUtils.isNull(v) && ObjectUtils.nonNull(s)) {
            try {
                writeLock.lock();
                v = cache.get(k);
                if (ObjectUtils.isNull(v) || ObjectUtils.nonNull(p) && !p.test(v)) {
                    v = s.get();
                    put(k, v);
                }
            } finally {
                writeLock.unlock();
            }
        }
        return v;
    }

    public V put(K key, V value) {
        try {
            writeLock.lock();
            return cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public V computeIfAbsent(K key, Function<K, V> f) {
        try {
            writeLock.lock();
            return cache.computeIfAbsent(key, f);
        } finally {
            writeLock.unlock();
        }
    }

    public V remove(K key) {
        try {
            writeLock.lock();
            return cache.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        try {
            writeLock.lock();
            cache.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return cache.entrySet().iterator();
    }
}