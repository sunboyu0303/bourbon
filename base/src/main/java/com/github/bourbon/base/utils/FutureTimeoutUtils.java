package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.NamedThreadFactory;
import com.github.bourbon.base.utils.timer.HashedWheelTimer;
import com.github.bourbon.base.utils.timer.Timeout;
import com.github.bourbon.base.utils.timer.Timer;
import com.github.bourbon.base.utils.timer.TimerTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 14:00
 */
public final class FutureTimeoutUtils {

    private static final Timer TIMER = new HashedWheelTimer(new NamedThreadFactory("future-timeout-clean"), 50, TimeUnit.MILLISECONDS);

    public static <T> CompletableFuture<T> registerFuture(CompletableFuture<T> future, long timeout, TimeUnit timeUnit) {
        TIMER.newTimeout(new TimeoutTask(future), timeout, timeUnit);
        return future;
    }

    private static class TimeoutTask implements TimerTask {
        private CompletableFuture<?> future;
        private TimeoutTask(CompletableFuture<?> future) {
            this.future = future;
        }

        @Override
        public void run(Timeout timeout) {
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
    }

    private FutureTimeoutUtils() {
    }
}