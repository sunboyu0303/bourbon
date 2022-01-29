package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 10:48
 */
public class NamedInternalThreadFactory extends NamedThreadFactory {

    public NamedInternalThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedInternalThreadFactory(String prefix, boolean daemon) {
        super(prefix, daemon);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new InternalThread(group, InternalRunnable.wrap(r), getThreadName(), 0);
        t.setDaemon(isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}