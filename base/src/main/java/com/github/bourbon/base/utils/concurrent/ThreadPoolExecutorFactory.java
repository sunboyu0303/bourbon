package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.lang.Holder;

import java.util.Set;
import java.util.concurrent.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/15 18:29
 */
public final class ThreadPoolExecutorFactory {

    private static final String DEFAULT_NAMESPACE = "thread-pool-executor-managed";
    private static final ConcurrentMap<String, Holder<ConcurrentMap<String, Set<ThreadPoolExecutor>>>> resourcesManager = new ConcurrentHashMap<>(64);

    public static ThreadPoolExecutor newFixedThreadPool(String group, int corePoolSize, ThreadFactory threadFactory) {
        return register(group, new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory));
    }

    public static ThreadPoolExecutor newFixedThreadPool(String group, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        return register(group, new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler));
    }

    public static ThreadPoolExecutor register(String group, ThreadPoolExecutor executor) {
        return register(DEFAULT_NAMESPACE, group, executor);
    }

    public static ThreadPoolExecutor register(String tenantId, String group, ThreadPoolExecutor executor) {
        Holder<ConcurrentMap<String, Set<ThreadPoolExecutor>>> holder = resourcesManager.computeIfAbsent(tenantId, o -> Holder.of(new ConcurrentHashMap<>()));
        try {
            holder.lock();
            holder.get().computeIfAbsent(group, o -> new ConcurrentHashSet<>()).add(executor);
        } finally {
            holder.unlock();
        }
        return executor;
    }

    private ThreadPoolExecutorFactory() {
    }
}