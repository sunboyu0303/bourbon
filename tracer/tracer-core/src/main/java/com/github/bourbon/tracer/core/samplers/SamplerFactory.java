package com.github.bourbon.tracer.core.samplers;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 11:01
 */
public class SamplerFactory {

    private static SamplerProperties samplerProperties;

    static {
        float percentage = 100;
        try {
            String perStr = SofaTracerConfiguration.getProperty(SofaTracerConfiguration.SAMPLER_STRATEGY_PERCENTAGE_KEY);
            if (CharSequenceUtils.isNotBlank(perStr)) {
                percentage = Float.parseFloat(perStr);
            }
        } catch (Exception e) {
            SelfLog.error("It will be use default percentage value :100;", e);
            percentage = 100;
        }
        samplerProperties = new SamplerProperties(percentage, SofaTracerConfiguration.getProperty(SofaTracerConfiguration.SAMPLER_STRATEGY_CUSTOM_RULE_CLASS_NAME));
    }

    public static Sampler getSampler() throws Exception {
        String ruleClassName = samplerProperties.getRuleClassName();
        if (CharSequenceUtils.isNotBlank(ruleClassName)) {
            Object instance = Class.forName(ruleClassName).newInstance();
            if (instance instanceof Sampler) {
                return (Sampler) instance;
            }
        }
        return new SofaTracerPercentageBasedSampler(samplerProperties);
    }
}