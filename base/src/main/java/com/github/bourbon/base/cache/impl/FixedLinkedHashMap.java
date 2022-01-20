package com.github.bourbon.base.cache.impl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:32
 */
class FixedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -629171177321416095L;
    private int capacity;

    FixedLinkedHashMap(int capacity) {
        super(capacity + 1, 1.0F, true);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public FixedLinkedHashMap<K, V> setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}