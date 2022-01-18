package com.github.bourbon.base.utils.function.async;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 17:28
 */
@FunctionalInterface
public interface AsyncAction {

    CompletableFuture<Void> execute();
}