package com.github.bourbon.tracer.core.extensions;

import io.opentracing.Span;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 14:39
 */
public interface SpanExtension {

    void logStartedSpan(Span currentSpan);

    void logStoppedSpan(Span currentSpan);

    void logStoppedSpanInRunnable(Span currentSpan);

    String supportName();
}