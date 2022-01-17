package com.github.bourbon.base.lang;

import com.github.bourbon.base.utils.ListUtils;

import java.util.EmptyStackException;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 17:46
 */
public class Stack<E> {

    private int mSize = 0;

    private final List<E> mElements = ListUtils.newArrayList();

    public void push(E e) {
        if (mElements.size() > mSize) {
            mElements.set(mSize, e);
        } else {
            mElements.add(e);
        }
        mSize++;
    }

    public E pop() {
        if (mSize == 0) {
            throw new EmptyStackException();
        }
        return mElements.set(--mSize, null);
    }

    public E peek() {
        if (mSize == 0) {
            throw new EmptyStackException();
        }
        return mElements.get(mSize - 1);
    }

    public E get(int index) {
        if (index >= mSize || index + mSize < 0) {
            throw forInput(index, mSize);
        }
        return index < 0 ? mElements.get(index + mSize) : mElements.get(index);
    }

    public E set(int index, E value) {
        if (index >= mSize || index + mSize < 0) {
            throw forInput(index, mSize);
        }
        return mElements.set(index < 0 ? index + mSize : index, value);
    }

    public E remove(int index) {
        if (index >= mSize || index + mSize < 0) {
            throw forInput(index, mSize);
        }
        E e = mElements.remove(index < 0 ? index + mSize : index);
        mSize--;
        return e;
    }

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return mSize == 0;
    }

    public void clear() {
        mSize = 0;
        mElements.clear();
    }

    private static IndexOutOfBoundsException forInput(int index, int mSize) {
        return new IndexOutOfBoundsException("Index: " + index + ", Size: " + mSize);
    }
}