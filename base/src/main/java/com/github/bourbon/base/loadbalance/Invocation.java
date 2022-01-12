package com.github.bourbon.base.loadbalance;

import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 10:38
 */
public final class Invocation {

    private final String serviceKey;
    private final Object[] arguments;

    public Invocation(String serviceKey, Object... arguments) {
        this.serviceKey = ObjectUtils.requireNonNull(serviceKey, "Invocation can not hold a null serviceKey!");
        this.arguments = ArrayUtils.requireNonEmpty(arguments, "Invocation can not hold a null arguments!");
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public Object[] getArguments() {
        return arguments;
    }
}