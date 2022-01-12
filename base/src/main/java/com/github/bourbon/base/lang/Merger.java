package com.github.bourbon.base.lang;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 16:55
 */
public interface Merger<T> {

    T merge(T[] items);
}