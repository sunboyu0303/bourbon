package com.github.bourbon.base.lang.mutable;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 16:44
 */
public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {
    private static final long serialVersionUID = -4838200458285390447L;
    private double value;

    public MutableDouble() {
    }

    public MutableDouble(double d) {
        value = d;
    }

    public MutableDouble(Number n) {
        value = n.doubleValue();
    }

    public MutableDouble(String s) {
        value = Double.parseDouble(s);
    }

    @Override
    public Double get() {
        return value;
    }

    public MutableDouble set(double d) {
        value = d;
        return this;
    }

    @Override
    public void set(Number n) {
        value = n.doubleValue();
    }

    public MutableDouble increment() {
        ++value;
        return this;
    }

    public MutableDouble decrement() {
        --value;
        return this;
    }

    public MutableDouble add(double d) {
        value += d;
        return this;
    }

    public MutableDouble add(Number n) {
        value += n.doubleValue();
        return this;
    }

    public MutableDouble subtract(double d) {
        value -= d;
        return this;
    }

    public MutableDouble subtract(Number n) {
        value -= n.doubleValue();
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
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        return BooleanUtils.defaultIfAssignableFrom(object, MutableDouble.class, o -> Double.doubleToLongBits(((MutableDouble) o).value) == Double.doubleToLongBits(value), false);
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ bits >>> 32);
    }

    @Override
    public int compareTo(MutableDouble o) {
        return Double.compare(value, o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}