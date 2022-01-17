package com.github.bourbon.base.utils.concurrent;

import com.github.bourbon.base.lang.Stack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/17 18:31
 */
public class ReentrantLockStack<E> extends Stack<E> {

    private final Lock lock = new ReentrantLock();

    @Override
    public void push(E e) {
        lock.lock();
        try {
            super.push(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E pop() {
        lock.lock();
        try {
            return super.pop();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E peek() {
        lock.lock();
        try {
            return super.peek();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E get(int index) {
        lock.lock();
        try {
            return super.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E set(int index, E value) {
        lock.lock();
        try {
            return super.set(index, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.lock();
        try {
            return super.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return super.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        } finally {
            lock.unlock();
        }
    }
}