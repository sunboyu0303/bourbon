package com.github.bourbon.base.utils.function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 21:58
 */
@FunctionalInterface
public interface BooleanFunc<R> {

    boolean apply(R value);
}