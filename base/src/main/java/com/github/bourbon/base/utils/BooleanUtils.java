package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.BooleanConstants;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 16:08
 */
public final class BooleanUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(BooleanConstants.PRIMITIVE_CLASS, BooleanConstants.BOXED_CLASS);

    public static boolean isTrue(Boolean b) {
        return Boolean.TRUE.equals(b);
    }

    public static boolean isFalse(Boolean b) {
        return Boolean.FALSE.equals(b);
    }

    public static boolean negate(boolean b) {
        return !b;
    }

    public static boolean toBoolean(String s) {
        return !CharSequenceUtils.isBlank(s) && Boolean.parseBoolean(s.trim().toLowerCase());
    }

    public static char toChar(boolean b) {
        return (char) toInt(b);
    }

    public static float toFloat(boolean b) {
        return (float) toInt(b);
    }

    public static double toDouble(boolean b) {
        return toInt(b);
    }

    public static byte toByte(boolean b) {
        return (byte) toInt(b);
    }

    public static short toShort(boolean b) {
        return (short) toInt(b);
    }

    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }

    public static long toLong(boolean b) {
        return toInt(b);
    }

    public static Character toCharWrapper(boolean b) {
        return toChar(b);
    }

    public static Float toFloatWrapper(boolean b) {
        return toFloat(b);
    }

    public static Double toDoubleWrapper(boolean b) {
        return toDouble(b);
    }

    public static Byte toByteWrapper(boolean b) {
        return toByte(b);
    }

    public static Short toShortWrapper(boolean b) {
        return toShort(b);
    }

    public static Integer toIntWrapper(boolean b) {
        return toInt(b);
    }

    public static Long toLongWrapper(boolean b) {
        return toLong(b);
    }

    public static boolean and(boolean... array) {
        for (boolean b : PrimitiveArrayUtils.requireNonEmpty(array, "The Array must not be empty !")) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public static boolean or(boolean... array) {
        for (boolean b : PrimitiveArrayUtils.requireNonEmpty(array, "The Array must not be empty !")) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBoolean(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static boolean booleanValue(Boolean w, boolean defaultValue) {
        return ObjectUtils.defaultIfNull(w, defaultValue);
    }

    public static boolean equals(boolean b1, boolean b2) {
        return b1 == b2;
    }

    public static int hashCode(boolean b) {
        return Boolean.hashCode(b);
    }

    public static <T> T defaultIfFalse(boolean b, T t, T defaultValue) {
        return b ? t : defaultValue;
    }

    public static <T> T defaultIfFalse(boolean b, Supplier<T> s) {
        return defaultIfFalse(b, s, null);
    }

    public static <T> T defaultIfFalse(boolean b, Supplier<T> s, T t) {
        return b ? s.get() : t;
    }

    public static <T> T defaultSupplierIfFalse(boolean b, Supplier<T> s) {
        return defaultSupplierIfFalse(b, s, () -> null);
    }

    public static <T> T defaultSupplierIfFalse(boolean b, Supplier<T> s, Supplier<T> defaultValue) {
        return b ? s.get() : defaultValue.get();
    }

    public static <T, R> R defaultIfAssignableFrom(T t, Class<?> clazz, Function<T, R> f) {
        return defaultIfAssignableFrom(t, clazz, f, null);
    }

    public static <T, R> R defaultIfAssignableFrom(T t, Class<?> clazz, Function<T, R> f, R r) {
        return ObjectUtils.nonNull(t) && clazz.isAssignableFrom(t.getClass()) ? f.apply(t) : r;
    }

    public static <T, R> R defaultIfPredicate(T t, Predicate<T> predicate, Function<T, R> f) {
        return defaultIfPredicate(t, predicate, f, null);
    }

    public static <T, R> R defaultIfPredicate(T t, Predicate<T> predicate, Function<T, R> f, R r) {
        return predicate.test(t) ? f.apply(t) : r;
    }

    public static <T, R> R defaultSupplierIfPredicate(T t, Predicate<T> predicate, Function<T, R> f) {
        return defaultSupplierIfPredicate(t, predicate, f, () -> null);
    }

    public static <T, R> R defaultSupplierIfPredicate(T t, Predicate<T> predicate, Function<T, R> f, Supplier<R> s) {
        return predicate.test(t) ? f.apply(t) : s.get();
    }

    public static <T, R> R defaultSupplierIfAssignableFrom(T t, Class<?> clazz, Function<T, R> f) {
        return defaultSupplierIfAssignableFrom(t, clazz, f, () -> null);
    }

    public static <T, R> R defaultSupplierIfAssignableFrom(T t, Class<?> clazz, Function<T, R> f, Supplier<R> s) {
        return ObjectUtils.nonNull(t) && clazz.isAssignableFrom(t.getClass()) ? f.apply(t) : s.get();
    }

    private BooleanUtils() {
    }
}