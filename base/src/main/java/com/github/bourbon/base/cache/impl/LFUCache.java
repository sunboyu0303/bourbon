package com.github.bourbon.base.cache.impl;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:36
 */
public class LFUCache<K, V> extends AbstractCache<K, V> {

    private static final long serialVersionUID = 6776132729113358577L;

    public LFUCache(int capacity) {
        this(capacity, 0L);
    }

    public LFUCache(int capacity, long timeout) {
        if (Integer.MAX_VALUE == capacity) {
            --capacity;
        }
        this.capacity = capacity;
        this.timeout = timeout;
        this.cacheMap = new HashMap<>(capacity + 1, 1.0F);
    }

    @Override
    protected int pruneCache() {
        int count = 0;
        CacheObj<K, V> comin = null;
        Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
        while (values.hasNext()) {
            CacheObj<K, V> co = values.next();
            if (co.isExpired()) {
                values.remove();
                onRemove(co.getKey(), co.getValue());
                ++count;
            } else if (comin == null || co.getAccessCount() < comin.getAccessCount()) {
                comin = co;
            }
        }
        if (isFull() && comin != null) {
            long minAccessCount = comin.getAccessCount();
            values = cacheMap.values().iterator();
            while (values.hasNext()) {
                CacheObj<K, V> co1 = values.next();
                if (co1.addAndGetAccessCount(-minAccessCount) <= 0L) {
                    values.remove();
                    onRemove(co1.getKey(), co1.getValue());
                    ++count;
                }
            }
        }
        return count;
    }
}