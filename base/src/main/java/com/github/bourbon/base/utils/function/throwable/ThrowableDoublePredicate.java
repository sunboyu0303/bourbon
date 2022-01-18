package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:23
 */
@FunctionalInterface
public interface ThrowableDoublePredicate extends ThrowablePredicate<Double> {

    default ThrowableDoublePredicate and(ThrowableDoublePredicate other) {
        ObjectUtils.requireNonNull(other);
        return v -> test(v) && other.test(v);
    }

    @Override
    default ThrowableDoublePredicate negate() {
        return v -> !test(v);
    }

    default ThrowableDoublePredicate or(ThrowableDoublePredicate other) {
        ObjectUtils.requireNonNull(other);
        return v -> test(v) || other.test(v);
    }

    static boolean execute(Double t, ThrowableDoublePredicate predicate) throws IllegalArgumentException {
        try {
            return predicate.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}