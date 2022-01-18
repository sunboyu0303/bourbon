package com.github.bourbon.base.ttl;

import com.github.bourbon.base.ttl.spi.TtlEnhanced;

import java.util.concurrent.ForkJoinTask;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 11:22
 */
public abstract class TtlRecursiveTask<V> extends ForkJoinTask<V> implements TtlEnhanced {
    private static final long serialVersionUID = 1814679366926362436L;
    private final transient Object captured = TransmittableThreadLocal.Transmitter.capture();
    private transient V result;

    protected TtlRecursiveTask() {
    }

    protected abstract V compute();

    @Override
    public final V getRawResult() {
        return result;
    }

    @Override
    protected final void setRawResult(V value) {
        result = value;
    }

    @Override
    protected final boolean exec() {
        Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
        try {
            result = compute();
            return true;
        } finally {
            TransmittableThreadLocal.Transmitter.restore(backup);
        }
    }
}