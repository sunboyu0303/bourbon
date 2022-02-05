package com.github.bourbon.tracer.core.appender.self;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.base.lang.clean.Cleanable;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 21:22
 */
public class TracerDaemon implements Runnable {

    private static final AtomicBoolean RUNNING = new AtomicBoolean(false);
    private static final List<TraceAppender> WATCHED_LIST = new CopyOnWriteArrayList<>();
    private static final AtomicLong SCAN_INTERVAL = new AtomicLong(60 * 60L);

    private TracerDaemon() {
    }

    public static void watch(TraceAppender traceAppender) {
        WATCHED_LIST.add(traceAppender);
    }

    @Override
    public void run() {
        while (RUNNING.get()) {
            WATCHED_LIST.forEach(Cleanable::cleanup);
            TimeUnitUtils.sleepSeconds(SCAN_INTERVAL.get());
        }
    }

    public static void setScanInterval(long scanInterval) {
        TracerDaemon.SCAN_INTERVAL.set(scanInterval);
    }

    public static void start() {
        if (RUNNING.compareAndSet(false, true)) {
            new NamedThreadFactory(TracerDaemon.class.getSimpleName(), true).newThread(new TracerDaemon()).start();
        }
    }
}