package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:26
 */
@FunctionalInterface
public interface ThrowableIntPredicate extends ThrowablePredicate<Integer> {

    default ThrowableIntPredicate and(ThrowableIntPredicate other) {
        ObjectUtils.requireNonNull(other);
        return v -> test(v) && other.test(v);
    }

    @Override
    default ThrowableIntPredicate negate() {
        return v -> !test(v);
    }

    default ThrowableIntPredicate or(ThrowableIntPredicate other) {
        ObjectUtils.requireNonNull(other);
        return v -> test(v) || other.test(v);
    }

    static boolean execute(Integer t, ThrowableIntPredicate predicate) throws IllegalArgumentException {
        try {
            return predicate.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}