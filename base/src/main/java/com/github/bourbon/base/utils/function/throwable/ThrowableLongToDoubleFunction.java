package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:40
 */
@FunctionalInterface
public interface ThrowableLongToDoubleFunction extends ThrowableLongFunction<Double> {

    @Override
    default ThrowableLongToDoubleFunction compose(ThrowableLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableLongToDoubleFunction identity() {
        return Long::doubleValue;
    }

    static Double execute(Long t, ThrowableLongToDoubleFunction function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}