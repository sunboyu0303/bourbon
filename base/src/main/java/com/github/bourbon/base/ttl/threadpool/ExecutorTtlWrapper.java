package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.ttl.TtlRunnable;
import com.github.bourbon.base.ttl.spi.TtlEnhanced;
import com.github.bourbon.base.ttl.spi.TtlWrapper;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 00:04
 */
class ExecutorTtlWrapper implements Executor, TtlWrapper<Executor>, TtlEnhanced {

    private final Executor e;
    final boolean idempotent;

    ExecutorTtlWrapper(Executor e, boolean idempotent) {
        this.e = e;
        this.idempotent = idempotent;
    }

    @Override
    public void execute(Runnable r) {
        e.execute(TtlRunnable.get(r, false, idempotent));
    }

    @Override
    public Executor unwrap() {
        return e;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass()) && e.equals(((ExecutorTtlWrapper) o).e);
    }

    @Override
    public int hashCode() {
        return e.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + e;
    }
}