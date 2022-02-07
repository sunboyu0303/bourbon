package com.github.bourbon.base.thread;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.DateConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.thread.log.ThreadLogger;
import com.github.bourbon.base.tracer.TracerIdAdapter;
import com.github.bourbon.base.utils.ObjectUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 16:19
 */
public class ThreadPoolMonitorRunner implements Runnable {

    private static final DateTimeFormatter DATE_FORMAT = DateConstants.DEFAULT_FORMATTER.withZone(ZoneId.systemDefault());
    private final ThreadPoolConfig config;
    private final ThreadPoolStatistics statistics;

    public ThreadPoolMonitorRunner(ThreadPoolConfig threadPoolConfig, ThreadPoolStatistics threadPoolStatistics) {
        this.statistics = threadPoolStatistics;
        this.config = threadPoolConfig;
    }

    @Override
    public void run() {
        try {
            if (ThreadPoolGovernor.getInstance().isGlobalMonitorLoggable()) {
                int decayedTaskCount = 0;
                for (ExecutingRunnable runnable : statistics.getExecutingTasks()) {
                    decayedTaskCount = calculateDecayedTaskCounts(decayedTaskCount, runnable);
                }
                ThreadLogger.info("Thread pool '{}' info: [{},{},{},{},{}]", config.getIdentity(), statistics.getQueueSize(), statistics.getExecutingTasks().size(), statistics.getPoolSize() - statistics.getExecutingTasks().size(), statistics.getPoolSize(), decayedTaskCount);
                if (statistics.getTotalTaskCount() != 0) {
                    ThreadLogger.info("Thread pool '{}' average static info: [{},{}]", config.getIdentity(), statistics.getAverageStayInQueueTime(), statistics.getAverageRunningTime());
                    statistics.resetAverageStatics();
                }
            }
        } catch (Throwable e) {
            ThreadLogger.warn("ThreadPool '{}' is interrupted when running: {}", this.config.getIdentity(), e);
        }
    }

    private int calculateDecayedTaskCounts(int decayedTaskCount, ExecutingRunnable task) {
        long dequeueTime = task.getDequeueTime();
        if (dequeueTime != 0) {
            if (Clock.currentTimeMillis() - dequeueTime >= config.getTaskTimeoutMilli()) {
                ++decayedTaskCount;
                printStackTrace(task);
            }
        }
        return decayedTaskCount;
    }

    private void printStackTrace(ExecutingRunnable task) {
        if (!task.isPrinted()) {
            task.setPrinted(true);
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement e : task.getThread().getStackTrace()) {
                sb.append(StringConstants.SPACE_SPACE_SPACE_SPACE).append(e).append(CharConstants.LF);
            }
            String traceId = ObjectUtils.defaultIfNullElseFunction(TracerIdAdapter.getInstance().traceIdSafari(task.getThread()), id -> " with traceId " + id, StringConstants.EMPTY);
            ThreadLogger.warn("Task {} in thread pool {} started on {}{} exceeds the limit of {} execution time with stack trace:\n    {}", task, config.getIdentity(), DATE_FORMAT.format(Instant.ofEpochMilli(task.getDequeueTime())), traceId, config.getTaskTimeout() + config.getTimeUnit().toString(), sb.toString().trim());
        }
    }
}