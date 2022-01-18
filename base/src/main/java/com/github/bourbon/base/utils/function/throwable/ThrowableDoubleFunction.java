package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:19
 */
@FunctionalInterface
public interface ThrowableDoubleFunction<R> extends ThrowableFunction<Double, R> {

    default ThrowableDoubleFunction<R> compose(ThrowableDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableDoubleFunction<Double> identity() {
        return t -> t;
    }

    static <R> R execute(Double t, ThrowableDoubleFunction<R> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}