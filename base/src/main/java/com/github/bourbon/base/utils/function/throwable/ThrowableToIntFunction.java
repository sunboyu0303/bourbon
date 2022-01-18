package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 17:22
 */
@FunctionalInterface
public interface ThrowableToIntFunction<T> extends ThrowableFunction<T, Integer> {

    default ThrowableToIntFunction<Integer> compose(ThrowableIntFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    default ThrowableToIntFunction<T> andThen(ThrowableIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return t -> a.apply(apply(t));
    }

    static ThrowableToIntFunction<Integer> identity() {
        return t -> t;
    }

    static <T> Integer execute(T t, ThrowableToIntFunction<T> function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}