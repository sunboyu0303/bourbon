package com.github.bourbon.base.utils.function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 10:43
 */
@FunctionalInterface
public interface IntFunc<R> {

    int apply(R value);
}