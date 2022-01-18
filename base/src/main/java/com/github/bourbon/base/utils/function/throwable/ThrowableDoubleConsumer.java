package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:14
 */
@FunctionalInterface
public interface ThrowableDoubleConsumer extends ThrowableConsumer<Double> {

    default ThrowableDoubleConsumer andThen(ThrowableDoubleConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> {
            accept(t);
            a.accept(t);
        };
    }

    default ThrowableDoubleConsumer compose(ThrowableDoubleConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> {
            b.accept(t);
            accept(t);
        };
    }

    static void execute(Double t, ThrowableDoubleConsumer consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}