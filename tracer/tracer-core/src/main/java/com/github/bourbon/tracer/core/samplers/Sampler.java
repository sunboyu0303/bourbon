package com.github.bourbon.tracer.core.samplers;

import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 10:38
 */
public interface Sampler {

    SamplingStatus sample(SofaTracerSpan sofaTracerSpan);

    String getType();

    void close();
}