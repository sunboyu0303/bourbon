package com.github.bourbon.base.lang.mutable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 16:51
 */
public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {
    private static final long serialVersionUID = -1348272840713402262L;
    private int value;

    public MutableInt() {
    }

    public MutableInt(int i) {
        value = i;
    }

    public MutableInt(Number n) {
        value = n.intValue();
    }

    public MutableInt(String s) {
        value = Integer.parseInt(s);
    }

    @Override
    public Integer get() {
        return value;
    }

    public MutableInt set(int i) {
        value = i;
        return this;
    }

    @Override
    public void set(Number n) {
        value = n.intValue();
    }

    public MutableInt increment() {
        ++value;
        return this;
    }

    public MutableInt decrement() {
        --value;
        return this;
    }

    public MutableInt add(int i) {
        value += i;
        return this;
    }

    public MutableInt add(Number n) {
        value += n.intValue();
        return this;
    }

    public MutableInt subtract(int i) {
        value -= i;
        return this;
    }

    public MutableInt subtract(Number n) {
        value -= n.intValue();
        return this;
    }

    @Override
    public int intValue() {
        return value;
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
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MutableInt && value == ((MutableInt) o).intValue();
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public int compareTo(MutableInt o) {
        return Integer.compare(value, o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}