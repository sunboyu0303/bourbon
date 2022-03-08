package com.github.bourbon.pfinder.profiler.sdk.util;

import com.github.bourbon.pfinder.profiler.sdk.metric.MetricTag;

import java.util.ArrayList;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 21:25
 */
public class MetricTags extends ArrayList<MetricTag> {

    public static MetricTags create() {
        return new MetricTags();
    }

    public MetricTags tag(String key, String value) {
        add(MetricTag.of(key, value));
        return this;
    }
}