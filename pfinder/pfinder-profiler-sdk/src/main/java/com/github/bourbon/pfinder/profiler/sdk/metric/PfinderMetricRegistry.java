package com.github.bourbon.pfinder.profiler.sdk.metric;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 12:52
 */
public interface PfinderMetricRegistry {

    MetricBuilder.HistogramBuilder histogram(String name);

    MetricBuilder.CounterBuilder counter(String name);

    MetricBuilder.GaugesBuilder gauges(String name, Gauges gauges);

    PfinderMetricRegistry NOOP = new PfinderMetricRegistry() {

        @Override
        public MetricBuilder.HistogramBuilder histogram(String name) {
            return MetricBuilder.HistogramBuilder.NOOP;
        }

        @Override
        public MetricBuilder.CounterBuilder counter(String name) {
            return MetricBuilder.CounterBuilder.NOOP;
        }

        @Override
        public MetricBuilder.GaugesBuilder gauges(String name, Gauges gauges) {
            return MetricBuilder.GaugesBuilder.NOOP;
        }
    };
}