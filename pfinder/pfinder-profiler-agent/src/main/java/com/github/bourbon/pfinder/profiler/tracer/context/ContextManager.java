package com.github.bourbon.pfinder.profiler.tracer.context;

import com.github.bourbon.pfinder.profiler.service.IdGenerateService;
import com.github.bourbon.pfinder.profiler.service.ProfilerContext;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 17:11
 */
public class ContextManager {
    private static final ThreadLocal<TracingContext> TRACING_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
    private static ProfilerContext PROFILER_CONTEXT;
    private static IdGenerateService ID_GENERATE_SERVICE;
    private static BaseTracingContextConfig TRACING_CONTEXT_CONFIG;

    private ContextManager() {
        throw new UnsupportedOperationException();
    }

    public static void initialize(ProfilerContext profilerContext) {
        PROFILER_CONTEXT = profilerContext;
        ID_GENERATE_SERVICE = profilerContext.getService(IdGenerateService.class);
        TRACING_CONTEXT_CONFIG = new BaseTracingContextConfig(profilerContext);
    }

    public static TracingContext tracingContext() {
        TracingContext context = TRACING_CONTEXT_THREAD_LOCAL.get();
        if (context == null) {
            if (ID_GENERATE_SERVICE == null || !ID_GENERATE_SERVICE.isReady()) {
                return NoopTracingContext.instance();
            }
            context = new BaseTracingContext(PROFILER_CONTEXT, ID_GENERATE_SERVICE, TRACING_CONTEXT_CONFIG);
            TRACING_CONTEXT_THREAD_LOCAL.set(context);
        }
        return context;
    }

    public static void clean() {
        TRACING_CONTEXT_THREAD_LOCAL.remove();
    }

    public static ProfilerContext profilerContext() {
        return PROFILER_CONTEXT;
    }
}