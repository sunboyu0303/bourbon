package com.github.bourbon.base.lang;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/7 12:11
 */
public final class VolatileObject<T> {

    private volatile T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}