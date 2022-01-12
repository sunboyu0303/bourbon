package com.github.bourbon.base.lang.mutable;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 15:55
 */
public class MutableFloat extends Number implements Comparable<MutableFloat>, Mutable<Number> {
    private static final long serialVersionUID = -102038992986313766L;
    private float value;

    public MutableFloat() {
    }

    public MutableFloat(float f) {
        value = f;
    }

    public MutableFloat(Number n) {
        value = n.floatValue();
    }

    public MutableFloat(String s) {
        value = Float.parseFloat(s);
    }

    @Override
    public Float get() {
        return value;
    }

    public MutableFloat set(float f) {
        value = f;
        return this;
    }

    @Override
    public void set(Number n) {
        value = n.floatValue();
    }

    public MutableFloat increment() {
        ++value;
        return this;
    }

    public MutableFloat decrement() {
        --value;
        return this;
    }

    public MutableFloat add(float f) {
        value += f;
        return this;
    }

    public MutableFloat add(Number n) {
        value += n.floatValue();
        return this;
    }

    public MutableFloat subtract(float f) {
        value -= f;
        return this;
    }

    public MutableFloat subtract(Number n) {
        value -= n.floatValue();
        return this;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
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
        return BooleanUtils.defaultIfAssignableFrom(object, MutableFloat.class, o -> Float.floatToIntBits(((MutableFloat) o).value) == Float.floatToIntBits(value), false);
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(value);
    }

    @Override
    public int compareTo(MutableFloat other) {
        return Double.compare(value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}