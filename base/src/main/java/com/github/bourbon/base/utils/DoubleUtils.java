package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.DoubleConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 10:11
 */
public final class DoubleUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(DoubleConstants.PRIMITIVE_CLASS, DoubleConstants.BOXED_CLASS);

    public static double checkPositive(double d) {
        if (d <= DoubleConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return d;
    }

    public static double checkPositive(double d, String name) {
        if (d <= DoubleConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + d + " (expected: > 0)");
        }
        return d;
    }

    public static double checkPositive(double d, Supplier<String> s) {
        if (d <= DoubleConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return d;
    }

    public static <X extends Throwable> double checkPositive(double d, ThrowableSupplier<X> s) throws X {
        if (d <= DoubleConstants.DEFAULT) {
            throw s.get();
        }
        return d;
    }

    public static double checkPositiveOrZero(double d) {
        if (d < DoubleConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return d;
    }

    public static double checkPositiveOrZero(double d, String name) {
        if (d < DoubleConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + d + " (expected: >= 0)");
        }
        return d;
    }

    public static double checkPositiveOrZero(double d, Supplier<String> s) {
        if (d < DoubleConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return d;
    }

    public static <X extends Throwable> double checkPositiveOrZero(double d, ThrowableSupplier<X> s) throws X {
        if (d < DoubleConstants.DEFAULT) {
            throw s.get();
        }
        return d;
    }

    public static double checkInRange(double d, double start, double end) {
        if (d >= start && d <= end) {
            return d;
        }
        throw new IllegalArgumentException();
    }

    public static double checkInRange(double d, double start, double end, String name) {
        if (d >= start && d <= end) {
            return d;
        }
        throw new IllegalArgumentException(name + ": " + d + " (expected: " + start + "-" + end + ")");
    }

    public static double checkInRange(double d, double start, double end, Supplier<String> s) {
        if (d >= start && d <= end) {
            return d;
        }
        throw new IllegalArgumentException(s.get());
    }

    public static <X extends Throwable> double checkInRange(double d, double start, double end, ThrowableSupplier<X> s) throws X {
        if (d >= start && d <= end) {
            return d;
        }
        throw s.get();
    }

    public static boolean isDouble(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static double doubleValue(Double wrapper, double defaultValue) {
        return ObjectUtils.defaultIfNull(wrapper, defaultValue);
    }

    public static boolean equals(double d1, double d2) {
        return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
    }

    public static int hashCode(double d) {
        return Double.hashCode(d);
    }

    private DoubleUtils() {
    }
}