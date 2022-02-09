package com.github.bourbon.tracer.core.reporter.stat.manager;

import com.github.bourbon.base.constant.StringConstants;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 11:22
 */
public class SofaTracerStatisticReporterThreadFactory implements ThreadFactory {

    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(0);

    private final long cycleTime;

    public SofaTracerStatisticReporterThreadFactory(long cycleTime) {
        this.cycleTime = cycleTime;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = new Thread(r, "Tracer-TimedAppender-" + THREAD_NUMBER.incrementAndGet() + StringConstants.HYPHEN + cycleTime);
        thread.setDaemon(true);
        return thread;
    }
}