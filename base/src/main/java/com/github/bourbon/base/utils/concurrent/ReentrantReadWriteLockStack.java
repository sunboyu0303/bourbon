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
        try {
            writeLock.lock();
            super.push(e);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public E pop() {
        try {
            writeLock.lock();
            return super.pop();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public E peek() {
        try {
            readLock.lock();
            return super.peek();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public E get(int index) {
        try {
            readLock.lock();
            return super.get(index);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public E set(int index, E value) {
        try {
            writeLock.lock();
            return super.set(index, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        try {
            writeLock.lock();
            return super.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            readLock.lock();
            return super.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            readLock.lock();
            return super.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            writeLock.lock();
            super.clear();
        } finally {
            writeLock.unlock();
        }
    }
}