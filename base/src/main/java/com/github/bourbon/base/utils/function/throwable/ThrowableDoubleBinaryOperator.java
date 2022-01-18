package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:08
 */
@FunctionalInterface
public interface ThrowableDoubleBinaryOperator extends ThrowableBinaryOperator<Double> {

    default ThrowableDoubleBinaryOperator andThen(ThrowableDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static Double execute(Double t1, Double t2, ThrowableDoubleBinaryOperator function) throws IllegalArgumentException {
        try {
            return function.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}