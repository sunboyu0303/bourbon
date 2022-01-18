package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.github.bourbon.base.constant.BooleanConstants.TRUE;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:27
 */
@FunctionalInterface
public interface ThrowableAsyncPredicate<T> {

    CompletableFuture<Boolean> test(T t) throws Exception;

    default ThrowableAsyncPredicate<T> and(ThrowableAsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncPredicate<T> andAsync(ThrowableAsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncPredicate<T> andAsync(ThrowableAsyncPredicate<? super T> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? execute(t, o) : completedFuture(false), e);
    }

    default ThrowableAsyncPredicate<T> negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    default ThrowableAsyncPredicate<T> negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    default ThrowableAsyncPredicate<T> negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default ThrowableAsyncPredicate<T> or(ThrowableAsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncPredicate<T> orAsync(ThrowableAsyncPredicate<? super T> o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncPredicate<T> orAsync(ThrowableAsyncPredicate<? super T> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : execute(t, o), e);
    }

    static <T> CompletableFuture<Boolean> execute(T t, ThrowableAsyncPredicate<T> p) throws IllegalArgumentException {
        try {
            return p.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}