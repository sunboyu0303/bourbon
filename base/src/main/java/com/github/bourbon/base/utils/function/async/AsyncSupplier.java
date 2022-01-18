package com.github.bourbon.base.utils.function.async;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:19
 */
@FunctionalInterface
public interface AsyncSupplier<T> {

    CompletableFuture<T> get();
}