package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:28
 */
@FunctionalInterface
public interface ThrowableIntToDoubleFunction extends ThrowableIntFunction<Double> {

    @Override
    default ThrowableIntToDoubleFunction compose(ThrowableIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableIntToDoubleFunction identity() {
        return Integer::doubleValue;
    }

    static Double execute(Integer t, ThrowableIntToDoubleFunction function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}