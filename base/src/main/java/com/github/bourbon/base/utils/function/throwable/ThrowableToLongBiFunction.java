package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 17:24
 */
@FunctionalInterface
public interface ThrowableToLongBiFunction<T, U> extends ThrowableBiFunction<T, U, Long> {

    default ThrowableToLongBiFunction<T, U> andThen(ThrowableToLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static <T, U> Long execute(T t, U u, ThrowableToLongBiFunction<T, U> function) throws IllegalArgumentException {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}