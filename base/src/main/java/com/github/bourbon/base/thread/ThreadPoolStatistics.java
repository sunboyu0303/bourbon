package com.github.bourbon.base.thread;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 15:01
 */
public class ThreadPoolStatistics {

    private final ThreadPoolExecutor threadPoolExecutor;

    private final Map<ExecutingRunnable, Long> executingTasks = MapUtils.newConcurrentHashMap();

    private final AtomicLong totalRunningTime = new AtomicLong();

    private final AtomicLong totalStayInQueueTime = new AtomicLong();

    private final AtomicLong totalTaskCount = new AtomicLong();

    public ThreadPoolStatistics(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public Map<ExecutingRunnable, Long> getExecutingTasks() {
        return executingTasks;
    }

    public long getQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

    public long getPoolSize() {
        return threadPoolExecutor.getPoolSize();
    }

    public void addTotalRunningTime(long runningTime) {
        totalRunningTime.addAndGet(runningTime);
    }

    public void addTotalStayInQueueTime(long stayInQueueTime) {
        totalStayInQueueTime.addAndGet(stayInQueueTime);
    }

    public void addTotalTaskCount() {
        totalTaskCount.incrementAndGet();
    }

    public long getTotalTaskCount() {
        return totalTaskCount.get();
    }

    public long getAverageRunningTime() {
        return BooleanUtils.defaultIfPredicate(this.totalTaskCount.get(), t -> t != 0, t -> this.totalRunningTime.get() / t, -1L);
    }

    public long getAverageStayInQueueTime() {
        return BooleanUtils.defaultIfPredicate(this.totalTaskCount.get(), t -> t != 0, t -> this.totalStayInQueueTime.get() / t, -1L);
    }

    public void resetAverageStatics() {
        this.totalTaskCount.set(0);
        this.totalRunningTime.set(0);
        this.totalStayInQueueTime.set(0);
    }
}