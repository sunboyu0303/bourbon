package com.github.bourbon.base.lang.mutable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 22:59
 */
public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {
    private static final long serialVersionUID = 6788914512630050485L;
    private short value;

    public MutableShort() {
    }

    public MutableShort(short s) {
        value = s;
    }

    public MutableShort(Number n) {
        value = n.shortValue();
    }

    public MutableShort(String s) {
        value = Short.parseShort(s);
    }

    @Override
    public Short get() {
        return value;
    }

    public MutableShort set(short s) {
        value = s;
        return this;
    }

    @Override
    public void set(Number n) {
        value = n.shortValue();
    }

    public MutableShort increment() {
        ++value;
        return this;
    }

    public MutableShort decrement() {
        --value;
        return this;
    }

    public MutableShort add(short s) {
        value += s;
        return this;
    }

    public MutableShort add(Number n) {
        value += n.shortValue();
        return this;
    }

    public MutableShort subtract(short s) {
        value -= s;
        return this;
    }

    public MutableShort subtract(Number n) {
        value -= n.shortValue();
        return this;
    }

    @Override
    public short shortValue() {
        return value;
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
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MutableShort && value == ((MutableShort) o).shortValue();
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public int compareTo(MutableShort o) {
        return Short.compare(value, o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}