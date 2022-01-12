package com.github.bourbon.base.extension.support;

import com.github.bourbon.base.extension.Lifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 16:26
 */
public abstract class AbstractLifecycle implements Lifecycle {

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    @Override
    public final void initialize() throws Exception {
        if (initialized.compareAndSet(false, true)) {
            doInitialize();
        }
    }

    @Override
    public final void destroy() throws Exception {
        if (initialized.get() && closed.compareAndSet(false, true)) {
            doDestroy();
        }
    }

    protected void doInitialize() throws Exception {
    }

    protected void doDestroy() throws Exception {
    }
}