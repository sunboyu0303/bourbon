package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 11:27
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    protected final ThreadGroup group;

    private final String namePrefix;

    protected final boolean isDaemon;

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this(
                ObjectUtils.defaultSupplierIfNull(System.getSecurityManager(), SecurityManager::getThreadGroup, () -> Thread.currentThread().getThreadGroup()),
                prefix + StringConstants.HYPHEN + POOL_NUMBER.getAndIncrement() + "-thread-",
                daemon
        );
    }

    protected NamedThreadFactory(ThreadGroup group, String namePrefix, boolean daemon) {
        this.group = group;
        this.namePrefix = namePrefix;
        this.isDaemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, getThreadName(), 0);
        t.setDaemon(isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

    protected final String getThreadName() {
        return namePrefix + threadNumber.getAndIncrement();
    }
}