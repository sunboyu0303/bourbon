package com.github.bourbon.base.threadpool.support.fixed;

import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.threadlocal.NamedInternalThreadFactory;
import com.github.bourbon.base.threadpool.ThreadPool;
import com.github.bourbon.base.threadpool.ThreadPoolParams;
import com.github.bourbon.base.threadpool.support.AbortPolicyWithReport;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;

import java.util.concurrent.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 20:05
 */
public class FixedThreadPool implements ThreadPool {

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor(ThreadPoolParams params) {
        int threads = IntUtils.intValue(params.getMaximumPoolSize(), IntConstants.MAX_VALUE);
        int queues = IntUtils.intValue(params.getQueueSize(), IntConstants.DEFAULT);
        String name = ObjectUtils.requireNonNull(params.getName());

        BlockingQueue<Runnable> queue;
        if (queues == 0) {
            queue = new SynchronousQueue<>();
        } else if (queues < 0) {
            queue = new LinkedBlockingQueue<>();
        } else {
            queue = new LinkedBlockingQueue<>(queues);
        }

        return ThreadPoolExecutorFactory.newFixedThreadPool("base", threads, threads, 0, TimeUnit.MILLISECONDS, queue, new NamedInternalThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}