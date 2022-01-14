package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 11:27
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String mPrefix;

    protected final boolean mDaemon;

    protected final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        threadGroup = ObjectUtils.defaultSupplierIfNull(System.getSecurityManager(), SecurityManager::getThreadGroup, () -> Thread.currentThread().getThreadGroup());
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, getThreadName(), 0);
        t.setDaemon(mDaemon);
        return t;
    }

    protected final String getThreadName() {
        return mPrefix + mThreadNum.getAndIncrement();
    }
}