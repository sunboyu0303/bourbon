package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:50
 */
@FunctionalInterface
public interface ThrowableObjLongConsumer<T> extends ThrowableBiConsumer<T, Long> {

    default ThrowableObjLongConsumer<T> andThen(ThrowableObjLongConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> {
            accept(l, r);
            a.accept(l, r);
        };
    }

    default ThrowableObjLongConsumer<T> compose(ThrowableObjLongConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> {
            b.accept(l, r);
            accept(l, r);
        };
    }

    static <T> void execute(T t, Long u, ThrowableObjLongConsumer<T> consumer) throws IllegalArgumentException {
        try {
            consumer.accept(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}