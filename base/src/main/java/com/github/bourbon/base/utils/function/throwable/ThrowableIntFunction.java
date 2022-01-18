package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:24
 */
@FunctionalInterface
public interface ThrowableIntFunction<R> extends ThrowableFunction<Integer, R> {

    default ThrowableIntFunction<R> compose(ThrowableIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableIntFunction<Integer> identity() {
        return t -> t;
    }

    static <R> R execute(Integer t, ThrowableIntFunction<R> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}