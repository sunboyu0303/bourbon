package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:38
 */
@FunctionalInterface
public interface ThrowableLongPredicate extends ThrowablePredicate<Long> {

    default ThrowableLongPredicate and(ThrowableLongPredicate other) {
        ObjectUtils.requireNonNull(other);
        return v -> test(v) && other.test(v);
    }

    @Override
    default ThrowableLongPredicate negate() {
        return v -> !test(v);
    }

    default ThrowableLongPredicate or(ThrowableLongPredicate other) {
        ObjectUtils.requireNonNull(other);
        return v -> test(v) || other.test(v);
    }

    static boolean execute(Long t, ThrowableLongPredicate predicate) throws IllegalArgumentException {
        try {
            return predicate.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}