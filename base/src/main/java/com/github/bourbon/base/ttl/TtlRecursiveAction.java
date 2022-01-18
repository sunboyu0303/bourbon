package com.github.bourbon.base.ttl;

import com.github.bourbon.base.ttl.spi.TtlEnhanced;

import java.util.concurrent.ForkJoinTask;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 11:26
 */
public abstract class TtlRecursiveAction extends ForkJoinTask<Void> implements TtlEnhanced {
    private static final long serialVersionUID = -5753568484583412377L;
    private final transient Object captured = TransmittableThreadLocal.Transmitter.capture();

    protected TtlRecursiveAction() {
    }

    protected abstract void compute();

    @Override
    public final Void getRawResult() {
        return null;
    }

    @Override
    protected final void setRawResult(Void mustBeNull) {
    }

    @Override
    protected final boolean exec() {
        Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
        try {
            compute();
            return true;
        } finally {
            TransmittableThreadLocal.Transmitter.restore(backup);
        }
    }
}