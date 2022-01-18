package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 10:52
 */
public class InternalThreadLocal<V> {

    private static final int VARIABLES_TO_REMOVE_INDEX = InternalThreadLocalMap.nextVariableIndex();

    private final int index;

    public InternalThreadLocal() {
        index = InternalThreadLocalMap.nextVariableIndex();
    }

    @SuppressWarnings("unchecked")
    public static void removeAll() {
        InternalThreadLocalMap map = InternalThreadLocalMap.getIfSet();
        if (map == null) {
            return;
        }
        try {
            Object v = map.indexedVariable(VARIABLES_TO_REMOVE_INDEX);
            if (v != null && v != InternalThreadLocalMap.UNSET) {
                ((Set<InternalThreadLocal<?>>) v).forEach(t -> t.remove(map));
            }
        } finally {
            InternalThreadLocalMap.remove();
        }
    }

    public static int size() {
        return ObjectUtils.defaultIfNull(InternalThreadLocalMap.getIfSet(), InternalThreadLocalMap::size, 0);
    }

    public static void destroy() {
        InternalThreadLocalMap.destroy();
    }

    public final void remove() {
        remove(InternalThreadLocalMap.getIfSet());
    }

    @SuppressWarnings("unchecked")
    public final void remove(InternalThreadLocalMap map) {
        if (map == null) {
            return;
        }
        Object v = map.removeIndexedVariable(index);
        removeFromVariablesToRemove(map, this);
        if (v != InternalThreadLocalMap.UNSET) {
            try {
                onRemoval((V) v);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public final V get() {
        InternalThreadLocalMap map = InternalThreadLocalMap.get();
        Object v = map.indexedVariable(index);
        return v != InternalThreadLocalMap.UNSET ? (V) v : initialize(map);
    }

    private V initialize(InternalThreadLocalMap map) {
        try {
            V v = initialValue();
            map.setIndexedVariable(index, v);
            addToVariablesToRemove(map, this);
            return v;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public final InternalThreadLocal set(V value) {
        if (value == null || value == InternalThreadLocalMap.UNSET) {
            remove();
        } else {
            InternalThreadLocalMap map = InternalThreadLocalMap.get();
            if (map.setIndexedVariable(index, value)) {
                addToVariablesToRemove(map, this);
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private static void addToVariablesToRemove(InternalThreadLocalMap map, InternalThreadLocal<?> variable) {
        Object v = map.indexedVariable(VARIABLES_TO_REMOVE_INDEX);
        Set<InternalThreadLocal<?>> variablesToRemove;
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            variablesToRemove = Collections.newSetFromMap(new IdentityHashMap<>());
            map.setIndexedVariable(VARIABLES_TO_REMOVE_INDEX, variablesToRemove);
        } else {
            variablesToRemove = (Set<InternalThreadLocal<?>>) v;
        }
        variablesToRemove.add(variable);
    }

    @SuppressWarnings("unchecked")
    private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, InternalThreadLocal<?> variable) {
        Object v = threadLocalMap.indexedVariable(VARIABLES_TO_REMOVE_INDEX);
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            return;
        }
        ((Set<InternalThreadLocal<?>>) v).remove(variable);
    }

    protected V initialValue() throws Exception {
        return null;
    }

    protected void onRemoval(V v) throws Exception {
    }
}