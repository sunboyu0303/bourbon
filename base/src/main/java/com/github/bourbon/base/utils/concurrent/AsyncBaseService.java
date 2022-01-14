package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.utils.FutureTimeoutUtils;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 13:59
 */
public interface AsyncBaseService {

    static <T> CompletableFuture<T> scheduleSupplier(ScheduledExecutorService e, Supplier<T> s, long delay, TimeUnit unit) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        e.schedule(() -> {
            try {
                return completableFuture.complete(s.get());
            } catch (Exception t) {
                return completableFuture.completeExceptionally(t);
            }
        }, delay, unit);
        return completableFuture;
    }

    static <T> CompletableFuture<T> scheduleSupplierNoException(ScheduledExecutorService e, Supplier<T> s, long delay, TimeUnit unit) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        e.schedule(() -> {
            try {
                return completableFuture.complete(s.get());
            } catch (Exception t) {
                return completableFuture.complete(null);
            }
        }, delay, unit);
        return completableFuture;
    }

    static <T> CompletableFuture<T> scheduleCallable(ScheduledExecutorService e, Callable<T> c, long delay, TimeUnit unit) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        e.schedule(() -> {
            try {
                return completableFuture.complete(c.call());
            } catch (Exception t) {
                return completableFuture.completeExceptionally(t);
            }
        }, delay, unit);
        return completableFuture;
    }

    static <T> CompletableFuture<T> scheduleCallableNoException(ScheduledExecutorService e, Callable<T> c, long delay, TimeUnit unit) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        e.schedule(() -> {
            try {
                return completableFuture.complete(c.call());
            } catch (Exception t) {
                return completableFuture.complete(null);
            }
        }, delay, unit);
        return completableFuture;
    }

    static CompletableFuture<Boolean> scheduleRunnable(ScheduledExecutorService e, Runnable r, long delay, TimeUnit unit) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        e.schedule(() -> {
            try {
                r.run();
                return completableFuture.complete(true);
            } catch (Exception t) {
                return completableFuture.completeExceptionally(t);
            }
        }, delay, unit);
        return completableFuture;
    }

    static CompletableFuture<Boolean> scheduleRunnableNoException(ScheduledExecutorService e, Runnable r, long delay, TimeUnit unit) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        e.schedule(() -> {
            try {
                r.run();
                return completableFuture.complete(true);
            } catch (Exception t) {
                return completableFuture.complete(false);
            }
        }, delay, unit);
        return completableFuture;
    }

    static CompletableFuture<Boolean> runAsync(Runnable r) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                r.run();
                completableFuture.complete(true);
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }

    static CompletableFuture<Boolean> runAsyncNoException(Runnable r) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                r.run();
                completableFuture.complete(true);
            } catch (Exception e) {
                completableFuture.complete(false);
            }
        });
        return completableFuture;
    }

    static CompletableFuture<Boolean> runAsync(Runnable r, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(runAsync(r), timeout, timeUnit);
    }

    static CompletableFuture<Boolean> runAsyncNoException(Runnable r, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(runAsyncNoException(r), timeout, timeUnit);
    }

    static CompletableFuture<Boolean> runAsync(Runnable r, Executor executor) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                r.run();
                completableFuture.complete(true);
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, executor);
        return completableFuture;
    }

    static CompletableFuture<Boolean> runAsyncNoException(Runnable r, Executor executor) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                r.run();
                completableFuture.complete(true);
            } catch (Exception e) {
                completableFuture.complete(false);
            }
        }, executor);
        return completableFuture;
    }

    static CompletableFuture<Boolean> runAsync(Runnable r, Executor e, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(runAsync(r, e), timeout, timeUnit);
    }

    static CompletableFuture<Boolean> runAsyncNoException(Runnable r, Executor e, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(runAsyncNoException(r, e), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> supplierAsync(Supplier<T> s) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(s.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }

    static <T> CompletableFuture<T> supplierAsyncNoException(Supplier<T> s) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(s.get());
            } catch (Exception e) {
                completableFuture.complete(null);
            }
        });
        return completableFuture;
    }

    static <T> CompletableFuture<T> supplierAsync(Supplier<T> s, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(supplierAsync(s), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> supplierAsyncNoException(Supplier<T> s, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(supplierAsyncNoException(s), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> supplierAsync(Supplier<T> s, Executor executor) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(s.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, executor);
        return completableFuture;
    }

    static <T> CompletableFuture<T> supplierAsyncNoException(Supplier<T> s, Executor executor) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(s.get());
            } catch (Exception e) {
                completableFuture.complete(null);
            }
        }, executor);
        return completableFuture;
    }

    static <T> CompletableFuture<T> supplierAsync(Supplier<T> s, Executor e, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(supplierAsync(s, e), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> supplierAsyncNoException(Supplier<T> s, Executor e, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(supplierAsyncNoException(s, e), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> callAsync(Callable<T> c) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(c.call());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }

    static <T> CompletableFuture<T> callAsyncNoException(Callable<T> c) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(c.call());
            } catch (Exception e) {
                completableFuture.complete(null);
            }
        });
        return completableFuture;
    }

    static <T> CompletableFuture<T> callAsync(Callable<T> c, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(callAsync(c), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> callAsyncNoException(Callable<T> c, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(callAsyncNoException(c), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> callAsync(Callable<T> c, Executor executor) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(c.call());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, executor);
        return completableFuture;
    }

    static <T> CompletableFuture<T> callAsyncNoException(Callable<T> c, Executor executor) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                completableFuture.complete(c.call());
            } catch (Exception e) {
                completableFuture.complete(null);
            }
        }, executor);
        return completableFuture;
    }

    static <T> CompletableFuture<T> callAsync(Callable<T> c, Executor e, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(callAsync(c, e), timeout, timeUnit);
    }

    static <T> CompletableFuture<T> callAsyncNoException(Callable<T> c, Executor e, long timeout, TimeUnit timeUnit) {
        return FutureTimeoutUtils.registerFuture(callAsyncNoException(c, e), timeout, timeUnit);
    }
}