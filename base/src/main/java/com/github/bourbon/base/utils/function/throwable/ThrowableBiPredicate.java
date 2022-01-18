package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:06
 */
@FunctionalInterface
public interface ThrowableBiPredicate<T, U> {

    boolean test(T t, U u) throws Exception;

    default ThrowableBiPredicate<T, U> and(ThrowableBiPredicate<? super T, ? super U> other) {
        ObjectUtils.requireNonNull(other);
        return (t, u) -> test(t, u) && other.test(t, u);
    }

    default ThrowableBiPredicate<T, U> negate() {
        return (t, u) -> !test(t, u);
    }

    default ThrowableBiPredicate<T, U> or(ThrowableBiPredicate<? super T, ? super U> other) {
        ObjectUtils.requireNonNull(other);
        return (t, u) -> test(t, u) || other.test(t, u);
    }

    static <T, U> boolean execute(T t, U u, ThrowableBiPredicate<T, U> predicate) throws IllegalArgumentException {
        try {
            return predicate.test(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}