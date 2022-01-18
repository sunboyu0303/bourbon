package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:26
 */
@FunctionalInterface
public interface ThrowableDoubleToIntFunction extends ThrowableDoubleFunction<Integer> {

    @Override
    default ThrowableDoubleToIntFunction compose(ThrowableDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableDoubleToIntFunction identity() {
        return Double::intValue;
    }

    static Integer execute(Double t, ThrowableDoubleToIntFunction function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}