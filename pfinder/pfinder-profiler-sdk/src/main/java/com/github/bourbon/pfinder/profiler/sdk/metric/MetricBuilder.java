package com.github.bourbon.pfinder.profiler.sdk.metric;

import java.util.Collection;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 12:49
 */
public interface MetricBuilder<B extends MetricBuilder<B, PB, M>, PB extends MetricPlanBuilder<PB, M>, M extends Metric> extends BasicBuilder<B> {

    /**
     * In order to build a MetricPlan for using dynamic tag
     */
    PB tagKeys(String... tagKeys);

    M build();

    interface HistogramBuilder extends MetricBuilder<HistogramBuilder, MetricPlanBuilder.HistogramBuilder, Histogram> {
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
            public MetricPlanBuilder.HistogramBuilder tagKeys(String... tagKeys) {
                return MetricPlanBuilder.HistogramBuilder.NOOP;
            }

            @Override
            public Histogram build() {
                return Histogram.NOOP;
            }
        };
    }

    interface CounterBuilder extends MetricBuilder<CounterBuilder, MetricPlanBuilder.CounterBuilder, Counter> {
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
            public MetricPlanBuilder.CounterBuilder tagKeys(String... tagKeys) {
                return MetricPlanBuilder.CounterBuilder.NOOP;
            }

            @Override
            public Counter build() {
                return Counter.NOOP;
            }
        };
    }

    interface GaugesBuilder extends BasicBuilder<GaugesBuilder> {

        Gauges build();

        GaugesBuilder NOOP = new GaugesBuilder() {
            @Override
            public GaugesBuilder tags(MetricTag... tags) {
                return this;
            }

            @Override
            public GaugesBuilder tags(Collection<? extends MetricTag> tags) {
                return this;
            }

            @Override
            public Gauges build() {
                return Gauges.NOOP;
            }
        };
    }
}