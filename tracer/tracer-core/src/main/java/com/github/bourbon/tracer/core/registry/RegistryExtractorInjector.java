package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import io.opentracing.propagation.Format;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 16:53
 */
public interface RegistryExtractorInjector<T> {

    String FORMATER_KEY_HEAD = "sftc_head";

    Format<T> getFormatType();

    SofaTracerSpanContext extract(T carrier);

    void inject(SofaTracerSpanContext spanContext, T carrier);
}