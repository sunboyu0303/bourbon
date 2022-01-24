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
public class SpanExtensionFactory {

    private static Set<SpanExtension> spanExtensions = SetUtils.newHashSet(ServiceLoader.load(SpanExtension.class));

    public static void logStartedSpan(Span currentSpan) {
        if (!spanExtensions.isEmpty() && currentSpan != null) {
            spanExtensions.forEach(spanExtension -> spanExtension.logStartedSpan(currentSpan));
        }
    }

    public static void logStoppedSpan(Span currentSpan) {
        if (!spanExtensions.isEmpty() && currentSpan != null) {
            spanExtensions.forEach(spanExtension -> spanExtension.logStoppedSpan(currentSpan));
        }
    }

    public static void logStoppedSpanInRunnable(Span currentSpan) {
        if (!spanExtensions.isEmpty() && currentSpan != null) {
            spanExtensions.forEach(spanExtension -> spanExtension.logStoppedSpanInRunnable(currentSpan));
        }
    }
}