package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.TimeUnitUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/18 10:40
 */
public class AsyncActionTest {

    @Test
    public void test() throws InterruptedException, ExecutionException, TimeoutException {
        AsyncAction action = () -> {
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            CompletableFuture.runAsync(() -> {
                TimeUnitUtils.sleepMilliSeconds(500L);
                completableFuture.complete(null);
            });
            return completableFuture;
        };

        Assert.assertNull(action.execute().get(300, TimeUnit.SECONDS));
    }
}