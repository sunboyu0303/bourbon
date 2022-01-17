package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 22:10
 */
public class ConcurrentHashMultiSet<E> {
    private final Map<E, AtomicInteger> map = new ConcurrentHashMap<>();

    public int add(E e) {
        return ObjectUtils.defaultIfNull(e, k -> map.computeIfAbsent(k, o -> new AtomicInteger()).incrementAndGet(), 0);
    }

    public int remove(E e) {
        if (e == null) {
            return 0;
        }
        AtomicInteger atomicInteger = map.get(e);
        if (atomicInteger == null || atomicInteger.get() == 0) {
            return 0;
        }
        return atomicInteger.decrementAndGet();
    }

    public int get(E e) {
        if (e == null) {
            return 0;
        }
        AtomicInteger atomicInteger = map.get(e);
        if (atomicInteger == null) {
            return 0;
        }
        return atomicInteger.get();
    }

    public Set<E> elementSet() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(E e) {
        return map.containsKey(e);
    }

    public void clear() {
        map.clear();
    }
}