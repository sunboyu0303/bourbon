package com.github.bourbon.base.thread;

import com.github.bourbon.base.thread.log.ThreadLogger;
import com.github.bourbon.base.utils.SystemUtils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 16:13
 */
public class ThreadPoolMonitorWrapper {

    private final ThreadPoolExecutor threadPoolExecutor;

    private final ThreadPoolConfig threadPoolConfig;

    private final ThreadPoolStatistics threadPoolStatistics;

    private ScheduledFuture<?> scheduledFuture;

    public ThreadPoolMonitorWrapper(ThreadPoolExecutor threadPoolExecutor, ThreadPoolConfig threadPoolConfig, ThreadPoolStatistics threadPoolStatistics) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.threadPoolConfig = threadPoolConfig;
        this.threadPoolStatistics = threadPoolStatistics;
    }

    public void startMonitor() {
        if (SystemUtils.get(SofaThreadPoolConstants.SOFA_THREAD_POOL_LOGGING_CAPABILITY, true)) {
            synchronized (this) {
                if (scheduledFuture != null) {
                    ThreadLogger.warn("Thread pool '{}' is already started with period: {} {}", threadPoolConfig.getIdentity(), threadPoolConfig.getPeriod(), threadPoolConfig.getTimeUnit());
                } else {
                    scheduledFuture = ThreadPoolGovernor.getInstance().getMonitorScheduler().scheduleAtFixedRate(new ThreadPoolMonitorRunner(threadPoolConfig, threadPoolStatistics), threadPoolConfig.getPeriod(), threadPoolConfig.getPeriod(), threadPoolConfig.getTimeUnit());
                    ThreadLogger.info("Thread pool '{}' started with period: {} {}", threadPoolConfig.getIdentity(), threadPoolConfig.getPeriod(), threadPoolConfig.getTimeUnit());
                }
            }
        }
    }

    public void stopMonitor() {
        synchronized (this) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
                ThreadLogger.info("Thread pool '{}' stopped.", threadPoolConfig.getIdentity());
            } else {
                ThreadLogger.warn("Thread pool '{}' is not scheduling!", threadPoolConfig.getIdentity());
            }
        }
    }

    public void restartMonitor() {
        synchronized (this) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            scheduledFuture = ThreadPoolGovernor.getInstance().getMonitorScheduler().scheduleAtFixedRate(new ThreadPoolMonitorRunner(threadPoolConfig, threadPoolStatistics), threadPoolConfig.getPeriod(), threadPoolConfig.getPeriod(), threadPoolConfig.getTimeUnit());
            ThreadLogger.info("Restart thread pool '{}' with period: {} {}", threadPoolConfig.getIdentity(), threadPoolConfig.getPeriod(), threadPoolConfig.getTimeUnit());
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public ThreadPoolConfig getThreadPoolConfig() {
        return threadPoolConfig;
    }

    public boolean isStarted() {
        return this.scheduledFuture != null;
    }
}