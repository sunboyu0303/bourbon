package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:12
 */
@FunctionalInterface
public interface AsyncBiConsumer<T, U> {

    CompletableFuture<Void> accept(T t, U u);

    default AsyncBiConsumer<T, U> andThen(AsyncBiConsumer<? super T, ? super U> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRun(() -> a.accept(l, r));
    }

    default AsyncBiConsumer<T, U> andThenAsync(AsyncBiConsumer<? super T, ? super U> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRunAsync(() -> a.accept(l, r));
    }

    default AsyncBiConsumer<T, U> andThenAsync(AsyncBiConsumer<? super T, ? super U> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> accept(l, r).thenRunAsync(() -> a.accept(l, r), e);
    }

    default AsyncBiConsumer<T, U> compose(AsyncBiConsumer<? super T, ? super U> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRun(() -> accept(l, r));
    }

    default AsyncBiConsumer<T, U> composeAsync(AsyncBiConsumer<? super T, ? super U> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(() -> accept(l, r));
    }

    default AsyncBiConsumer<T, U> composeAsync(AsyncBiConsumer<? super T, ? super U> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(() -> accept(l, r), e);
    }
}