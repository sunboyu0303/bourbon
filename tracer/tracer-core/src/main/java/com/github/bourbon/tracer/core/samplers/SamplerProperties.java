package com.github.bourbon.tracer.core.samplers;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 11:04
 */
public class SamplerProperties {

    private final float percentage;
    private final String ruleClassName;

    public SamplerProperties(float percentage, String ruleClassName) {
        this.percentage = percentage;
        this.ruleClassName = ruleClassName;
    }

    public float getPercentage() {
        return percentage;
    }

    public String getRuleClassName() {
        return ruleClassName;
    }
}