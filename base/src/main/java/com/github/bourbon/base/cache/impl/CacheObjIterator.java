package com.github.bourbon.base.cache.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:36
 */
public class CacheObjIterator<K, V> implements Iterator<CacheObj<K, V>> {

    private final Iterator<CacheObj<K, V>> iterator;
    private CacheObj<K, V> nextValue;

    CacheObjIterator(Iterator<CacheObj<K, V>> iterator) {
        this.iterator = iterator;
        nextValue();
    }

    @Override
    public boolean hasNext() {
        return nextValue != null;
    }

    @Override
    public CacheObj<K, V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        CacheObj<K, V> cachedObject = nextValue;
        nextValue();
        return cachedObject;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cache values Iterator is not support to modify.");
    }

    private void nextValue() {
        while (true) {
            if (iterator.hasNext()) {
                nextValue = iterator.next();
                if (nextValue.isExpired()) {
                    continue;
                }
                return;
            }
            nextValue = null;
            return;
        }
    }
}