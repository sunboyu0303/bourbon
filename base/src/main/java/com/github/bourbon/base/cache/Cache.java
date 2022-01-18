package com.github.bourbon.base.cache;

import com.github.bourbon.base.cache.impl.CacheObj;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 17:49
 */
public interface Cache<K, V> extends Iterable<V> {

    int capacity();

    long timeout();

    void put(K k, V v);

    void put(K k, V v, long timeout);

    default V get(K k) {
        return get(k, true);
    }

    default V get(K k, Supplier<V> s) {
        return get(k, true, s);
    }

    V get(K k, boolean b, Supplier<V> s);

    V get(K k, boolean b);

    Iterator<CacheObj<K, V>> cacheObjIterator();

    int prune();

    boolean isFull();

    void remove(K k);

    void clear();

    int size();

    boolean isEmpty();

    boolean containsKey(K k);

    default Cache<K, V> setListener(CacheListener<K, V> listener) {
        return this;
    }
}