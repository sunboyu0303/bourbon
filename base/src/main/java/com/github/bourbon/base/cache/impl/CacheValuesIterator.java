package com.github.bourbon.base.cache.impl;

import java.util.Iterator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:38
 */
public class CacheValuesIterator<V> implements Iterator<V> {
    private final CacheObjIterator<?, V> cacheObjIterator;

    CacheValuesIterator(CacheObjIterator<?, V> iterator) {
        cacheObjIterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return cacheObjIterator.hasNext();
    }

    @Override
    public V next() {
        return cacheObjIterator.next().getValue();
    }

    @Override
    public void remove() {
        cacheObjIterator.remove();
    }
}