package com.github.bourbon.base.utils.function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/17 15:03
 */
@FunctionalInterface
public interface Editor<T> {

    T edit(T t);
}