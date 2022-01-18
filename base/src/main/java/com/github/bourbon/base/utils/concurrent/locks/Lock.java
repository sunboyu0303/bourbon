package com.github.bourbon.base.utils.concurrent.locks;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.TimeUnitUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 10:56
 */
public interface Lock {

    default void lock() {
        boolean bool;
        do {
            bool = tryLock();
            TimeUnitUtils.sleepMilliSeconds(1L);
        } while (!bool);
    }

    boolean tryLock();

    default boolean tryLock(long time, TimeUnit unit) {
        long startTime = Clock.currentTimeMillis();
        long expiryTime = unit.toMillis(time);
        boolean bool;
        do {
            bool = tryLock();
            if (!bool) {
                if ((Clock.currentTimeMillis() - startTime) > expiryTime) {
                    return false;
                }
                TimeUnitUtils.sleepMilliSeconds(1L);
            }
        } while (!bool);
        return true;
    }

    void unlock();
}