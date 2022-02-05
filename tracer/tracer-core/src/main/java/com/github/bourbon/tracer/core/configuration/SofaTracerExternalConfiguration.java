package com.github.bourbon.tracer.core.configuration;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 15:35
 */
public interface SofaTracerExternalConfiguration {

    String getValue(String key);

    boolean contains(String key);
}