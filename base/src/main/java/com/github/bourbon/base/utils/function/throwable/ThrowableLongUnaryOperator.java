package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:45
 */
@FunctionalInterface
public interface ThrowableLongUnaryOperator extends ThrowableUnaryOperator<Long> {

    default ThrowableLongUnaryOperator compose(ThrowableLongUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableLongUnaryOperator andThen(ThrowableLongUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static ThrowableLongUnaryOperator identity() {
        return t -> t;
    }

    static Long execute(Long t, ThrowableLongUnaryOperator function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}