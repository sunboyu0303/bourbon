package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 17:25
 */
@FunctionalInterface
public interface ThrowableToLongFunction<T> extends ThrowableFunction<T, Long> {

    default ThrowableToLongFunction<Long> compose(ThrowableLongFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableToLongFunction<T> andThen(ThrowableLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static ThrowableToLongFunction<Long> identity() {
        return t -> t;
    }

    static <T> Long execute(T t, ThrowableToLongFunction<T> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}