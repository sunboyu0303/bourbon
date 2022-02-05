package com.github.bourbon.base.lang.mutable;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 22:36
 */
public class MutableObject<T> implements Mutable<T> {
    private static final long serialVersionUID = -1600709885140428612L;
    private T value;

    public MutableObject() {
    }

    public MutableObject(T t) {
        value = t;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T t) {
        value = t;
    }

    @Override
    public boolean equals(Object o) {
        if (ObjectUtils.isNull(o) || getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return ObjectUtils.equals(value, ((MutableObject) o).value);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.defaultIfNull(value, Object::hashCode, 0);
    }

    @Override
    public String toString() {
        return ObjectUtils.defaultIfNull(value, Object::toString, StringConstants.NULL);
    }
}