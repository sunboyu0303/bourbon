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
        try {
            lock.lock();
            super.push(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E pop() {
        try {
            lock.lock();
            return super.pop();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E peek() {
        try {
            lock.lock();
            return super.peek();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E get(int index) {
        try {
            lock.lock();
            return super.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E set(int index, E value) {
        try {
            lock.lock();
            return super.set(index, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        try {
            lock.lock();
            return super.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.lock();
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            lock.lock();
            return super.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            super.clear();
        } finally {
            lock.unlock();
        }
    }
}