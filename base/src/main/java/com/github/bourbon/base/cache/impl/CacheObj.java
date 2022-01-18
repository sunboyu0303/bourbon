package com.github.bourbon.base.cache.impl;

import com.github.bourbon.base.lang.SystemClock;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:00
 */
public class CacheObj<K, V> {

    private final K k;
    private final V v;
    private volatile long lastAccess;
    private AtomicLong accessCount = new AtomicLong();
    private final long ttl;

    CacheObj(K k, V v, long ttl) {
        this.k = k;
        this.v = v;
        this.ttl = ttl;
        this.lastAccess = SystemClock.currentTimeMillis();
    }

    K getKey() {
        return k;
    }

    V getValue() {
        return getValue(true);
    }

    V getValue(boolean isUpdateLastAccess) {
        if (isUpdateLastAccess) {
            lastAccess = SystemClock.currentTimeMillis();
        }
        accessCount.getAndIncrement();
        return v;
    }

    long addAndGetAccessCount(long accessCount) {
        return this.accessCount.addAndGet(accessCount);
    }

    long getAccessCount() {
        return accessCount.get();
    }

    boolean isExpired() {
        return ttl > 0L && SystemClock.currentTimeMillis() - lastAccess > ttl;
    }
}