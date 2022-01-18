package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:29
 */
@FunctionalInterface
public interface ThrowableDoubleUnaryOperator extends ThrowableUnaryOperator<Double> {

    default ThrowableDoubleUnaryOperator compose(ThrowableDoubleUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableDoubleUnaryOperator andThen(ThrowableDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static ThrowableDoubleUnaryOperator identity() {
        return t -> t;
    }

    static Double execute(Double t, ThrowableDoubleUnaryOperator function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}