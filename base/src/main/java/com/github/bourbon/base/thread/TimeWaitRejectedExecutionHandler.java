package com.github.bourbon.base.thread;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.thread.log.ThreadLogger;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 13:24
 */
public class TimeWaitRejectedExecutionHandler implements RejectedExecutionHandler {

    private RejectedExecutionHandler delegate;
    private TimeWaitRunner timeWaitRunner;
    private SofaThreadPoolExecutor threadPoolExecutor;

    public TimeWaitRejectedExecutionHandler(SofaThreadPoolExecutor executor, long waitTime, TimeUnit timeUnit) {
        this.timeWaitRunner = new TimeWaitRunner(timeUnit.toMillis(waitTime));
        this.delegate = executor.getRejectedExecutionHandler();
        this.threadPoolExecutor = executor;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        timeWaitRunner.doWithRunnable(this::logStackTrace);
        delegate.rejectedExecution(r, executor);
    }

    private void logStackTrace() {
        if (threadPoolExecutor != null) {
            String threadPoolName = threadPoolExecutor.getConfig().getThreadPoolName();
            String allStackTrace = getAllStackTrace(threadPoolExecutor);
            ThreadLogger.error("Queue of thread pool {} is full with all stack trace: \n    {}\n\n", threadPoolName, allStackTrace);
        }
    }

    private String getAllStackTrace(SofaThreadPoolExecutor threadPoolExecutor) {
        StringBuilder sb = new StringBuilder();
        threadPoolExecutor.getStatistics().getExecutingTasks().forEach(runnable -> {
            for (StackTraceElement e : runnable.getThread().getStackTrace()) {
                sb.append(StringConstants.SPACE_SPACE_SPACE_SPACE).append(e).append(CharConstants.LF);
            }
        });
        return sb.toString();
    }

    public RejectedExecutionHandler getDelegate() {
        return delegate;
    }

    public void setDelegate(RejectedExecutionHandler delegate) {
        this.delegate = delegate;
    }

    public TimeWaitRunner getTimeWaitRunner() {
        return timeWaitRunner;
    }

    public void setTimeWaitRunner(TimeWaitRunner timeWaitRunner) {
        this.timeWaitRunner = timeWaitRunner;
    }

    public SofaThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(SofaThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}