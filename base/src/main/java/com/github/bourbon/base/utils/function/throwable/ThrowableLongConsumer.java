package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:36
 */
@FunctionalInterface
public interface ThrowableLongConsumer extends ThrowableConsumer<Long> {

    default ThrowableLongConsumer andThen(ThrowableLongConsumer after) {
        ObjectUtils.requireNonNull(after);
        return t -> {
            accept(t);
            after.accept(t);
        };
    }

    default ThrowableLongConsumer compose(ThrowableLongConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> {
            b.accept(t);
            accept(t);
        };
    }

    static void execute(Long t, ThrowableLongConsumer consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}