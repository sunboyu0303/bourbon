package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/16 10:09
 */
public class AsyncBaseServiceTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ScheduledExecutorService scheduledExecutorService;

    @Before
    public void before() {
        scheduledExecutorService = ExecutorFactory.Managed.newSingleScheduledExecutorService("test", new NamedThreadFactory("Async-Base-Service-Test", true));
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Assert.assertTrue(AsyncBaseService.scheduleRunnable(scheduledExecutorService, () -> logger.info(123), 1, TimeUnit.SECONDS).get());
    }

    @After
    public void after() {
        scheduledExecutorService.shutdown();
    }
}