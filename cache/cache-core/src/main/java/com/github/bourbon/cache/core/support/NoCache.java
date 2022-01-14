package com.github.bourbon.cache.core.support;

import com.github.bourbon.cache.core.Cache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 20:21
 */
class NoCache<K, V> implements Cache<K, V> {

    private final com.github.bourbon.base.cache.Cache<K, V> cache = new com.github.bourbon.base.cache.impl.NoCache<>();

    @Override
    public V get(K k) {
        return cache.get(k);
    }

    @Override
    public V get(K k, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return CompletableFuture.supplyAsync(() -> cache.get(k)).get(timeout, unit);
    }

    @Override
    public void put(K k, V v) {
        cache.put(k, v);
    }

    @Override
    public void refresh(K k) {
        throw new UnsupportedOperationException();
    }
}