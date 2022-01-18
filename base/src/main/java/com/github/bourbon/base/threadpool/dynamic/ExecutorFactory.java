package com.github.bourbon.base.threadpool.dynamic;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/8 20:37
 */
public final class ExecutorFactory {

    public static final class Managed {
        private static final String DEFAULT_NAMESPACE = "dynamic-thread-pool";
        private static final ThreadPoolManager THREAD_POOL_MANAGER = ThreadPoolManager.getInstance();

        public static ScheduledExecutorService newSingleScheduledExecutorService(String group, ThreadFactory threadFactory) {
            return newScheduledExecutorService(group, 1, threadFactory);
        }

        public static ScheduledExecutorService newSingleScheduledExecutorService(String group, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            return newScheduledExecutorService(group, 1, threadFactory, handler);
        }

        public static ScheduledExecutorService newScheduledExecutorService(String group, int corePoolSize, ThreadFactory threadFactory) {
            return register(group, new ScheduledThreadPoolExecutor(corePoolSize, threadFactory));
        }

        public static ScheduledExecutorService newScheduledExecutorService(String group, int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            return register(group, new ScheduledThreadPoolExecutor(corePoolSize, threadFactory, handler));
        }

        private static ScheduledExecutorService register(String group, ScheduledExecutorService executorService) {
            THREAD_POOL_MANAGER.register(DEFAULT_NAMESPACE, group, executorService);
            return executorService;
        }
    }

    private ExecutorFactory() {
    }
}