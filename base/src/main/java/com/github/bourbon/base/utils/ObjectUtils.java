package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 */
public interface ObjectUtils {

    static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 != null && o2 != null) {
            return o1.equals(o2) || (o1.getClass().isArray() && o2.getClass().isArray()) && ArrayUtils.arrayEquals(o1, o2);
        }
        return false;
    }

    static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    static int hashCode(Object o) {
        return Objects.hashCode(o);
    }

    static int nullSafeHashCode(Object object) {
        return defaultIfNullElseFunction(object, o -> BooleanUtils.defaultSupplierIfFalse(o.getClass().isArray(), () -> ArrayUtils.nullSafeHashCode(o), o::hashCode), 0);
    }

    static <T> int compare(T a, T b, Comparator<? super T> c) {
        return BooleanUtils.defaultIfFalse(a != b, () -> c.compare(a, b), 0);
    }

    static String toString(Object o) {
        return String.valueOf(o);
    }

    static String nullSafeToString(Object object) {
        return defaultIfNullElseFunction(object, o -> BooleanUtils.defaultSupplierIfAssignableFrom(o, String.class, String.class::cast,
                () -> BooleanUtils.defaultSupplierIfFalse(
                        o.getClass().isArray(), () -> ArrayUtils.nullSafeToString(o), () -> defaultIfNull(o.toString(), StringConstants.EMPTY)
                )
        ), StringConstants.NULL);
    }

    static String toString(Object o, String s) {
        return defaultIfNullElseFunction(o, Object::toString, s);
    }

    static boolean isNull(Object o) {
        return o == null;
    }

    static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof Optional) {
            return !((Optional) o).isPresent();
        }
        if (o instanceof CharSequence) {
            return CharSequenceUtils.isEmpty((CharSequence) o);
        }
        if (o.getClass().isArray()) {
            return ArrayUtils.isEmpty(o);
        }
        if (o instanceof Collection) {
            return CollectionUtils.isEmpty((Collection) o);
        }
        if (o instanceof Map) {
            return MapUtils.isEmpty((Map) o);
        }
        return false;
    }

    static boolean nonNull(Object o) {
        return o != null;
    }

    static <T> void nonNullConsumer(T t, Consumer<T> c) {
        if (nonNull(t)) {
            c.accept(t);
        }
    }

    static <T> T defaultIfNull(T t, T defaultValue) {
        return nonNull(t) ? t : defaultValue;
    }

    static <T> T defaultSupplierIfNull(T t, Supplier<T> s) {
        return nonNull(t) ? t : s.get();
    }

    static <T, R> R defaultIfNullElseFunction(T t, Function<T, R> f) {
        return defaultIfNullElseFunction(t, f, null);
    }

    static <T, R> R defaultIfNullElseFunction(T t, Function<T, R> f, R r) {
        return nonNull(t) ? f.apply(t) : r;
    }

    static <T, R> R defaultSupplierIfNullElseFunction(T t, Function<T, R> f) {
        return defaultSupplierIfNullElseFunction(t, f, () -> null);
    }

    static <T, R> R defaultSupplierIfNullElseFunction(T t, Function<T, R> f, Supplier<R> s) {
        return nonNull(t) ? f.apply(t) : s.get();
    }

    static <T> T requireNonNull(T t) {
        if (isNull(t)) {
            throw new NullPointerException();
        }
        return t;
    }

    static <T> T requireNonNull(T t, String s) {
        if (isNull(t)) {
            throw new NullPointerException(s);
        }
        return t;
    }

    static <T> T requireNonNull(T t, Supplier<String> s) {
        if (isNull(t)) {
            throw new NullPointerException(s.get());
        }
        return t;
    }

    static <T, X extends Throwable> T requireNonNull(T t, ThrowableSupplier<X> s) throws X {
        if (isNull(t)) {
            throw s.get();
        }
        return t;
    }

    static <T> T nullSafeGet(Supplier<T> s) {
        return nullSafeGet(s, null);
    }

    static <T> T nullSafeGet(Supplier<T> s, T t) {
        return nonNull(s) ? s.get() : t;
    }

    static <T, R> R nullSafeApply(Function<T, R> f, T t) {
        return nullSafeApply(f, t, null);
    }

    static <T, R> R nullSafeApply(Function<T, R> f, T t, R r) {
        return nonNull(f) ? f.apply(t) : r;
    }
}