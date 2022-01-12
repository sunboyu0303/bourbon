package com.github.bourbon.base.lang.mutable;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 16:31
 */
public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {
    private static final long serialVersionUID = 7371740489762504517L;
    private byte value;

    public MutableByte() {
    }

    public MutableByte(byte b) {
        value = b;
    }

    public MutableByte(Number n) {
        value = n.byteValue();
    }

    public MutableByte(String s) {
        value = Byte.parseByte(s);
    }

    @Override
    public Byte get() {
        return value;
    }

    public MutableByte set(byte b) {
        value = b;
        return this;
    }

    @Override
    public void set(Number n) {
        value = n.byteValue();
    }

    public MutableByte increment() {
        ++value;
        return this;
    }

    public MutableByte decrement() {
        --value;
        return this;
    }

    public MutableByte add(byte b) {
        value += b;
        return this;
    }

    public MutableByte add(Number n) {
        value += n.byteValue();
        return this;
    }

    public MutableByte subtract(byte b) {
        value -= b;
        return this;
    }

    public MutableByte subtract(Number n) {
        value -= n.byteValue();
        return this;
    }

    @Override
    public byte byteValue() {
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
    public boolean equals(Object object) {
        return BooleanUtils.defaultIfAssignableFrom(object, MutableByte.class, o -> value == ((MutableByte) o).byteValue(), false);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public int compareTo(MutableByte o) {
        return Byte.compare(value, o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}