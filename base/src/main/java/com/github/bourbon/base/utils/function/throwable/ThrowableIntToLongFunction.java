package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:31
 */
@FunctionalInterface
public interface ThrowableIntToLongFunction extends ThrowableIntFunction<Long> {

    @Override
    default ThrowableIntToLongFunction compose(ThrowableIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableIntToLongFunction identity() {
        return Integer::longValue;
    }

    static Long execute(Integer t, ThrowableIntToLongFunction function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}