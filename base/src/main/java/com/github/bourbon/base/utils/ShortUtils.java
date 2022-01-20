package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.ShortConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 09:59
 */
public final class ShortUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(ShortConstants.PRIMITIVE_CLASS, ShortConstants.BOXED_CLASS);

    public static short checkPositive(short s) {
        if (s <= ShortConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return s;
    }

    public static short checkPositive(short s, String name) {
        if (s <= ShortConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + s + " (expected: > 0)");
        }
        return s;
    }

    public static short checkPositive(short s, Supplier<String> supplier) {
        if (s <= ShortConstants.DEFAULT) {
            throw new IllegalArgumentException(supplier.get());
        }
        return s;
    }

    public static <X extends Throwable> short checkPositive(short s, ThrowableSupplier<X> supplier) throws X {
        if (s <= ShortConstants.DEFAULT) {
            throw supplier.get();
        }
        return s;
    }

    public static short checkPositiveOrZero(short s) {
        if (s < ShortConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return s;
    }

    public static short checkPositiveOrZero(short s, String name) {
        if (s < ShortConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + s + " (expected: >= 0)");
        }
        return s;
    }

    public static short checkPositiveOrZero(short s, Supplier<String> supplier) {
        if (s < ShortConstants.DEFAULT) {
            throw new IllegalArgumentException(supplier.get());
        }
        return s;
    }

    public static <X extends Throwable> short checkPositiveOrZero(short s, ThrowableSupplier<X> supplier) throws X {
        if (s < ShortConstants.DEFAULT) {
            throw supplier.get();
        }
        return s;
    }

    public static short checkInRange(short s, short start, short end) {
        if (s >= start && s <= end) {
            return s;
        }
        throw new IllegalArgumentException();
    }

    public static short checkInRange(short s, short start, short end, String name) {
        if (s >= start && s <= end) {
            return s;
        }
        throw new IllegalArgumentException(name + ": " + s + " (expected: " + start + "-" + end + ")");
    }

    public static short checkInRange(short s, short start, short end, Supplier<String> supplier) {
        if (s >= start && s <= end) {
            return s;
        }
        throw new IllegalArgumentException(supplier.get());
    }

    public static <X extends Throwable> short checkInRange(short s, short start, short end, ThrowableSupplier<X> supplier) throws X {
        if (s >= start && s <= end) {
            return s;
        }
        throw supplier.get();
    }

    public static boolean isShort(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static short shortValue(Short wrapper, short defaultValue) {
        return ObjectUtils.defaultIfNull(wrapper, defaultValue);
    }

    public static boolean equals(short s1, short s2) {
        return s1 == s2;
    }

    private ShortUtils() {
    }
}