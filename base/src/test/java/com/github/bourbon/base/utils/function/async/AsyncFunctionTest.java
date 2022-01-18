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
 * @date 2022/1/18 16:50
 */
public class AsyncFunctionTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() throws ExecutionException, InterruptedException {
        AsyncFunction<Integer, String> firstFunction = i -> CompletableFuture.completedFuture(i.toString());
        logger.info(firstFunction.apply(123).get());
        AsyncFunction<String, Long> secondFunction = s -> CompletableFuture.completedFuture(Long.parseLong(s));
        logger.info(secondFunction.apply("123").get());
        logger.info(firstFunction.andThen(secondFunction).apply(123).get());
        AsyncFunction<Long, Double> thirdFunction = l -> CompletableFuture.completedFuture(l / 10d);
        logger.info(thirdFunction.apply(123L).get());
        Double d = thirdFunction.compose(firstFunction.andThen(secondFunction)).apply(123).get();
        Assert.assertTrue(d == 12.3);
    }
}