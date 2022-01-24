package com.github.bourbon.tracer.core.samplers;

import com.github.bourbon.base.utils.MapUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 10:39
 */
public class SamplingStatus {

    private boolean isSampled;

    private Map<String, Object> tags = MapUtils.newHashMap();

    public boolean isSampled() {
        return isSampled;
    }

    public void setSampled(boolean sampled) {
        isSampled = sampled;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }
}