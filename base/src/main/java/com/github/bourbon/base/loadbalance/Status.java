package com.github.bourbon.base.loadbalance;

import com.github.bourbon.base.utils.BooleanUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 15:40
 */
public final class Status {

    private static final ConcurrentMap<String, Status> SERVICE_STATISTICS = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Object> values = new ConcurrentHashMap<>();

    private final AtomicInteger active = new AtomicInteger();
    private final AtomicLong total = new AtomicLong();
    private final AtomicLong totalElapsed = new AtomicLong();
    private final AtomicLong maxElapsed = new AtomicLong();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicLong failedElapsed = new AtomicLong();
    private final AtomicLong failedMaxElapsed = new AtomicLong();
    private final AtomicLong succeededMaxElapsed = new AtomicLong();

    public static Status getStatus(Invoker<?> invoker) {
        return SERVICE_STATISTICS.computeIfAbsent(invoker.getIdentity(), key -> new Status());
    }

    public static void removeStatus(Invoker<?> invoker) {
        SERVICE_STATISTICS.remove(invoker.getIdentity());
    }

    public static void beginCount(Invoker<?> invoker) {
        beginCount(invoker, Integer.MAX_VALUE);
    }

    public static boolean beginCount(Invoker<?> invoker, int max) {
        max = (max <= 0) ? Integer.MAX_VALUE : max;
        Status status = getStatus(invoker);
        if (status.active.get() == Integer.MAX_VALUE) {
            return false;
        }
        for (int i; ; ) {
            i = status.active.get();

            if (i == Integer.MAX_VALUE || i + 1 > max) {
                return false;
            }

            if (status.active.compareAndSet(i, i + 1)) {
                break;
            }
        }
        return true;
    }

    public static void endCount(Invoker<?> invoker, long elapsed, boolean succeeded) {
        endCount(getStatus(invoker), elapsed, succeeded);
    }

    private static void endCount(Status status, long elapsed, boolean succeeded) {
        status.active.decrementAndGet();
        status.total.incrementAndGet();
        status.totalElapsed.addAndGet(elapsed);

        if (status.maxElapsed.get() < elapsed) {
            status.maxElapsed.set(elapsed);
        }

        if (succeeded) {
            if (status.succeededMaxElapsed.get() < elapsed) {
                status.succeededMaxElapsed.set(elapsed);
            }
        } else {
            status.failed.incrementAndGet();
            status.failedElapsed.addAndGet(elapsed);
            if (status.failedMaxElapsed.get() < elapsed) {
                status.failedMaxElapsed.set(elapsed);
            }
        }
    }

    public void set(String key, Object value) {
        values.put(key, value);
    }

    public Object get(String key) {
        return values.get(key);
    }

    public int getActive() {
        return active.get();
    }

    public long getTotal() {
        return total.get();
    }

    public long getTotalElapsed() {
        return totalElapsed.get();
    }

    public long getAverageElapsed() {
        return BooleanUtils.defaultIfPredicate(getTotal(), t -> t != 0, t -> getTotalElapsed() / t, 0L);
    }

    public long getMaxElapsed() {
        return maxElapsed.get();
    }

    public int getFailed() {
        return failed.get();
    }

    public long getFailedElapsed() {
        return failedElapsed.get();
    }

    public long getFailedAverageElapsed() {
        return BooleanUtils.defaultIfPredicate(getFailed(), f -> f != 0, f -> getFailedElapsed() / f, 0L);
    }

    public long getFailedMaxElapsed() {
        return failedMaxElapsed.get();
    }

    public long getSucceeded() {
        return getTotal() - getFailed();
    }

    public long getSucceededElapsed() {
        return getTotalElapsed() - getFailedElapsed();
    }

    public long getSucceededAverageElapsed() {
        return BooleanUtils.defaultIfPredicate(getSucceeded(), s -> s != 0, s -> getSucceededElapsed() / s, 0L);
    }

    public long getSucceededMaxElapsed() {
        return succeededMaxElapsed.get();
    }

    public long getAverageTps() {
        return BooleanUtils.defaultSupplierIfPredicate(getTotalElapsed(), t -> t >= 1000L, t -> getTotal() / (t / 1000L), this::getTotal);
    }

    private Status() {
    }
}