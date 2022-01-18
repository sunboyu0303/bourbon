package com.github.bourbon.base.utils.function.async;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/18 18:20
 */
public class AsyncBiFunctionTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        AsyncBiFunction<Integer, Integer, Long> biFunction = (i1, i2) -> CompletableFuture.completedFuture(new Integer(i1 + i2).longValue());
        Assert.assertTrue(biFunction.apply(1, 2).get() == 3L);
        AsyncFunction<Long, String> function = l -> CompletableFuture.completedFuture(l.toString());
        Assert.assertEquals(biFunction.andThen(function).apply(1, 2).get(), "3");
    }
}