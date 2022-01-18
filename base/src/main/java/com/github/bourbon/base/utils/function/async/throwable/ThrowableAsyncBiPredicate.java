package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.github.bourbon.base.constant.BooleanConstants.TRUE;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:00
 */
@FunctionalInterface
public interface ThrowableAsyncBiPredicate<T, U> {

    CompletableFuture<Boolean> test(T t, U u) throws Exception;

    default ThrowableAsyncBiPredicate<T, U> and(ThrowableAsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenCompose(r -> TRUE.equals(r) ? execute(t, u, o) : completedFuture(false));
    }

    default ThrowableAsyncBiPredicate<T, U> andAsync(ThrowableAsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? execute(t, u, o) : completedFuture(false));
    }

    default ThrowableAsyncBiPredicate<T, U> andAsync(ThrowableAsyncBiPredicate<? super T, ? super U> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? execute(t, u, o) : completedFuture(false), e);
    }

    default ThrowableAsyncBiPredicate<T, U> negate() {
        return (t, u) -> test(t, u).thenCompose(r -> completedFuture(!r));
    }

    default ThrowableAsyncBiPredicate<T, U> negateAsync() {
        return (t, u) -> test(t, u).thenComposeAsync(r -> completedFuture(!r));
    }

    default ThrowableAsyncBiPredicate<T, U> negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return (t, u) -> test(t, u).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default ThrowableAsyncBiPredicate<T, U> or(ThrowableAsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenCompose(r -> TRUE.equals(r) ? completedFuture(true) : execute(t, u, o));
    }

    default ThrowableAsyncBiPredicate<T, U> orAsync(ThrowableAsyncBiPredicate<? super T, ? super U> o) {
        ObjectUtils.requireNonNull(o);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : execute(t, u, o));
    }

    default ThrowableAsyncBiPredicate<T, U> orAsync(ThrowableAsyncBiPredicate<? super T, ? super U> o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> test(t, u).thenComposeAsync(r -> TRUE.equals(r) ? completedFuture(true) : execute(t, u, o), e);
    }

    static <T, U> CompletableFuture<Boolean> execute(T t, U u, ThrowableAsyncBiPredicate<T, U> p) throws IllegalArgumentException {
        try {
            return p.test(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}