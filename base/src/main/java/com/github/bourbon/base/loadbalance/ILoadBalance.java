package com.github.bourbon.base.loadbalance;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/2 17:47
 */
@SPI(value = "random", scope = ExtensionScope.FRAMEWORK)
public interface ILoadBalance {

    <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation);
}