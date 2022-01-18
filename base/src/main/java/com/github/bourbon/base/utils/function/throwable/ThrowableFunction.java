package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 17:55
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {

    R apply(T t) throws Exception;

    default <V> ThrowableFunction<V, R> compose(ThrowableFunction<? super V, ? extends T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default <V> ThrowableFunction<T, V> andThen(ThrowableFunction<? super R, ? extends V> a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static <T> ThrowableFunction<T, T> identity() {
        return t -> t;
    }

    static <T, R> R execute(T t, ThrowableFunction<T, R> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}