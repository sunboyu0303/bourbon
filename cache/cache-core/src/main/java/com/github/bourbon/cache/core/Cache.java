package com.github.bourbon.cache.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 18:05
 */
public interface Cache<K, V> {

    V get(K k);

    V get(K k, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

    void put(K k, V v);

    void refresh(K k);
}