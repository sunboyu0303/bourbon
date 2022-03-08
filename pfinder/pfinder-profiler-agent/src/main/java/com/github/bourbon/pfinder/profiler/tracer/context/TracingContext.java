package com.github.bourbon.pfinder.profiler.tracer.context;

import com.github.bourbon.base.convert.Converter;
import com.sun.xml.internal.bind.v2.model.core.ID;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 17:14
 */
public interface TracingContext {

    boolean inject(ContextCarrier var1);

    boolean inject(ContextCarrier var1, TracingSpan<?> var2);

    boolean inject(ContextCarrier var1, PrefabbedTracingSnapshot var2);

    void extract(ContextCarrier var1);

    TracingSpan<?> currentSpan();

    TracingSpan<?> createSpan(String var1, Component var2);

    TracingSpan<?> createSpan(String var1, Component var2, ContextCarrier var3, boolean var4);

    TracingSpan<?> createSpan(IntIdHolder var1, Component var2, ContextCarrier var3, boolean var4);

    <T> TracingSpan<?> createSpan(String var1, Component var2, T var3, Converter<T> var4, boolean var5);

    void pushInterceptor(InterceptorContext<?> var1);

    InterceptorContext<?> popInterceptor();

    TracingNode<?> currentTracingNode();

    <T> T getServiceFromTracingEnvironment(Class<T> var1);

    void finishSpan();

    void finishSpan(TracingSpan<?> var1);

    void appendFinishedSpan(TracingSpan<?> var1);

    TracingNode<?> finishSpanButNoReport();

    TracingNode<?> finishSpanButNoReport(TracingSpan<?> var1);

    void report(TracingNode<?> var1);

    TracingSnapshot<?> createTracingSnapshot();

    PrefabbedTracingSnapshot createPrefabbedTracingSnapshot(String var1, Component var2);

    boolean resumeTracingNode(TracingNode<?> var1);

    boolean resumeTracingNode(TracingNode<?> var1, ContextCarrier var2);

    TracingSpan<?> resumeTracingSnapshot(TracingSnapshot<?> var1, Component var2, String var3);

    boolean resumeTracingSnapshot(TracingSnapshot<?> var1);

    TracingSpan<?> resumePrefabbedTracingSnapshot(PrefabbedTracingSnapshot var1);

    boolean isCollectDetail();

    boolean isTracing();

    Component activeSpanComponent();

    ContextCarrier getUpstreamCarrier();

    ID traceId();

    Stack<? extends TracingNode<?>> getTracingNodeStack();

    boolean forceSample();
}