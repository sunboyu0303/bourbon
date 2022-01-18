package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 17:56
 */
@FunctionalInterface
public interface ThrowableBinaryOperator<T> extends ThrowableBiFunction<T, T, T> {

    default ThrowableBinaryOperator<T> andThen(ThrowableUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static <T> T execute(T t1, T t2, ThrowableBinaryOperator<T> function) throws IllegalArgumentException {
        try {
            return function.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}