package com.github.bourbon.base.ttl.threadpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 15:43
 */
public final class TtlForkJoinPoolHelper {

    public static ForkJoinWorkerThreadFactory getDisableInheritableForkJoinWorkerThreadFactory(ForkJoinWorkerThreadFactory tf) {
        return tf != null && !isDisableInheritableForkJoinWorkerThreadFactory(tf) ? new DisableInheritableForkJoinWorkerThreadFactoryWrapper(tf) : tf;
    }

    public static ForkJoinWorkerThreadFactory getDefaultDisableInheritableForkJoinWorkerThreadFactory() {
        return getDisableInheritableForkJoinWorkerThreadFactory(ForkJoinPool.defaultForkJoinWorkerThreadFactory);
    }

    public static boolean isDisableInheritableForkJoinWorkerThreadFactory(ForkJoinWorkerThreadFactory tf) {
        return tf instanceof DisableInheritableForkJoinWorkerThreadFactory;
    }

    public static ForkJoinWorkerThreadFactory unwrap(ForkJoinWorkerThreadFactory tf) {
        return !isDisableInheritableForkJoinWorkerThreadFactory(tf) ? tf : ((DisableInheritableForkJoinWorkerThreadFactory) tf).unwrap();
    }

    private TtlForkJoinPoolHelper() {
    }
}