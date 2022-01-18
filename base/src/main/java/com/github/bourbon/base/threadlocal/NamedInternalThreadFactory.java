package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 10:48
 */
public class NamedInternalThreadFactory extends NamedThreadFactory {

    public NamedInternalThreadFactory() {
        super();
    }

    public NamedInternalThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedInternalThreadFactory(String prefix, boolean daemon) {
        super(prefix, daemon);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new InternalThread(threadGroup, InternalRunnable.wrap(r), getThreadName(), 0);
        t.setDaemon(mDaemon);
        return t;
    }
}