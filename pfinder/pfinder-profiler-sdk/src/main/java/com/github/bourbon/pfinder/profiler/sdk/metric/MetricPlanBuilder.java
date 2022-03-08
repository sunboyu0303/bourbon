package com.github.bourbon.pfinder.profiler.sdk.metric;

import java.util.Collection;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 00:12
 */
public interface MetricPlanBuilder<B extends MetricPlanBuilder<B, M>, M extends Metric> extends BasicBuilder<B> {

    B tagKeys(String... tagKeys);

    MetricPlan<M> build();

    interface HistogramBuilder extends MetricPlanBuilder<HistogramBuilder, Histogram> {
        HistogramBuilder NOOP = new HistogramBuilder() {
            @Override
            public HistogramBuilder tags(MetricTag... tags) {
                return this;
            }

            @Override
            public HistogramBuilder tags(Collection<? extends MetricTag> tags) {
                return this;
            }

            @Override
            public HistogramBuilder tagKeys(String... tagKeys) {
                return this;
            }

            @Override
            public MetricPlan<Histogram> build() {
                return tagValues -> Histogram.NOOP;
            }
        };
    }

    interface CounterBuilder extends MetricPlanBuilder<CounterBuilder, Counter> {
        CounterBuilder NOOP = new CounterBuilder() {
            @Override
            public CounterBuilder tags(MetricTag... tags) {
                return this;
            }

            @Override
            public CounterBuilder tags(Collection<? extends MetricTag> tags) {
                return this;
            }

            @Override
            public CounterBuilder tagKeys(String... tagKeys) {
                return this;
            }

            @Override
            public MetricPlan<Counter> build() {
                return tagValues -> Counter.NOOP;
            }
        };
    }
}