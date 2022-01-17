package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.lang.Stack;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/17 18:34
 */
public class ReentrantReadWriteLockStack<E> extends Stack<E> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    @Override
    public void push(E e) {
        writeLock.lock();
        try {
            super.push(e);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public E pop() {
        writeLock.lock();
        try {
            return super.pop();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public E peek() {
        readLock.lock();
        try {
            return super.peek();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public E get(int index) {
        readLock.lock();
        try {
            return super.get(index);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public E set(int index, E value) {
        writeLock.lock();
        try {
            return super.set(index, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        writeLock.lock();
        try {
            return super.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return super.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return super.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            super.clear();
        } finally {
            writeLock.unlock();
        }
    }
}