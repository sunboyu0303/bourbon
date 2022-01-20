package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.LongConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 10:05
 */
public final class LongUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(LongConstants.PRIMITIVE_CLASS, LongConstants.BOXED_CLASS);

    public static long checkPositive(long l) {
        if (l <= LongConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return l;
    }

    public static long checkPositive(long l, String s) {
        if (l <= LongConstants.DEFAULT) {
            throw new IllegalArgumentException(s + " : " + l + " (expected: > 0)");
        }
        return l;
    }

    public static long checkPositive(long l, Supplier<String> s) {
        if (l <= LongConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return l;
    }

    public static <X extends Throwable> long checkPositive(long l, ThrowableSupplier<X> s) throws X {
        if (l <= LongConstants.DEFAULT) {
            throw s.get();
        }
        return l;
    }

    public static long checkPositiveOrZero(long l) {
        if (l < LongConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return l;
    }

    public static long checkPositiveOrZero(long l, String s) {
        if (l < LongConstants.DEFAULT) {
            throw new IllegalArgumentException(s + " : " + l + " (expected: >= 0)");
        }
        return l;
    }

    public static long checkPositiveOrZero(long l, Supplier<String> s) {
        if (l < LongConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return l;
    }

    public static <X extends Throwable> long checkPositiveOrZero(long l, ThrowableSupplier<X> s) throws X {
        if (l < LongConstants.DEFAULT) {
            throw s.get();
        }
        return l;
    }

    public static long checkInRange(long l, long start, long end) {
        if (l >= start && l <= end) {
            return l;
        }
        throw new IllegalArgumentException();
    }

    public static long checkInRange(long l, long start, long end, String s) {
        if (l >= start && l <= end) {
            return l;
        }
        throw new IllegalArgumentException(s + ": " + l + " (expected: " + start + "-" + end + ")");
    }

    public static long checkInRange(long l, long start, long end, Supplier<String> s) {
        if (l >= start && l <= end) {
            return l;
        }
        throw new IllegalArgumentException(s.get());
    }

    public static <X extends Throwable> long checkInRange(long l, long start, long end, ThrowableSupplier<X> s) throws X {
        if (l >= start && l <= end) {
            return l;
        }
        throw s.get();
    }

    public static boolean isLong(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static long longValue(Long w, long defaultValue) {
        return ObjectUtils.defaultIfNull(w, defaultValue);
    }

    public static boolean equals(long l1, long l2) {
        return l1 == l2;
    }

    public static int hashCode(long l) {
        return Long.hashCode(l);
    }

    private LongUtils() {
    }
}