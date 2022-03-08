package com.github.bourbon.pfinder.profiler.sdk.metric;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 00:07
 */
public class MetricTag {

    public static MetricTag of(String key, String value) {
        return new MetricTag(key, value);
    }

    private String key;

    private String value;

    private MetricTag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public MetricTag setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public MetricTag setValue(String value) {
        this.value = value;
        return this;
    }
}