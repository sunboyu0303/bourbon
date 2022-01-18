package com.github.bourbon.base.utils.function.async.throwable;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:25
 */
@FunctionalInterface
public interface ThrowableAsyncSupplier<T> {

    CompletableFuture<T> get() throws Exception;

    static <T> CompletableFuture<T> execute(ThrowableAsyncSupplier<T> s) throws IllegalArgumentException {
        try {
            return s.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}