package com.github.bourbon.base.utils.function.async.throwable;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:04
 */
@FunctionalInterface
public interface ThrowableAsyncBooleanSupplier extends ThrowableAsyncSupplier<Boolean> {

    static CompletableFuture<Boolean> execute(ThrowableAsyncBooleanSupplier s) throws IllegalArgumentException {
        try {
            return s.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}