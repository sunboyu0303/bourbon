package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:47
 */
@FunctionalInterface
public interface ThrowableObjDoubleConsumer<T> extends ThrowableBiConsumer<T, Double> {

    default ThrowableObjDoubleConsumer<T> andThen(ThrowableObjDoubleConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> {
            accept(l, r);
            a.accept(l, r);
        };
    }

    default ThrowableObjDoubleConsumer<T> compose(ThrowableObjDoubleConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> {
            b.accept(l, r);
            accept(l, r);
        };
    }

    static <T> void execute(T t, Double u, ThrowableObjDoubleConsumer<T> consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}