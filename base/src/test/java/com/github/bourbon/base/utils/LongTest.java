package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 17:48
 */
public class LongTest {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final int POOL_SIZE = 1000;

    @Test
    public void test() {
        testAtomicLong(100);
        testAtomicLong(1000);
        testAtomicLong(10000);
        testAtomicLong(50000);

        testLongAdder(100);
        testLongAdder(1000);
        testLongAdder(10000);
        testLongAdder(50000);
    }

    private void testAtomicLong(int len) {
        ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);
        AtomicLong counter = new AtomicLong(0);

        long start = Clock.currentTimeMillis();
        ArrayList<Future> futures = new ArrayList<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE * 100; i++) {
            futures.add(service.submit(new AtomicLongTask(counter, len)));
        }

        for (Future future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        LOGGER.info("统计结果为：[" + numberFormat.format(counter.get()) + "]");
        LOGGER.info("耗时：[" + (Clock.currentTimeMillis() - start) + "]毫秒");
        service.shutdown();
    }

    static class AtomicLongTask implements Runnable {

        private final AtomicLong counter;
        private final int len;

        AtomicLongTask(AtomicLong counter, int len) {
            this.counter = counter;
            this.len = len;
        }

        @Override
        public void run() {
            for (int i = 0; i < len; i++) {
                counter.incrementAndGet();
            }
        }
    }

    private void testLongAdder(int len) {

        LongAdder counter = new LongAdder();
        ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);

        long start = Clock.currentTimeMillis();
        ArrayList<Future> futures = new ArrayList<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE * 100; i++) {
            futures.add(service.submit(new LongAdderTask(counter, len)));
        }

        for (Future future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        LOGGER.info("统计结果为：[" + numberFormat.format(counter.sum()) + "]");
        LOGGER.info("耗时：[" + (Clock.currentTimeMillis() - start) + "]毫秒");
        service.shutdown();
    }

    static class LongAdderTask implements Runnable {

        private final LongAdder counter;
        private final int len;

        LongAdderTask(LongAdder counter, int len) {
            this.counter = counter;
            this.len = len;
        }

        @Override
        public void run() {
            for (int i = 0; i < len; i++) {
                counter.increment();
            }
        }
    }
}