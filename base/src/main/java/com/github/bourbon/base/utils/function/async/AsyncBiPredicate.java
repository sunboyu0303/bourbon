package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.github.bourbon.base.constant.BooleanConstants.TRUE;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:16
 */
@FunctionalInterface
public interface AsyncBiPredicate<T, U> {

    CompletableFuture<Boolean> test(T t, U u);

    default AsyncBiPredicate<T, U> and(AsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenCompose(r -> TRUE.equals(r) ? o.test(t, u) : completedFuture(false));
    }

    default AsyncBiPredicate<T, U> andAsync(AsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? o.test(t, u) : completedFuture(false));
    }

    default AsyncBiPredicate<T, U> andAsync(AsyncBiPredicate<? super T, ? super U> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? o.test(t, u) : completedFuture(false), e);
    }

    default AsyncBiPredicate<T, U> negate() {
        return (t, u) -> test(t, u).thenCompose(r -> completedFuture(!r));
    }

    default AsyncBiPredicate<T, U> negateAsync() {
        return (t, u) -> test(t, u).thenComposeAsync(r -> completedFuture(!r));
    }

    default AsyncBiPredicate<T, U> negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return (t, u) -> test(t, u).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default AsyncBiPredicate<T, U> or(AsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenCompose(r -> TRUE.equals(r) ? completedFuture(true) : o.test(t, u));
    }

    default AsyncBiPredicate<T, U> orAsync(AsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : o.test(t, u));
    }

    default AsyncBiPredicate<T, U> orAsync(AsyncBiPredicate<? super T, ? super U> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : o.test(t, u), e);
    }
}