package com.github.bourbon.base.utils.function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 10:43
 */
@FunctionalInterface
public interface LongFunc<R> {

    long apply(R value);
}