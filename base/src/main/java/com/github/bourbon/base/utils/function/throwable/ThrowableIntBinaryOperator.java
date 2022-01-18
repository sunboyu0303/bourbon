package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:21
 */
@FunctionalInterface
public interface ThrowableIntBinaryOperator extends ThrowableBinaryOperator<Integer> {

    default ThrowableIntBinaryOperator andThen(ThrowableIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static Integer execute(Integer t1, Integer t2, ThrowableIntBinaryOperator function) throws IllegalArgumentException {
        try {
            return function.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}