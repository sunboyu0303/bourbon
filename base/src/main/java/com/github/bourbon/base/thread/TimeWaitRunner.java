package com.github.bourbon.base.thread;

import com.github.bourbon.base.lang.Clock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 13:29
 */
public class TimeWaitRunner {
    private final long waitTime;
    private final boolean runImmediately;
    private volatile long lastLogTime;

    public TimeWaitRunner(long waitTimeMills) {
        this(waitTimeMills, false);
    }

    public TimeWaitRunner(long waitTimeMills, boolean runImmediately) {
        this.waitTime = waitTimeMills;
        this.runImmediately = runImmediately;
    }

    public void doWithRunnable(Runnable runnable) {
        long currentTimeMillis = Clock.currentTimeMillis();
        if (runImmediately) {
            runnable.run();
        } else if (currentTimeMillis > lastLogTime + waitTime) {
            synchronized (this) {
                if (currentTimeMillis > lastLogTime + waitTime) {
                    lastLogTime = currentTimeMillis;
                    runnable.run();
                }
            }
        }
    }
}