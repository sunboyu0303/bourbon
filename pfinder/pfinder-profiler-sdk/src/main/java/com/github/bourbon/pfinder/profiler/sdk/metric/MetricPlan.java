package com.github.bourbon.pfinder.profiler.sdk.metric;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 00:11
 */
public interface MetricPlan<T extends Metric> {

    /**
     * tagValue must be paired with tagKey which be given in plan's builder
     */
    T ofTagValues(String... tagValues);

    MetricPlan<Counter> NOOP_COUNTER_PLAN = tagValues -> Counter.NOOP;

    MetricPlan<Histogram> NOOP_HISTOGRAM_PLAN = tagValues -> Histogram.NOOP;
}