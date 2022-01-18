package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:53
 */
@FunctionalInterface
public interface ThrowableToDoubleBiFunction<T, U> extends ThrowableBiFunction<T, U, Double> {

    default ThrowableToDoubleBiFunction<T, U> andThen(ThrowableToDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> a.apply(apply(t, u));
    }

    static <T, U> Double execute(T t, U u, ThrowableToDoubleBiFunction<T, U> function) throws IllegalArgumentException {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}