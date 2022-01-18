package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:32
 */
@FunctionalInterface
public interface ThrowableIntUnaryOperator extends ThrowableUnaryOperator<Integer> {

    default ThrowableIntUnaryOperator compose(ThrowableIntUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableIntUnaryOperator andThen(ThrowableIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static ThrowableIntUnaryOperator identity() {
        return t -> t;
    }

    static Integer execute(Integer t, ThrowableIntUnaryOperator function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}