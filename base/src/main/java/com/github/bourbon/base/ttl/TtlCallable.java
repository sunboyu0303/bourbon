package com.github.bourbon.base.ttl;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.ttl.spi.TtlAttachments;
import com.github.bourbon.base.ttl.spi.TtlAttachmentsDelegate;
import com.github.bourbon.base.ttl.spi.TtlEnhanced;
import com.github.bourbon.base.ttl.spi.TtlWrapper;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 00:55
 */
public final class TtlCallable<V> implements Callable<V>, TtlWrapper<Callable<V>>, TtlEnhanced, TtlAttachments {
    private final AtomicReference<Object> capturedRef = new AtomicReference<>(TransmittableThreadLocal.Transmitter.capture());
    private final TtlAttachmentsDelegate ttlAttachment = new TtlAttachmentsDelegate();
    private final Callable<V> callable;
    private final boolean releaseTtlValueReferenceAfterCall;

    private TtlCallable(Callable<V> callable, boolean releaseTtlValueReferenceAfterCall) {
        this.callable = callable;
        this.releaseTtlValueReferenceAfterCall = releaseTtlValueReferenceAfterCall;
    }

    @Override
    public V call() throws Exception {
        Object captured = capturedRef.get();
        if (captured != null && (!releaseTtlValueReferenceAfterCall || capturedRef.compareAndSet(captured, null))) {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                return callable.call();
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        }
        throw new IllegalStateException("TTL value reference is released after call!");
    }

    public Callable<V> getCallable() {
        return unwrap();
    }

    @Override
    public Callable<V> unwrap() {
        return callable;
    }

    @Override
    public void setTtlAttachment(String key, Object value) {
        ttlAttachment.setTtlAttachment(key, value);
    }

    @Override
    public <T> T getTtlAttachment(String key) {
        return ttlAttachment.getTtlAttachment(key);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass()) && callable.equals(((TtlCallable) o).callable);
    }

    @Override
    public int hashCode() {
        return callable.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + callable;
    }

    public static <T> TtlCallable<T> get(Callable<T> callable) {
        return get(callable, false, false);
    }

    public static <T> TtlCallable<T> get(Callable<T> callable, boolean releaseTtlValueReferenceAfterCall) {
        return get(callable, releaseTtlValueReferenceAfterCall, false);
    }

    @SuppressWarnings("unchecked")
    public static <T> TtlCallable<T> get(Callable<T> t, boolean releaseTtlValueReferenceAfterCall, boolean idempotent) {
        return ObjectUtils.defaultIfNull(t, callable -> BooleanUtils.defaultSupplierIfAssignableFrom(callable, TtlEnhanced.class, c -> {
            if (idempotent) {
                return (TtlCallable) c;
            }
            throw new IllegalStateException("Already TtlCallable!");
        }, () -> new TtlCallable<>(callable, releaseTtlValueReferenceAfterCall)));
    }

    public static <T> List<TtlCallable<T>> gets(Collection<? extends Callable<T>> tasks) {
        return gets(tasks, false, false);
    }

    public static <T> List<TtlCallable<T>> gets(Collection<? extends Callable<T>> tasks, boolean releaseTtlValueReferenceAfterCall) {
        return gets(tasks, releaseTtlValueReferenceAfterCall, false);
    }

    public static <T> List<TtlCallable<T>> gets(Collection<? extends Callable<T>> tasks, boolean releaseTtlValueReferenceAfterCall, boolean idempotent) {
        return BooleanUtils.defaultSupplierIfPredicate(tasks, c -> !CollectionUtils.isEmpty(c), c -> c.stream().map(task -> get(task, releaseTtlValueReferenceAfterCall, idempotent)).collect(Collectors.toList()), Collections::emptyList);
    }

    @SuppressWarnings("unchecked")
    public static <T> Callable<T> unwrap(Callable<T> callable) {
        return BooleanUtils.defaultIfAssignableFrom(callable, TtlCallable.class, c -> ((TtlCallable) c).getCallable(), callable);
    }

    public static <T> List<Callable<T>> unwraps(Collection<? extends Callable<T>> tasks) {
        return BooleanUtils.defaultSupplierIfPredicate(tasks, c -> !CollectionUtils.isEmpty(c), c -> c.stream().map(TtlCallable::unwrap).collect(Collectors.toList()), Collections::emptyList);
    }
}