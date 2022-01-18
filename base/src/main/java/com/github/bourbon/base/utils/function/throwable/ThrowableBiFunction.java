package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 17:54
 */
@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {

    R apply(T t, U u) throws Exception;

    default <V> ThrowableBiFunction<T, U, V> andThen(ThrowableFunction<? super R, ? extends V> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static <T, U, R> R execute(T t, U u, ThrowableBiFunction<T, U, R> function) throws IllegalArgumentException {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}