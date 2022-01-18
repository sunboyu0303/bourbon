package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:24
 */
@FunctionalInterface
public interface ThrowablePredicate<T> {

    boolean test(T t) throws Exception;

    default ThrowablePredicate<T> and(ThrowablePredicate<? super T> other) {
        ObjectUtils.requireNonNull(other);
        return t -> test(t) && other.test(t);
    }

    default ThrowablePredicate<T> negate() {
        return t -> !test(t);
    }

    default ThrowablePredicate<T> or(ThrowablePredicate<? super T> other) {
        ObjectUtils.requireNonNull(other);
        return t -> test(t) || other.test(t);
    }

    static <T> boolean execute(T t, ThrowablePredicate<T> predicate) throws IllegalArgumentException {
        try {
            return predicate.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}