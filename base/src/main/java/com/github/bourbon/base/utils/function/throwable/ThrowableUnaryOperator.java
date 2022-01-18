package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:30
 */
@FunctionalInterface
public interface ThrowableUnaryOperator<T> extends ThrowableFunction<T, T> {

    default ThrowableUnaryOperator<T> compose(ThrowableUnaryOperator<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableUnaryOperator<T> andThen(ThrowableUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static <T> ThrowableUnaryOperator<T> identity() {
        return t -> t;
    }

    static <T> T execute(T t, ThrowableUnaryOperator<T> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}