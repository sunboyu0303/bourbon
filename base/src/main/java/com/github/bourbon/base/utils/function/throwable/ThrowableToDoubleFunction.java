package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 17:01
 */
@FunctionalInterface
public interface ThrowableToDoubleFunction<T> extends ThrowableFunction<T, Double> {

    default ThrowableToDoubleFunction<Double> compose(ThrowableDoubleFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableToDoubleFunction<T> andThen(ThrowableDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static ThrowableToDoubleFunction<Double> identity() {
        return t -> t;
    }

    static <T> Double execute(T t, ThrowableToDoubleFunction<T> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}