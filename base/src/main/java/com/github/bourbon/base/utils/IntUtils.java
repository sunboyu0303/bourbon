package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 10:03
 */
public final class IntUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(IntConstants.PRIMITIVE_CLASS, IntConstants.BOXED_CLASS);

    public static int checkPositive(int i) {
        if (i <= IntConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return i;
    }

    public static int checkPositive(int i, String name) {
        if (i <= IntConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + i + " (expected: > 0)");
        }
        return i;
    }

    public static int checkPositive(int i, Supplier<String> s) {
        if (i <= IntConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return i;
    }

    public static <X extends Throwable> int checkPositive(int i, ThrowableSupplier<X> s) throws X {
        if (i <= IntConstants.DEFAULT) {
            throw s.get();
        }
        return i;
    }

    public static int checkPositiveOrZero(int i) {
        if (i < IntConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return i;
    }

    public static int checkPositiveOrZero(int i, String name) {
        if (i < IntConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + i + " (expected: >= 0)");
        }
        return i;
    }

    public static int checkPositiveOrZero(int i, Supplier<String> s) {
        if (i < IntConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return i;
    }

    public static <X extends Throwable> int checkPositiveOrZero(int i, ThrowableSupplier<X> s) throws X {
        if (i < IntConstants.DEFAULT) {
            throw s.get();
        }
        return i;
    }

    public static int checkInRange(int i, int start, int end) {
        if (i >= start && i <= end) {
            return i;
        }
        throw new IllegalArgumentException();
    }

    public static int checkInRange(int i, int start, int end, String name) {
        if (i >= start && i <= end) {
            return i;
        }
        throw new IllegalArgumentException(name + ": " + i + " (expected: " + start + "-" + end + ")");
    }

    public static int checkInRange(int i, int start, int end, Supplier<String> s) {
        if (i >= start && i <= end) {
            return i;
        }
        throw new IllegalArgumentException(s.get());
    }

    public static <X extends Throwable> int checkInRange(int i, int start, int end, ThrowableSupplier<X> s) throws X {
        if (i >= start && i <= end) {
            return i;
        }
        throw s.get();
    }

    public static boolean isInteger(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static int intValue(Integer wrapper, int defaultValue) {
        return ObjectUtils.defaultIfNull(wrapper, defaultValue);
    }

    public static boolean equals(int i1, int i2) {
        return i1 == i2;
    }

    private IntUtils() {
    }
}