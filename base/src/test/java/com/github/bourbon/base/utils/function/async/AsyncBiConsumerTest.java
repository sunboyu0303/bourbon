package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/18 10:54
 */
public class AsyncBiConsumerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() throws ExecutionException, InterruptedException {
        AsyncBiConsumer<Integer, Integer> consumer = (t, u) -> {
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            CompletableFuture.runAsync(() -> {
                logger.info(t.toString() + StringConstants.SPACE_PLUS_SPACE + u.toString() + StringConstants.SPACE_EQUAL_SPACE + (t + u));
                completableFuture.complete(null);
            });
            return completableFuture;
        };

        AsyncBiConsumer<Integer, Integer> otherConsumer = (t, u) -> {
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            CompletableFuture.runAsync(() -> {
                logger.info(t.toString() + StringConstants.SPACE_HYPHEN_SPACE + u.toString() + StringConstants.SPACE_EQUAL_SPACE + (t - u));
                completableFuture.complete(null);
            });
            return completableFuture;
        };

        Assert.assertNull(consumer.accept(1, 2).get());
        Assert.assertNull(otherConsumer.accept(1, 2).get());
        Assert.assertNull(consumer.andThen(otherConsumer).accept(1, 2).get());
        Assert.assertNull(consumer.compose(otherConsumer).accept(1, 2).get());
    }
}