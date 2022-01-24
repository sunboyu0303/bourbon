package com.github.bourbon.tracer.core.samplers;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 11:04
 */
public class SamplerProperties {

    private float percentage = 100;

    private String ruleClassName;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getRuleClassName() {
        return ruleClassName;
    }

    public void setRuleClassName(String ruleClassName) {
        this.ruleClassName = ruleClassName;
    }
}