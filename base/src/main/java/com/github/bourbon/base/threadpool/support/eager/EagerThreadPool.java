package com.github.bourbon.base.threadpool.support.eager;

import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.threadlocal.NamedInternalThreadFactory;
import com.github.bourbon.base.threadpool.ThreadPool;
import com.github.bourbon.base.threadpool.ThreadPoolParams;
import com.github.bourbon.base.threadpool.support.AbortPolicyWithReport;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.LongUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 10:43
 */
public class EagerThreadPool implements ThreadPool {

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor(ThreadPoolParams params) {
        int cores = IntUtils.intValue(params.getCorePoolSize(), IntConstants.DEFAULT);
        int threads = IntUtils.intValue(params.getMaximumPoolSize(), IntConstants.MAX_VALUE);
        long alive = LongUtils.longValue(params.getKeepAliveTime(), 60_000L);
        int queues = IntUtils.intValue(params.getQueueSize(), IntConstants.DEFAULT);
        TimeUnit unit = ObjectUtils.defaultIfNull(params.getUnit(), TimeUnit.MILLISECONDS);
        String name = ObjectUtils.requireNonNull(params.getName());

        TaskQueue taskQueue = new TaskQueue(queues <= 0 ? 1 : queues);
        EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(cores, threads, alive, unit, taskQueue, new NamedInternalThreadFactory(name, true), new AbortPolicyWithReport(name));
        taskQueue.setExecutor(executor);
        return executor;
    }
}