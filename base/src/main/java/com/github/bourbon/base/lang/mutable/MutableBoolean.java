package com.github.bourbon.base.lang.mutable;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 15:44
 */
public class MutableBoolean implements Comparable<MutableBoolean>, Mutable<Boolean> {
    private static final long serialVersionUID = 8283515764895559145L;
    private boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(boolean b) {
        value = b;
    }

    public MutableBoolean(String s) {
        value = Boolean.parseBoolean(s);
    }

    @Override
    public Boolean get() {
        return value;
    }

    public MutableBoolean set(boolean b) {
        value = b;
        return this;
    }

    @Override
    public void set(Boolean b) {
        value = b;
    }

    @Override
    public boolean equals(Object object) {
        return BooleanUtils.defaultIfAssignableFrom(object, MutableBoolean.class, o -> value == ((MutableBoolean) o).value, false);
    }

    @Override
    public int hashCode() {
        return BooleanUtils.defaultSupplierIfFalse(value, Boolean.TRUE::hashCode, Boolean.FALSE::hashCode);
    }

    @Override
    public int compareTo(MutableBoolean o) {
        return Boolean.compare(value, o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}