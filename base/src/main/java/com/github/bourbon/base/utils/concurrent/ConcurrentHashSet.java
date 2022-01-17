package com.github.bourbon.base.utils.concurrent;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 16:28
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E> {

    private static final Boolean PRESENT = Boolean.TRUE;
    private final ConcurrentHashMap<E, Boolean> map;

    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    public ConcurrentHashSet(Iterable<E> i) {
        if (i instanceof Collection) {
            Collection<E> c = (Collection<E>) i;
            map = new ConcurrentHashMap<>((int) ((float) c.size() / 0.75F));
            addAll(c);
        } else {
            map = new ConcurrentHashMap<>();
            i.forEach(this::add);
        }
    }

    public ConcurrentHashSet(Collection<E> c) {
        map = new ConcurrentHashMap<>((int) ((float) c.size() / 0.75F));
        addAll(c);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }
}