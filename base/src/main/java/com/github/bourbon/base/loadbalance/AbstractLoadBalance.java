package com.github.bourbon.base.loadbalance;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CollectionUtils;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/2 21:31
 */
public abstract class AbstractLoadBalance implements ILoadBalance {

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation) {
        return BooleanUtils.defaultIfPredicate(invokers, l -> !CollectionUtils.isEmpty(l),
                l -> BooleanUtils.defaultSupplierIfFalse(l.size() == 1, () -> l.get(0), () -> doSelect(l, invocation))
        );
    }

    protected abstract <T> Invoker<T> doSelect(List<Invoker<T>> invokers, Invocation invocation);
}