package com.github.bourbon.base.threadpool.dynamic;

import com.github.bourbon.base.lang.Holder;
import com.github.bourbon.base.utils.concurrent.ConcurrentHashSet;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/8 19:09
 */
public final class ThreadPoolManager {

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    private final ConcurrentMap<String, Holder<ConcurrentMap<String, Set<ExecutorService>>>> resourcesManager = new ConcurrentHashMap<>(64);

    public void register(String tenantId, String group, ExecutorService executor) {
        Holder<ConcurrentMap<String, Set<ExecutorService>>> holder = resourcesManager.computeIfAbsent(tenantId, o -> Holder.of(new ConcurrentHashMap<>()));
        try {
            holder.lock();
            holder.get().computeIfAbsent(group, o -> new ConcurrentHashSet<>()).add(executor);
        } finally {
            holder.unlock();
        }
    }

    private ThreadPoolManager() {
    }
}