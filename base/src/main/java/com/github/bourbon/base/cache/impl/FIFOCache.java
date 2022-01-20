package com.github.bourbon.base.cache.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:35
 */
public class FIFOCache<K, V> extends AbstractCache<K, V> {

    private static final long serialVersionUID = 5868300189498782129L;

    public FIFOCache(int capacity) {
        this(capacity, 0L);
    }

    public FIFOCache(int capacity, long timeout) {
        this.capacity = capacity;
        this.timeout = timeout;
        this.cacheMap = new LinkedHashMap<>(capacity + 1, 1.0F, false);
    }

    protected int pruneCache() {
        int count = 0;
        CacheObj<K, V> first = null;
        Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
        while (values.hasNext()) {
            CacheObj<K, V> co = values.next();
            if (co.isExpired()) {
                values.remove();
                ++count;
            }
            if (first == null) {
                first = co;
            }
        }
        if (isFull() && null != first) {
            cacheMap.remove(first.getKey());
            onRemove(first.getKey(), first.getValue());
            ++count;
        }
        return count;
    }
}