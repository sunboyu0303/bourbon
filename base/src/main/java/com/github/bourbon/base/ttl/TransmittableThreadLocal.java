package com.github.bourbon.base.ttl;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 23:21
 */
public class TransmittableThreadLocal<T> extends InheritableThreadLocal<T> implements TtlCopier<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransmittableThreadLocal.class);

    private static final InheritableThreadLocal<WeakHashMap<TransmittableThreadLocal<Object>, ?>> holder =
            new InheritableThreadLocal<WeakHashMap<TransmittableThreadLocal<Object>, ?>>() {
                @Override
                protected WeakHashMap<TransmittableThreadLocal<Object>, ?> initialValue() {
                    return new WeakHashMap<>();
                }

                @Override
                protected WeakHashMap<TransmittableThreadLocal<Object>, ?> childValue(WeakHashMap<TransmittableThreadLocal<Object>, ?> m) {
                    return new WeakHashMap<>(m);
                }
            };

    private final boolean disableIgnoreNullValueSemantics;

    public TransmittableThreadLocal() {
        this(false);
    }

    public TransmittableThreadLocal(boolean disableIgnoreNullValueSemantics) {
        this.disableIgnoreNullValueSemantics = disableIgnoreNullValueSemantics;
    }

    @Override
    public T copy(T parentValue) {
        return parentValue;
    }

    protected void beforeExecute() {
    }

    protected void afterExecute() {
    }

    @Override
    public final T get() {
        T value = super.get();
        if (disableIgnoreNullValueSemantics || null != value) {
            addThisToHolder();
        }
        return value;
    }

    @Override
    public final void set(T value) {
        if (!disableIgnoreNullValueSemantics && null == value) {
            remove();
        } else {
            super.set(value);
            addThisToHolder();
        }
    }

    @SuppressWarnings("unchecked")
    private void addThisToHolder() {
        if (!holder.get().containsKey(this)) {
            ((WeakHashMap) holder.get()).put(this, null);
        }
    }

    @Override
    public final void remove() {
        removeThisFromHolder();
        super.remove();
    }

    private void removeThisFromHolder() {
        holder.get().remove(this);
    }

    private void superRemove() {
        super.remove();
    }

    private T copyValue() {
        return copy(get());
    }

    private static void doExecuteCallback(boolean isBefore) {
        for (TransmittableThreadLocal<Object> threadLocal : holder.get().keySet()) {
            try {
                if (isBefore) {
                    threadLocal.beforeExecute();
                } else {
                    threadLocal.afterExecute();
                }
            } catch (Exception e) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("TTL exception when " + (isBefore ? "beforeExecute" : "afterExecute") + ", cause: " + e.toString(), e);
                }
            }
        }
    }

    public static class Transmitter {
        private static volatile WeakHashMap<ThreadLocal<Object>, TtlCopier<Object>> threadLocalHolder = new WeakHashMap<>();
        private static final Object threadLocalHolderUpdateLock = new Object();
        private static final Object threadLocalClearMark = new Object();

        public static Object capture() {
            return new Snapshot(captureTtlValues(), captureThreadLocalValues());
        }

        private static HashMap<TransmittableThreadLocal<Object>, Object> captureTtlValues() {
            HashMap<TransmittableThreadLocal<Object>, Object> ttl2Value = new HashMap<>();
            for (TransmittableThreadLocal<Object> threadLocal : TransmittableThreadLocal.holder.get().keySet()) {
                ttl2Value.put(threadLocal, threadLocal.copyValue());
            }
            return ttl2Value;
        }

        private static HashMap<ThreadLocal<Object>, Object> captureThreadLocalValues() {
            HashMap<ThreadLocal<Object>, Object> threadLocal2Value = new HashMap<>();
            for (Map.Entry<ThreadLocal<Object>, TtlCopier<Object>> entry : threadLocalHolder.entrySet()) {
                ThreadLocal<Object> threadLocal = entry.getKey();
                threadLocal2Value.put(threadLocal, entry.getValue().copy(threadLocal.get()));
            }
            return threadLocal2Value;
        }

        public static Object replay(Object captured) {
            Snapshot capturedSnapshot = (Snapshot) captured;
            return new Snapshot(replayTtlValues(capturedSnapshot.ttl2Value), replayThreadLocalValues(capturedSnapshot.threadLocal2Value));
        }

        private static HashMap<TransmittableThreadLocal<Object>, Object> replayTtlValues(HashMap<TransmittableThreadLocal<Object>, Object> captured) {
            HashMap<TransmittableThreadLocal<Object>, Object> backup = new HashMap<>();
            final Iterator<TransmittableThreadLocal<Object>> iterator = TransmittableThreadLocal.holder.get().keySet().iterator();
            while (iterator.hasNext()) {
                TransmittableThreadLocal<Object> threadLocal = iterator.next();
                backup.put(threadLocal, threadLocal.get());
                if (!captured.containsKey(threadLocal)) {
                    iterator.remove();
                    threadLocal.superRemove();
                }
            }
            setTtlValuesTo(captured);
            doExecuteCallback(true);
            return backup;
        }

        private static void setTtlValuesTo(HashMap<TransmittableThreadLocal<Object>, Object> ttlValues) {
            ttlValues.forEach(TransmittableThreadLocal::set);
        }

        private static HashMap<ThreadLocal<Object>, Object> replayThreadLocalValues(HashMap<ThreadLocal<Object>, Object> captured) {
            HashMap<ThreadLocal<Object>, Object> backup = new HashMap<>();
            for (Map.Entry<ThreadLocal<Object>, Object> entry : captured.entrySet()) {
                ThreadLocal<Object> threadLocal = entry.getKey();
                backup.put(threadLocal, threadLocal.get());
                Object value = entry.getValue();
                if (value == threadLocalClearMark) {
                    threadLocal.remove();
                } else {
                    threadLocal.set(value);
                }
            }
            return backup;
        }

        public static void restore(Object backup) {
            Snapshot backupSnapshot = (Snapshot) backup;
            restoreTtlValues(backupSnapshot.ttl2Value);
            restoreThreadLocalValues(backupSnapshot.threadLocal2Value);
        }

        private static void restoreTtlValues(HashMap<TransmittableThreadLocal<Object>, Object> backup) {
            doExecuteCallback(false);
            Iterator<TransmittableThreadLocal<Object>> iterator = TransmittableThreadLocal.holder.get().keySet().iterator();
            while (iterator.hasNext()) {
                TransmittableThreadLocal<Object> threadLocal = iterator.next();
                if (!backup.containsKey(threadLocal)) {
                    iterator.remove();
                    threadLocal.superRemove();
                }
            }
            setTtlValuesTo(backup);
        }

        private static void restoreThreadLocalValues(HashMap<ThreadLocal<Object>, Object> backup) {
            backup.forEach(ThreadLocal::set);
        }

        public static Object clear() {
            HashMap<ThreadLocal<Object>, Object> threadLocal2Value = new HashMap<>();
            for (Map.Entry<ThreadLocal<Object>, TtlCopier<Object>> threadLocalTtlCopierEntry : threadLocalHolder.entrySet()) {
                threadLocal2Value.put(threadLocalTtlCopierEntry.getKey(), threadLocalClearMark);
            }
            return replay(new Snapshot(new HashMap<>(), threadLocal2Value));
        }

        public static <R> R runSupplierWithCaptured(Object captured, Supplier<R> bizLogic) {
            Object backup = replay(captured);
            try {
                return bizLogic.get();
            } finally {
                restore(backup);
            }
        }

        public static <R> R runSupplierWithClear(Supplier<R> bizLogic) {
            Object backup = clear();
            try {
                return bizLogic.get();
            } finally {
                restore(backup);
            }
        }

        public static <R> R runCallableWithCaptured(Object captured, Callable<R> bizLogic) throws Exception {
            Object backup = replay(captured);
            try {
                return bizLogic.call();
            } finally {
                restore(backup);
            }
        }

        public static <R> R runCallableWithClear(Callable<R> bizLogic) throws Exception {
            Object backup = clear();
            try {
                return bizLogic.call();
            } finally {
                restore(backup);
            }
        }

        public static <T> boolean registerThreadLocal(ThreadLocal<T> threadLocal, TtlCopier<T> copier) {
            return registerThreadLocal(threadLocal, copier, false);
        }

        public static <T> boolean registerThreadLocal(ThreadLocal<T> threadLocal) {
            return registerThreadLocal(threadLocal, t -> t, false);
        }

        public static <T> boolean registerThreadLocal(ThreadLocal<T> threadLocal, boolean force) {
            return registerThreadLocal(threadLocal, t -> t, force);
        }

        @SuppressWarnings("unchecked")
        public static <T> boolean registerThreadLocal(ThreadLocal<T> threadLocal, TtlCopier<T> copier, boolean force) {
            if (threadLocal instanceof TransmittableThreadLocal) {
                LOGGER.warn("register a TransmittableThreadLocal instance, this is unnecessary!");
                return true;
            }
            synchronized (threadLocalHolderUpdateLock) {
                if (!force && threadLocalHolder.containsKey(threadLocal)) {
                    return false;
                }
                WeakHashMap<ThreadLocal<Object>, TtlCopier<Object>> newHolder = new WeakHashMap<>(threadLocalHolder);
                newHolder.put((ThreadLocal) threadLocal, (TtlCopier) copier);
                threadLocalHolder = newHolder;
                return true;
            }
        }

        public static <T> boolean unregisterThreadLocal(ThreadLocal<T> threadLocal) {
            if (threadLocal instanceof TransmittableThreadLocal) {
                LOGGER.warn("unregister a TransmittableThreadLocal instance, this is unnecessary!");
                return true;
            }
            synchronized (threadLocalHolderUpdateLock) {
                if (!threadLocalHolder.containsKey(threadLocal)) {
                    return false;
                }
                WeakHashMap<ThreadLocal<Object>, TtlCopier<Object>> newHolder = new WeakHashMap<>(threadLocalHolder);
                newHolder.remove(threadLocal);
                threadLocalHolder = newHolder;
                return true;
            }
        }

        private Transmitter() {
        }

        private static class Snapshot {
            private final HashMap<TransmittableThreadLocal<Object>, Object> ttl2Value;
            private final HashMap<ThreadLocal<Object>, Object> threadLocal2Value;

            private Snapshot(HashMap<TransmittableThreadLocal<Object>, Object> ttl2Value, HashMap<ThreadLocal<Object>, Object> threadLocal2Value) {
                this.ttl2Value = ttl2Value;
                this.threadLocal2Value = threadLocal2Value;
            }
        }
    }
}