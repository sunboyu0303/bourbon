package com.github.bourbon.base.lang;

import com.github.bourbon.base.lang.mutable.MutableObject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/26 10:41
 */
public final class StampedLockHolder<T> extends MutableObject<T> {

    private final StampedLock sl = new StampedLock();

    private StampedLockHolder(T value) {
        super(value);
    }

    public long writeLock() {
        return sl.writeLock();
    }

    public long tryWriteLock() {
        return sl.tryWriteLock();
    }

    public long tryWriteLock(long time, TimeUnit unit) throws InterruptedException {
        return sl.tryWriteLock(time, unit);
    }

    public long writeLockInterruptibly() throws InterruptedException {
        return sl.writeLockInterruptibly();
    }

    public void unlockWrite(long stamp) {
        sl.unlockWrite(stamp);
    }

    public boolean tryUnlockWrite() {
        return sl.tryUnlockWrite();
    }

    public boolean isWriteLocked() {
        return sl.isWriteLocked();
    }

    public long tryOptimisticRead() {
        return sl.tryOptimisticRead();
    }

    public boolean validate(long stamp) {
        return sl.validate(stamp);
    }

    public long readLock() {
        return sl.readLock();
    }

    public long tryReadLock() {
        return sl.tryReadLock();
    }

    public long tryReadLock(long time, TimeUnit unit) throws InterruptedException {
        return sl.tryReadLock(time, unit);
    }

    public long readLockInterruptibly() throws InterruptedException {
        return sl.readLockInterruptibly();
    }

    public void unlockRead(long stamp) {
        sl.unlockRead(stamp);
    }

    public boolean tryUnlockRead() {
        return sl.tryUnlockRead();
    }

    public boolean isReadLocked() {
        return sl.isReadLocked();
    }

    public int getReadLockCount() {
        return sl.getReadLockCount();
    }

    public long tryConvertToWriteLock(long stamp) {
        return sl.tryConvertToWriteLock(stamp);
    }

    public long tryConvertToReadLock(long stamp) {
        return sl.tryConvertToReadLock(stamp);
    }

    public long tryConvertToOptimisticRead(long stamp) {
        return sl.tryConvertToOptimisticRead(stamp);
    }

    public Lock asReadLock() {
        return sl.asReadLock();
    }

    public Lock asWriteLock() {
        return sl.asWriteLock();
    }

    public ReadWriteLock asReadWriteLock() {
        return sl.asReadWriteLock();
    }

    public void unlock(long stamp) {
        sl.unlock(stamp);
    }

    public static <T> StampedLockHolder<T> of(T v) {
        return new StampedLockHolder<>(v);
    }
}