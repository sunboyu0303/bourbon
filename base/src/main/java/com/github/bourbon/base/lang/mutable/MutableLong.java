package com.github.bourbon.base.lang.mutable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 22:24
 */
public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {
    private static final long serialVersionUID = 4426064833071802408L;
    private long value;

    public MutableLong() {
    }

    public MutableLong(long l) {
        value = l;
    }

    public MutableLong(Number n) {
        value = n.longValue();
    }

    public MutableLong(String s) {
        value = Long.parseLong(s);
    }

    @Override
    public Long get() {
        return value;
    }

    public MutableLong set(long l) {
        value = l;
        return this;
    }

    @Override
    public void set(Number n) {
        value = n.longValue();
    }

    public MutableLong increment() {
        ++value;
        return this;
    }

    public MutableLong decrement() {
        --value;
        return this;
    }

    public MutableLong add(long l) {
        value += l;
        return this;
    }

    public MutableLong add(Number n) {
        value += n.longValue();
        return this;
    }

    public MutableLong subtract(long l) {
        value -= l;
        return this;
    }

    public MutableLong subtract(Number n) {
        value -= n.longValue();
        return this;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MutableLong && value == ((MutableLong) o).longValue();
    }

    @Override
    public int hashCode() {
        return (int) (value ^ value >>> 32);
    }

    @Override
    public int compareTo(MutableLong o) {
        return Long.compare(value, o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}