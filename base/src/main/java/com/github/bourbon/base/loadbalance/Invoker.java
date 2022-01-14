package com.github.bourbon.base.loadbalance;

import com.github.bourbon.base.lang.mutable.MutableObject;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/3 12:49
 */
public class Invoker<T> extends MutableObject<T> {
    private static final long serialVersionUID = -8689875074517360182L;
    private final String identity;
    private final int weight;

    private Invoker(T value, int weight) {
        super(value);
        this.weight = weight;
        this.identity = value.toString();
    }

    public String getIdentity() {
        return identity;
    }

    public int getWeight() {
        return weight;
    }

    public static <T> Invoker<T> of(T value) throws NullPointerException {
        return of(value, 1);
    }

    public static <T> Invoker<T> of(T value, int weight) throws NullPointerException {
        return new Invoker<>(ObjectUtils.requireNonNull(value, "value == null"), IntUtils.checkPositive(weight, "weight == null"));
    }
}