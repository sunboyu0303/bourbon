package com.github.bourbon.base.lang.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/2 15:48
 */
public interface Lockable {

    Lock getLock();

    default void lock() {
        getLock().lock();
    }

    default void lockInterruptibly() throws InterruptedException {
        getLock().lockInterruptibly();
    }

    default boolean tryLock() {
        return getLock().tryLock();
    }

    default boolean tryLock(long time, TimeUnit unit) {
        try {
            return getLock().tryLock(time, unit);
        } catch (Exception e) {
            return false;
        }
    }

    default void unlock() {
        getLock().unlock();
    }

    default Condition newCondition() {
        return getLock().newCondition();
    }
}