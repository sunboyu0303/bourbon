package com.github.bourbon.cache.core.support;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.cache.core.Cache;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 20:21
 */
class NoCache<K, V> implements Cache<K, V> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public V get(K k) {
        if (logger.isInfoEnabled()) {
            logger.info("get key: {}", k);
        }
        return null;
    }

    @Override
    public CompletableFuture<V> getAsync(K k) {
        return CompletableFuture.supplyAsync(() -> get(k));
    }

    @Override
    public void put(K k, V v) {
        if (logger.isInfoEnabled()) {
            logger.info("put key: {}, value: {}", k, v);
        }
    }

    @Override
    public void refresh(K k) {
        throw new UnsupportedOperationException();
    }
}