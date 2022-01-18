package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:43
 */
@FunctionalInterface
public interface ThrowableLongToIntFunction extends ThrowableLongFunction<Integer> {

    @Override
    default ThrowableLongToIntFunction compose(ThrowableLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> apply(b.apply(v));
    }

    static ThrowableLongToIntFunction identity() {
        return Long::intValue;
    }

    static Integer execute(Long t, ThrowableLongToIntFunction function) throws IllegalArgumentException {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}