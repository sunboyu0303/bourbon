package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:23
 */
@FunctionalInterface
public interface ThrowableIntConsumer extends ThrowableConsumer<Integer> {

    default ThrowableIntConsumer andThen(ThrowableIntConsumer after) {
        ObjectUtils.requireNonNull(after);
        return t -> {
            accept(t);
            after.accept(t);
        };
    }

    default ThrowableIntConsumer compose(ThrowableIntConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> {
            b.accept(t);
            accept(t);
        };
    }

    static void execute(Integer t, ThrowableIntConsumer consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}