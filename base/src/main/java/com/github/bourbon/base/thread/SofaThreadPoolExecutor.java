package com.github.bourbon.base.thread;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.thread.log.ThreadLogger;
import com.github.bourbon.base.thread.space.SpaceNamedThreadFactory;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 16:42
 */
public class SofaThreadPoolExecutor extends ThreadPoolExecutor {

    private static final String SIMPLE_CLASS_NAME = SofaThreadPoolExecutor.class.getSimpleName();
    private static final AtomicInteger POOL_COUNTER = new AtomicInteger(0);
    private final ThreadPoolConfig config;
    private final ThreadPoolStatistics statistics;

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName, String spaceName, long taskTimeout, long period, TimeUnit timeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.config = ThreadPoolConfig.newBuilder().threadPoolName(CharSequenceUtils.isEmpty(threadPoolName) ? createName() : threadPoolName).spaceName(spaceName).taskTimeout(taskTimeout).period(period).timeUnit(timeUnit).build();
        this.statistics = new ThreadPoolStatistics(this);
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config, statistics);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName, long taskTimeout, long period, TimeUnit timeUnit) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler, threadPoolName, null, taskTimeout, period, timeUnit);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName, String spaceName) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler, threadPoolName, spaceName, 0, 0, null);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler, threadPoolName, 0, 0, null);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String threadPoolName, String spaceName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
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

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String threadPoolName) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadPoolName, null);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, StringConstants.EMPTY, null);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.setThreadFactory(threadFactory);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.setRejectedExecutionHandler(handler);
    }

    public SofaThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.setThreadFactory(threadFactory);
        this.setRejectedExecutionHandler(handler);
    }

    private String createName() {
        return SIMPLE_CLASS_NAME + StringConstants.HYPHEN + POOL_COUNTER.getAndIncrement();
    }

    @Override
    public void execute(Runnable command) {
        ExecutingRunnable executingRunnable;
        if (command instanceof ExecutingRunnable) {
            executingRunnable = (ExecutingRunnable) command;
        } else {
            executingRunnable = new ExecutingRunnable(command, Thread.currentThread());
        }
        super.execute(executingRunnable.setEnqueueTime(Clock.currentTimeMillis()));
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (r instanceof ExecutingRunnable) {
            ExecutingRunnable executingRunnable = (ExecutingRunnable) r;
            executingRunnable.setDequeueTime(Clock.currentTimeMillis());
            this.statistics.getExecutingTasks().add(executingRunnable);
        }
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (r instanceof ExecutingRunnable) {
            ExecutingRunnable executingRunnable = (ExecutingRunnable) r;
            executingRunnable.setFinishTime(Clock.currentTimeMillis());
            this.statistics.addTotalStayInQueueTime(executingRunnable.getStayInQueueTime());
            this.statistics.addTotalRunningTime(executingRunnable.getRunningTime());
            this.statistics.addTotalTaskCount();
            this.statistics.getExecutingTasks().remove(executingRunnable);
        }
    }

    @Override
    protected void terminated() {
        super.terminated();
        ThreadPoolGovernor.getInstance().unregisterThreadPoolExecutor(config);
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
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config.setThreadPoolName(threadPoolName), statistics);
    }

    public void updateSpaceName(String spaceName) {
        ThreadPoolGovernor.getInstance().unregisterThreadPoolExecutor(config);
        ThreadPoolGovernor.getInstance().registerThreadPoolExecutor(this, config.setSpaceName(spaceName), statistics);
    }

    public void updatePeriod(long period) {
        this.config.setPeriod(period);
        reschedule();
    }

    public void updateTaskTimeout(long taskTimeout) {
        this.config.setTaskTimeout(taskTimeout);
        ThreadLogger.info("Updated '{}' taskTimeout to {} {}", this.config.getIdentity(), taskTimeout, config.getTimeUnit());
    }
}