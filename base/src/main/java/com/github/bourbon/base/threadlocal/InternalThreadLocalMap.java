package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.utils.BooleanUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 10:55
 */
public class InternalThreadLocalMap {

    static final Object UNSET = new Object();

    private static final AtomicInteger NEXT_INDEX = new AtomicInteger();

    private static ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<>();

    private Object[] indexedVariables;

    private InternalThreadLocalMap() {
        int variableIndex = NEXT_INDEX.get();
        Object[] array = new Object[variableIndex < 32 ? 32 : newCapacity(variableIndex)];
        Arrays.fill(array, UNSET);
        indexedVariables = array;
    }

    public static InternalThreadLocalMap getIfSet() {
        return BooleanUtils.defaultSupplierIfAssignableFrom(Thread.currentThread(), InternalThread.class, t -> ((InternalThread) t).getThreadLocalMap(), () -> slowThreadLocalMap.get());
    }

    public static InternalThreadLocalMap get() {
        return BooleanUtils.defaultSupplierIfAssignableFrom(Thread.currentThread(), InternalThread.class, t -> fastGet((InternalThread) t), InternalThreadLocalMap::slowGet);
    }

    private static InternalThreadLocalMap fastGet(InternalThread t) {
        InternalThreadLocalMap map = t.getThreadLocalMap();
        if (map == null) {
            map = new InternalThreadLocalMap();
            t.setThreadLocalMap(map);
        }
        return map;
    }

    private static InternalThreadLocalMap slowGet() {
        InternalThreadLocalMap map = slowThreadLocalMap.get();
        if (map == null) {
            map = new InternalThreadLocalMap();
            slowThreadLocalMap.set(map);
        }
        return map;
    }

    public static void remove() {
        Thread t = Thread.currentThread();
        if (t instanceof InternalThread) {
            ((InternalThread) t).setThreadLocalMap(null);
        } else {
            slowThreadLocalMap.remove();
        }
    }

    public static void destroy() {
        slowThreadLocalMap = null;
    }

    private static int newCapacity(int index) {
        int newCapacity = index;
        newCapacity |= newCapacity >>> 1;
        newCapacity |= newCapacity >>> 2;
        newCapacity |= newCapacity >>> 4;
        newCapacity |= newCapacity >>> 8;
        newCapacity |= newCapacity >>> 16;
        return ++newCapacity;
    }

    public Object indexedVariable(int idx) {
        return BooleanUtils.defaultIfFalse(idx < indexedVariables.length, () -> indexedVariables[idx], UNSET);
    }

    public boolean setIndexedVariable(int index, Object value) {
        return BooleanUtils.defaultSupplierIfFalse(index < indexedVariables.length,
                () -> {
                    Object oldValue = indexedVariables[index];
                    indexedVariables[index] = value;
                    return oldValue == UNSET;
                },
                () -> {
                    expandIndexedVariableTableAndSet(index, value);
                    return true;
                }
        );
    }

    public Object removeIndexedVariable(int index) {
        return BooleanUtils.defaultIfFalse(index < indexedVariables.length, () -> {
            Object v = indexedVariables[index];
            indexedVariables[index] = UNSET;
            return v;
        }, UNSET);
    }

    public int size() {
        int count = 0;
        for (Object o : indexedVariables) {
            if (o != UNSET) {
                ++count;
            }
        }
        return count - 1;
    }

    private void expandIndexedVariableTableAndSet(int index, Object value) {
        Object[] oldArray = indexedVariables;
        final int oldCapacity = oldArray.length;
        Object[] newArray = Arrays.copyOf(oldArray, newCapacity(index));
        Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
        newArray[index] = value;
        indexedVariables = newArray;
    }

    static int nextVariableIndex() {
        int index = NEXT_INDEX.getAndIncrement();
        if (index < 0) {
            NEXT_INDEX.decrementAndGet();
            throw new IllegalStateException("Too many thread-local indexed variables");
        }
        return index;
    }

    public static int lastVariableIndex() {
        return NEXT_INDEX.get() - 1;
    }
}