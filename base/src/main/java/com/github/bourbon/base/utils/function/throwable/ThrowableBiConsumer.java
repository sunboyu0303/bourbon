package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 17:15
 */
@FunctionalInterface
public interface ThrowableBiConsumer<T, U> {

    void accept(T t, U u) throws Exception;

    default ThrowableBiConsumer<T, U> andThen(ThrowableBiConsumer<? super T, ? super U> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> {
            accept(l, r);
            a.accept(l, r);
        };
    }

    default ThrowableBiConsumer<T, U> compose(ThrowableBiConsumer<? super T, ? super U> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> {
            b.accept(l, r);
            accept(l, r);
        };
    }

    static <T, U> void execute(T t, U u, ThrowableBiConsumer<T, U> consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}