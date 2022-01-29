package com.github.bourbon.base.thread;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.thread.log.ThreadLogger;
import com.github.bourbon.base.thread.space.SpaceNamedThreadFactory;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 15:25
 */
public class SofaScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static final String SIMPLE_CLASS_NAME = SofaScheduledThreadPoolExecutor.class.getSimpleName();
    private static final AtomicInteger POOL_COUNTER = new AtomicInteger(0);
    private final ThreadPoolConfig config;
    private final ThreadPoolStatistics statistics;

    public SofaScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName, String spaceName, long taskTimeout, long period, TimeUnit timeUnit) {
        super(corePoolSize, threadFactory, handler);
        this.config = ThreadPoolConfig.newBuilder().threadPoolName(CharSequenceUtils.isEmpty(threadPoolName) ? createName() : threadPoolName).spaceName(spaceName).taskTimeout(taskTimeout).period(period).timeUnit(timeUnit).build();
        this.statistics = new ThreadPoolStatistics(this);
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config, statistics);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName, long taskTimeout, long period, TimeUnit timeUnit) {
        this(corePoolSize, threadFactory, handler, threadPoolName, null, taskTimeout, period, timeUnit);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName, String spaceName) {
        this(corePoolSize, threadFactory, handler, threadPoolName, spaceName, 0, 0, null);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName) {
        this(corePoolSize, threadFactory, handler, threadPoolName, 0, 0, null);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, String threadPoolName, String spaceName) {
        super(corePoolSize);
        this.config = ThreadPoolConfig.newBuilder().threadPoolName(CharSequenceUtils.isEmpty(threadPoolName) ? createName() : threadPoolName).spaceName(spaceName).build();
        this.statistics = new ThreadPoolStatistics(this);
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config, statistics);
        if (!CharSequenceUtils.isEmpty(threadPoolName)) {
            if (!CharSequenceUtils.isEmpty(spaceName)) {
                this.setThreadFactory(new SpaceNamedThreadFactory(threadPoolName, spaceName));
            } else {
                this.setThreadFactory(new NamedThreadFactory(threadPoolName));
            }
        }
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, String threadPoolName) {
        this(corePoolSize, threadPoolName, null);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize) {
        this(corePoolSize, StringConstants.EMPTY, null);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        this(corePoolSize);
        this.setThreadFactory(threadFactory);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        this(corePoolSize);
        this.setRejectedExecutionHandler(handler);
    }

    public SofaScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        this(corePoolSize);
        this.setThreadFactory(threadFactory);
        this.setRejectedExecutionHandler(handler);
    }

    private String createName() {
        return SIMPLE_CLASS_NAME + String.format("%08x", POOL_COUNTER.getAndIncrement());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        ExecutingRunnable runner = new ExecutingRunnable(r);
        runner.setThread(t);
        this.statistics.getExecutingTasks().put(runner, Clock.currentTimeMillis());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        ExecutingRunnable runner = new ExecutingRunnable(r);
        runner.setThread(Thread.currentThread());
        this.statistics.addTotalRunningTime(Clock.currentTimeMillis() - this.statistics.getExecutingTasks().get(runner));
        this.statistics.addTotalTaskCount();
        this.statistics.getExecutingTasks().remove(runner);
    }

    @Override
    protected void terminated() {
        super.terminated();
        ThreadPoolGovernor.getInstance().unregisterThreadPoolExecutor(this.getConfig());
    }

    public synchronized void startSchedule() {
        ThreadPoolGovernor.getInstance().startMonitorThreadPool(config.getIdentity());
    }

    public synchronized void stopSchedule() {
        ThreadPoolGovernor.getInstance().stopMonitorThreadPool(config.getIdentity());
    }

    public synchronized void reschedule() {
        ThreadPoolGovernor.getInstance().restartMonitorThreadPool(config.getIdentity());
    }

    public void updateThreadPoolName(String threadPoolName) {
        ThreadPoolGovernor.getInstance().unregisterThreadPoolExecutor(config);
        this.config.setThreadPoolName(threadPoolName);
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config, statistics);
    }

    public void updatespaceName(String spaceName) {
        ThreadPoolGovernor.getInstance().unregisterThreadPoolExecutor(config);
        this.config.setSpaceName(spaceName);
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config, statistics);
    }

    public void updatePeriod(long period) {
        this.config.setPeriod(period);
        reschedule();
    }

    public void updateTaskTimeout(long taskTimeout) {
        this.config.setTaskTimeout(taskTimeout);
        ThreadLogger.info("Updated '{}' taskTimeout to {} {}", this.config.getIdentity(), taskTimeout, this.config.getTimeUnit());
    }

    public ThreadPoolConfig getConfig() {
        return config;
    }

    public ThreadPoolStatistics getStatistics() {
        return statistics;
    }
}