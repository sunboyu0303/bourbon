package com.github.bourbon.base.ttl;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 23:20
 */
@FunctionalInterface
public interface TtlCopier<T> {

    T copy(T parentValue);
}