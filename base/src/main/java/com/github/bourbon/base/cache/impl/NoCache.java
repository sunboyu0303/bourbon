package com.github.bourbon.base.cache.impl;

import com.github.bourbon.base.cache.Cache;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:48
 */
public class NoCache<K, V> implements Cache<K, V> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public long timeout() {
        return 0L;
    }

    @Override
    public void put(K key, V value) {
        if (logger.isInfoEnabled()) {
            logger.info("put key: {}, value: {}", key, value);
        }
    }

    @Override
    public void put(K key, V value, long timeout) {
        if (logger.isInfoEnabled()) {
            logger.info("put key: {}, value: {}, timeout: {}", key, value, timeout);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public V get(K key) {
        if (logger.isInfoEnabled()) {
            logger.info("get key: {}", key);
        }
        return null;
    }

    @Override
    public V get(K key, boolean isUpdateLastAccess) {
        return null;
    }

    @Override
    public V get(K key, Supplier<V> supplier) {
        return get(key, true, supplier);
    }

    @Override
    public V get(K key, boolean isUpdateLastAccess, Supplier<V> supplier) {
        try {
            return ObjectUtils.defaultIfNull(supplier, Supplier::get);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public V next() {
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public Iterator<CacheObj<K, V>> cacheObjIterator() {
        return null;
    }

    @Override
    public int prune() {
        return 0;
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public void remove(K key) {
        if (logger.isInfoEnabled()) {
            logger.info("remove key: {}", key);
        }
    }

    @Override
    public void clear() {
        if (logger.isInfoEnabled()) {
            logger.info("clear");
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}