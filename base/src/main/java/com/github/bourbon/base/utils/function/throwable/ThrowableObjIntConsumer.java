package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:49
 */
@FunctionalInterface
public interface ThrowableObjIntConsumer<T> extends ThrowableBiConsumer<T, Integer> {

    default ThrowableObjIntConsumer<T> andThen(ThrowableObjIntConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> {
            accept(l, r);
            a.accept(l, r);
        };
    }

    default ThrowableObjIntConsumer<T> compose(ThrowableObjIntConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> {
            b.accept(l, r);
            accept(l, r);
        };
    }

    static <T> void execute(T t, Integer u, ThrowableObjIntConsumer<T> consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}