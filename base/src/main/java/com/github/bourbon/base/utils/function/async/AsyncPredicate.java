package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.lang.Boolean.TRUE;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:18
 */
@FunctionalInterface
public interface AsyncPredicate<T> {

    CompletableFuture<Boolean> test(T t);

    default AsyncPredicate<T> and(AsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncPredicate<T> andAsync(AsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncPredicate<T> andAsync(AsyncPredicate<? super T> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? o.test(t) : completedFuture(false), e);
    }

    default AsyncPredicate<T> negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    default AsyncPredicate<T> negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    default AsyncPredicate<T> negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default AsyncPredicate<T> or(AsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncPredicate<T> orAsync(AsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncPredicate<T> orAsync(AsyncPredicate<? super T> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : o.test(t), e);
    }
}