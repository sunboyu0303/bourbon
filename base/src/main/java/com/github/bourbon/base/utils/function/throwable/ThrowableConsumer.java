package com.github.bourbon.base.utils.function.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:06
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {

    void accept(T t) throws Exception;

    default ThrowableConsumer<T> andThen(ThrowableConsumer<? super T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> {
            accept(t);
            a.accept(t);
        };
    }

    default ThrowableConsumer<T> compose(ThrowableConsumer<? super T> b) {
        ObjectUtils.requireNonNull(b);
        return t -> {
            b.accept(t);
            accept(t);
        };
    }

    static <T> void execute(T t, ThrowableConsumer<T> c) throws IllegalArgumentException {
        try {
            c.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}