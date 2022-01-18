package com.github.bourbon.base.utils.function.async.throwable;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:12
 */
@FunctionalInterface
public interface ThrowableAsyncLongSupplier extends ThrowableAsyncSupplier<Long> {

    static CompletableFuture<Long> execute(ThrowableAsyncLongSupplier s) throws IllegalArgumentException {
        try {
            return s.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}