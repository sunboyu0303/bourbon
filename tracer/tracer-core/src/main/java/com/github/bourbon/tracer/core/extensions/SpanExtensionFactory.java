package com.github.bourbon.tracer.core.extensions;

import com.github.bourbon.base.utils.SetUtils;
import io.opentracing.Span;

import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 14:40
 */
public final class SpanExtensionFactory {

    private static final Set<SpanExtension> spanExtensions = SetUtils.newHashSet(ServiceLoader.load(SpanExtension.class));

    public static void logStartedSpan(Span currentSpan) {
        if (!spanExtensions.isEmpty() && currentSpan != null) {
            spanExtensions.forEach(e -> e.logStartedSpan(currentSpan));
        }
    }

    public static void logStoppedSpan(Span currentSpan) {
        if (!spanExtensions.isEmpty() && currentSpan != null) {
            spanExtensions.forEach(e -> e.logStoppedSpan(currentSpan));
        }
    }

    public static void logStoppedSpanInRunnable(Span currentSpan) {
        if (!spanExtensions.isEmpty() && currentSpan != null) {
            spanExtensions.forEach(e -> e.logStoppedSpanInRunnable(currentSpan));
        }
    }

    private SpanExtensionFactory() {
    }
}