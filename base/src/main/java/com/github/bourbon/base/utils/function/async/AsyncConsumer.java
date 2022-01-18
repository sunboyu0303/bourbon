package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:18
 */
@FunctionalInterface
public interface AsyncConsumer<T> {

    CompletableFuture<Void> accept(T t);

    default AsyncConsumer<T> andThen(AsyncConsumer<? super T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(() -> a.accept(t));
    }

    default AsyncConsumer<T> andThenAsync(AsyncConsumer<? super T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(() -> a.accept(t));
    }

    default AsyncConsumer<T> andThenAsync(AsyncConsumer<? super T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(() -> a.accept(t), e);
    }

    default AsyncConsumer<T> compose(AsyncConsumer<? super T> b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(() -> accept(t));
    }

    default AsyncConsumer<T> composeAsync(AsyncConsumer<? super T> b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(() -> accept(t));
    }

    default AsyncConsumer<T> composeAsync(AsyncConsumer<? super T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(() -> accept(t), e);
    }
}