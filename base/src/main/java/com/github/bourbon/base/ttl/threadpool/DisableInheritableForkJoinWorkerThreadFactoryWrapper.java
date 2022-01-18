package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.ttl.TransmittableThreadLocal;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 15:36
 */
class DisableInheritableForkJoinWorkerThreadFactoryWrapper implements DisableInheritableForkJoinWorkerThreadFactory {

    private final ForkJoinWorkerThreadFactory tf;

    DisableInheritableForkJoinWorkerThreadFactoryWrapper(ForkJoinWorkerThreadFactory tf) {
        this.tf = tf;
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        Object backup = TransmittableThreadLocal.Transmitter.clear();
        try {
            return tf.newThread(pool);
        } finally {
            TransmittableThreadLocal.Transmitter.restore(backup);
        }
    }

    @Override
    public ForkJoinWorkerThreadFactory unwrap() {
        return tf;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass()) && tf.equals(((DisableInheritableForkJoinWorkerThreadFactoryWrapper) o).tf);
    }

    @Override
    public int hashCode() {
        return tf.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + tf;
    }
}