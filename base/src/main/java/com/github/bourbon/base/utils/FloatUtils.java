package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.FloatConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 10:06
 */
public final class FloatUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(FloatConstants.PRIMITIVE_CLASS, FloatConstants.BOXED_CLASS);

    public static float checkPositive(float f) {
        if (f <= FloatConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return f;
    }

    public static float checkPositive(float f, String name) {
        if (f <= FloatConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + f + " (expected: > 0)");
        }
        return f;
    }

    public static float checkPositive(float f, Supplier<String> s) {
        if (f <= FloatConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return f;
    }

    public static <X extends Throwable> float checkPositive(float f, ThrowableSupplier<X> s) throws X {
        if (f <= FloatConstants.DEFAULT) {
            throw s.get();
        }
        return f;
    }

    public static float checkPositiveOrZero(float f) {
        if (f < FloatConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return f;
    }

    public static float checkPositiveOrZero(float f, String name) {
        if (f < FloatConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + f + " (expected: >= 0)");
        }
        return f;
    }

    public static float checkPositiveOrZero(float f, Supplier<String> s) {
        if (f < FloatConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return f;
    }

    public static <X extends Throwable> float checkPositiveOrZero(float f, ThrowableSupplier<X> s) throws X {
        if (f < FloatConstants.DEFAULT) {
            throw s.get();
        }
        return f;
    }

    public static float checkInRange(float f, float start, float end) {
        if (f >= start && f <= end) {
            return f;
        }
        throw new IllegalArgumentException();
    }

    public static float checkInRange(float f, float start, float end, String name) {
        if (f >= start && f <= end) {
            return f;
        }
        throw new IllegalArgumentException(name + ": " + f + " (expected: " + start + "-" + end + ")");
    }

    public static float checkInRange(float f, float start, float end, Supplier<String> s) {
        if (f >= start && f <= end) {
            return f;
        }
        throw new IllegalArgumentException(s.get());
    }

    public static <X extends Throwable> float checkInRange(float f, float start, float end, ThrowableSupplier<X> s) throws X {
        if (f >= start && f <= end) {
            return f;
        }
        throw s.get();
    }

    public static boolean isFloat(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static float floatValue(Float wrapper, float defaultValue) {
        return ObjectUtils.defaultIfNull(wrapper, defaultValue);
    }

    public static boolean equals(float f1, float f2) {
        return Float.floatToIntBits(f1) == Float.floatToIntBits(f2);
    }

    public static int hashCode(float f) {
        return Float.hashCode(f);
    }

    private FloatUtils() {
    }
}