package com.github.bourbon.cache.core;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 18:05
 */
public interface Cache<K, V> {

    V get(K k);

    CompletableFuture<V> getAsync(K k);

    void put(K k, V v);

    void refresh(K k);
}