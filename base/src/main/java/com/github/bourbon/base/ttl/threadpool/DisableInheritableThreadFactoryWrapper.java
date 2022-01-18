package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.ttl.TransmittableThreadLocal;

import java.util.concurrent.ThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 11:43
 */
class DisableInheritableThreadFactoryWrapper implements DisableInheritableThreadFactory {

    private final ThreadFactory tf;

    DisableInheritableThreadFactoryWrapper(ThreadFactory tf) {
        this.tf = tf;
    }

    @Override
    public Thread newThread(Runnable r) {
        Object backup = TransmittableThreadLocal.Transmitter.clear();
        try {
            return tf.newThread(r);
        } finally {
            TransmittableThreadLocal.Transmitter.restore(backup);
        }
    }

    @Override
    public ThreadFactory unwrap() {
        return tf;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass()) && tf.equals(((DisableInheritableThreadFactoryWrapper) o).tf);
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