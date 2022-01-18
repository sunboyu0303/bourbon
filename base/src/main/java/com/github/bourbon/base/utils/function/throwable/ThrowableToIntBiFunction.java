package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 17:21
 */
@FunctionalInterface
public interface ThrowableToIntBiFunction<T, U> extends ThrowableBiFunction<T, U, Integer> {

    default ThrowableToIntBiFunction<T, U> andThen(ThrowableToIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static <T, U> Integer execute(T t, U u, ThrowableToIntBiFunction<T, U> function) throws IllegalArgumentException {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}