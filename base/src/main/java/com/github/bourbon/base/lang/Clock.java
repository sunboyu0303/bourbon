package com.github.bourbon.base.lang;

import com.github.bourbon.base.lang.statistic.LeapArray;
import com.github.bourbon.base.lang.statistic.WindowWrap;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 11:22
 */
public final class Clock implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final long CHECK_INTERVAL = 3000;
    private static final long HITS_LOWER_BOUNDARY = 800;
    private static final long HITS_UPPER_BOUNDARY = 1200;

    private static final Clock INSTANCE = new Clock();

    enum STATE {
        IDLE, PREPARE, RUNNING
    }

    private static class Statistic {
        private final LongAdder writes = new LongAdder();
        private final LongAdder reads = new LongAdder();

        private long writeLongValue() {
            return writes.longValue();
        }

        private long readLongValue() {
            return reads.longValue();
        }

        private void writeInc() {
            writes.increment();
        }

        private void readInc() {
            reads.increment();
        }

        private void reset() {
            reads.reset();
            writes.reset();
        }
    }

    private volatile long currentTimeMillis;
    private volatile STATE state = STATE.IDLE;
    private volatile long lastCheck;

    private final LeapArray<Statistic> statistics;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private Clock() {
        statistics = new LeapArray<Statistic>(3, CHECK_INTERVAL) {
            @Override
            public Statistic newEmptyBucket(long timeMillis) {
                return new Statistic();
            }

            @Override
            protected WindowWrap<Statistic> resetWindowTo(WindowWrap<Statistic> windowWrap, long startTime) {
                windowWrap.get().reset();
                windowWrap.resetTo(startTime);
                return windowWrap;
            }
        };
        currentTimeMillis = System.currentTimeMillis();
        lastCheck = currentTimeMillis;
        new NamedThreadFactory("Clock-tick-thread", true).newThread(this).start();
    }

    public static long currentTimeMillis() {
        return INSTANCE.getTime();
    }

    public long getTime() {
        return currentTime(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            check();
            if (state == STATE.RUNNING) {
                currentTimeMillis = System.currentTimeMillis();
                statistics.currentWindow(currentTimeMillis).get().writeInc();
                TimeUnitUtils.sleepMilliSeconds(1);
            } else if (state == STATE.IDLE) {
                TimeUnitUtils.sleepMilliSeconds(300);
            } else if (state == STATE.PREPARE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("TimeUtil switches to RUNNING");
                }
                currentTimeMillis = System.currentTimeMillis();
                state = STATE.RUNNING;
            }
        }
    }

    private void check() {
        long now = currentTime(true);
        if (now - lastCheck < CHECK_INTERVAL) {
            return;
        }
        lastCheck = now;
        Pair<Long, Long> qps = currentQps(now);
        if (state == STATE.IDLE && qps.r1 > HITS_UPPER_BOUNDARY) {
            if (logger.isInfoEnabled()) {
                logger.info("TimeUtil switches to PREPARE for better performance, reads=" + qps.r1 + "/s, writes=" + qps.r2 + "/s");
            }
            state = STATE.PREPARE;
        } else if (state == STATE.RUNNING && qps.r1 < HITS_LOWER_BOUNDARY) {
            if (logger.isInfoEnabled()) {
                logger.info("TimeUtil switches to IDLE due to not enough load, reads=" + qps.r1 + "/s, writes=" + qps.r2 + "/s");
            }
            state = STATE.IDLE;
        }
    }

    private long currentTime(boolean innerCall) {
        long now = currentTimeMillis;
        Statistic statistic = statistics.currentWindow(now).get();
        if (!innerCall) {
            statistic.readInc();
        }
        if (state == STATE.IDLE || state == STATE.PREPARE) {
            now = System.currentTimeMillis();
            currentTimeMillis = now;
            if (!innerCall) {
                statistic.writeInc();
            }
        }
        return now;
    }

    private Pair<Long, Long> currentQps(long now) {
        AtomicLong reads = new AtomicLong();
        AtomicLong writes = new AtomicLong();
        AtomicInteger cnt = new AtomicInteger();
        for (WindowWrap<Statistic> windowWrap : statistics.listAll()) {
            if (windowWrap.isTimeInWindow(now)) {
                continue;
            }
            cnt.incrementAndGet();
            Statistic statistic = windowWrap.get();
            reads.getAndAdd(statistic.readLongValue());
            writes.getAndAdd(statistic.writeLongValue());
        }
        int c = cnt.get();
        return BooleanUtils.defaultSupplierIfFalse(c == 0, () -> Pair.of(0L, 0L), () -> Pair.of(reads.get() / c, writes.get() / c));
    }
}