package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:35
 */
@FunctionalInterface
public interface ThrowableLongBinaryOperator extends ThrowableBinaryOperator<Long> {

    default ThrowableLongBinaryOperator andThen(ThrowableLongUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static Long execute(Long t1, Long t2, ThrowableLongBinaryOperator function) throws IllegalArgumentException {
        try {
            return function.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}