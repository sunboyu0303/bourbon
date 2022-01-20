package com.github.bourbon.base.lang.cache;

import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 16:21
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -3337809725722845047L;

    private final Lock lock = new ReentrantLock();

    private final int maxCapacity;

    private transient PreCache<K, V> preCache;

    public LRUCache() {
        this(IntConstants.KB);
    }

    public LRUCache(int maxCapacity) {
        super(16, 0.75f, true);
        this.maxCapacity = maxCapacity;
        this.preCache = new PreCache<>(maxCapacity);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            lock.lock();
            return preCache.containsKey(key) || super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        try {
            lock.lock();
            return ObjectUtils.defaultSupplierIfNull(preCache.get(key), () -> super.get(key));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            if (preCache.containsKey(key)) {
                preCache.remove(key);
                return super.put(key, value);
            } else {
                preCache.put(key, value);
                return value;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        try {
            lock.lock();
            preCache.remove(key);
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.lock();
            return preCache.size() + super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            preCache.clear();
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    private static class PreCache<K, V> extends LinkedHashMap<K, V> implements Serializable {
        private static final long serialVersionUID = 6504486498471235491L;
        private final int maxCapacity;

        private PreCache(int maxCapacity) {
            super(16, 0.75f, true);
            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return super.size() > maxCapacity;
        }
    }
}