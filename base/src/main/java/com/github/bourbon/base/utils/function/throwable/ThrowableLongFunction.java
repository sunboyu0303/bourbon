package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:36
 */
@FunctionalInterface
public interface ThrowableLongFunction<R> extends ThrowableFunction<Long, R> {

    default ThrowableLongFunction<R> compose(ThrowableLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableLongFunction<Long> identity() {
        return t -> t;
    }

    static <R> R execute(Long t, ThrowableLongFunction<R> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}