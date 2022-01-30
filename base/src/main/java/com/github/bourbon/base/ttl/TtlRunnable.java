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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 00:08
 */
public final class TtlRunnable implements Runnable, TtlWrapper<Runnable>, TtlEnhanced, TtlAttachments {

    private final AtomicReference<Object> capturedRef = new AtomicReference<>(TransmittableThreadLocal.Transmitter.capture());
    private final TtlAttachmentsDelegate ttlAttachment = new TtlAttachmentsDelegate();
    private final Runnable runnable;
    private final boolean releaseTtlValueReferenceAfterRun;

    private TtlRunnable(Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
        this.runnable = runnable;
        this.releaseTtlValueReferenceAfterRun = releaseTtlValueReferenceAfterRun;
    }

    @Override
    public void run() {
        Object captured = capturedRef.get();
        if (captured != null && (!releaseTtlValueReferenceAfterRun || capturedRef.compareAndSet(captured, null))) {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                runnable.run();
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        } else {
            throw new IllegalStateException("TTL value reference is released after run!");
        }
    }

    public Runnable getRunnable() {
        return unwrap();
    }

    @Override
    public Runnable unwrap() {
        return runnable;
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
        return this == o || (o != null && getClass() == o.getClass()) && runnable.equals(((TtlRunnable) o).runnable);
    }

    @Override
    public int hashCode() {
        return runnable.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + runnable;
    }

    public static TtlRunnable get(Runnable runnable) {
        return get(runnable, false, false);
    }

    public static TtlRunnable get(Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
        return get(runnable, releaseTtlValueReferenceAfterRun, false);
    }

    public static TtlRunnable get(Runnable runnable, boolean releaseTtlValueReferenceAfterRun, boolean idempotent) {
        return ObjectUtils.defaultIfNull(runnable, t -> BooleanUtils.defaultSupplierIfAssignableFrom(t, TtlEnhanced.class, r -> {
            if (idempotent) {
                return (TtlRunnable) r;
            }
            throw new IllegalStateException("Already TtlRunnable!");
        }, () -> new TtlRunnable(t, releaseTtlValueReferenceAfterRun)));
    }

    public static List<TtlRunnable> gets(Collection<? extends Runnable> tasks) {
        return gets(tasks, false, false);
    }

    public static List<TtlRunnable> gets(Collection<? extends Runnable> tasks, boolean releaseTtlValueReferenceAfterRun) {
        return gets(tasks, releaseTtlValueReferenceAfterRun, false);
    }

    public static List<TtlRunnable> gets(Collection<? extends Runnable> tasks, boolean releaseTtlValueReferenceAfterRun, boolean idempotent) {
        return BooleanUtils.defaultSupplierIfPredicate(tasks, CollectionUtils::isNotEmpty, c -> c.stream().map(task -> get(task, releaseTtlValueReferenceAfterRun, idempotent)).collect(Collectors.toList()), Collections::emptyList);
    }

    public static Runnable unwrap(Runnable runnable) {
        return BooleanUtils.defaultIfAssignableFrom(runnable, TtlRunnable.class, r -> ((TtlRunnable) r).getRunnable(), runnable);
    }

    public static List<Runnable> unwraps(Collection<? extends Runnable> tasks) {
        return BooleanUtils.defaultSupplierIfPredicate(tasks, CollectionUtils::isNotEmpty, c -> c.stream().map(TtlRunnable::unwrap).collect(Collectors.toList()), Collections::emptyList);
    }
}