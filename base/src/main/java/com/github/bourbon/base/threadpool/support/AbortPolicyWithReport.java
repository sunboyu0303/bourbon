package com.github.bourbon.base.threadpool.support;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.threadpool.event.ThreadPoolExhaustedEvent;
import com.github.bourbon.base.threadpool.event.ThreadPoolExhaustedListener;
import com.github.bourbon.base.utils.JVMUtils;
import com.github.bourbon.base.utils.concurrent.ConcurrentHashSet;

import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 17:22
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Set<ThreadPoolExhaustedListener> listeners = new ConcurrentHashSet<>();

    private final String threadName;

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("Thread pool is EXHAUSTED! Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d), Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)",
                threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());

        if (log.isWarnEnabled()) {
            log.warn(msg);
        }

        JVMUtils.dumpJStack();

        final ThreadPoolExhaustedEvent event = new ThreadPoolExhaustedEvent(msg);
        listeners.forEach(listener -> listener.onEvent(event));

        throw new RejectedExecutionException(msg);
    }

    public void addThreadPoolExhaustedEventListener(ThreadPoolExhaustedListener l) {
        listeners.add(l);
    }

    public void removeThreadPoolExhaustedEventListener(ThreadPoolExhaustedListener l) {
        listeners.remove(l);
    }
}