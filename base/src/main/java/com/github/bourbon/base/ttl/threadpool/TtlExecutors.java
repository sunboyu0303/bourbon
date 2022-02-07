package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.ttl.spi.TtlEnhanced;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 00:22
 */
public final class TtlExecutors {

    public static Executor getTtlExecutor(Executor executor) {
        return ObjectUtils.defaultIfNullElseFunction(executor, e -> BooleanUtils.defaultSupplierIfAssignableFrom(e, TtlEnhanced.class, t -> t, () -> new ExecutorTtlWrapper(e, true)));
    }

    public static ExecutorService getTtlExecutorService(ExecutorService service) {
        return ObjectUtils.defaultIfNullElseFunction(service, e -> BooleanUtils.defaultSupplierIfAssignableFrom(e, TtlEnhanced.class, t -> t, () -> new ExecutorServiceTtlWrapper(e, true)));
    }

    public static ScheduledExecutorService getTtlScheduledExecutorService(ScheduledExecutorService service) {
        return ObjectUtils.defaultIfNullElseFunction(service, e -> BooleanUtils.defaultSupplierIfAssignableFrom(e, TtlEnhanced.class, t -> t, () -> new ScheduledExecutorServiceTtlWrapper(e, true)));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Executor> T unwrap(T e) {
        return BooleanUtils.defaultIfFalse(isTtlWrapper(e), () -> (T) ((ExecutorTtlWrapper) e).unwrap(), e);
    }

    public static <T extends Executor> boolean isTtlWrapper(T e) {
        return e instanceof TtlEnhanced;
    }

    public static ThreadFactory getDefaultDisableInheritableThreadFactory() {
        return getDisableInheritableThreadFactory(Executors.defaultThreadFactory());
    }

    public static ThreadFactory getDisableInheritableThreadFactory(ThreadFactory threadFactory) {
        return ObjectUtils.defaultIfNullElseFunction(threadFactory, tf -> BooleanUtils.defaultIfFalse(!isDisableInheritableThreadFactory(tf), () -> new DisableInheritableThreadFactoryWrapper(tf), tf));
    }

    public static boolean isDisableInheritableThreadFactory(ThreadFactory tf) {
        return tf instanceof DisableInheritableThreadFactory;
    }

    public static ThreadFactory unwrap(ThreadFactory tf) {
        return BooleanUtils.defaultIfFalse(isDisableInheritableThreadFactory(tf), ((DisableInheritableThreadFactory) tf)::unwrap, tf);
    }

    private TtlExecutors() {
    }
}