package com.github.bourbon.pfinder.profiler.sdk.metric;

import com.github.bourbon.pfinder.profiler.sdk.util.Numbers;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 23:58
 */
public interface Gauges<T extends Number> extends Metric {

    T getValue();

    Gauges<Number> NOOP = () -> Numbers.ZERO;
}