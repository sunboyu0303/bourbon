package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/18 18:32
 */
public class AsyncUnaryOperatorTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() throws ExecutionException, InterruptedException {
        AsyncUnaryOperator<Integer> operator = i -> CompletableFuture.completedFuture(i << 2);
        logger.error(operator.apply(8).get());
        AsyncUnaryOperator<Integer> otherOperator = i -> CompletableFuture.completedFuture(i >> 2);
        logger.error(otherOperator.apply(8).get());
        Assert.assertTrue(operator.andThen(otherOperator).apply(8).get() == 8);
        Assert.assertTrue(operator.compose(otherOperator).apply(8).get() == 8);
    }
}