package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:27
 */
@FunctionalInterface
public interface ThrowableDoubleToLongFunction extends ThrowableDoubleFunction<Long> {

    @Override
    default ThrowableDoubleToLongFunction compose(ThrowableDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableDoubleToLongFunction identity() {
        return Double::longValue;
    }

    static Long execute(Double t, ThrowableDoubleToLongFunction function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}