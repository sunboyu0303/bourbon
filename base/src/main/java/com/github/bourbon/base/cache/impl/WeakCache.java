package com.github.bourbon.base.cache.impl;

import java.util.WeakHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:55
 */
public class WeakCache<K, V> extends TimedCache<K, V> {

    private static final long serialVersionUID = 4954622307482454880L;

    public WeakCache(long timeout) {
        super(timeout, new WeakHashMap<>());
    }
}