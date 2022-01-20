package com.github.bourbon.base.cache.impl;

import java.util.Iterator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:31
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {
    private static final long serialVersionUID = 7154684998513273134L;

    public LRUCache(int capacity) {
        this(capacity, 0L);
    }

    public LRUCache(int capacity, long timeout) {
        if (Integer.MAX_VALUE == capacity) {
            --capacity;
        }
        this.capacity = capacity;
        this.timeout = timeout;
        this.cacheMap = new FixedLinkedHashMap<>(capacity);
    }

    @Override
    protected int pruneCache() {
        if (!isPruneExpiredActive()) {
            return 0;
        }
        int count = 0;
        Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
        while (values.hasNext()) {
            CacheObj<K, V> co = values.next();
            if (co.isExpired()) {
                values.remove();
                onRemove(co.getKey(), co.getValue());
                ++count;
            }
        }
        return count;
    }
}