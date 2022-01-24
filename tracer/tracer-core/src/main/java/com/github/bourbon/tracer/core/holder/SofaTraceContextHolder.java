package com.github.bourbon.tracer.core.holder;

import com.github.bourbon.tracer.core.context.trace.SofaTraceContext;
import com.github.bourbon.tracer.core.context.trace.SofaTracerThreadLocalTraceContext;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:26
 */
public final class SofaTraceContextHolder {

    private static final SofaTraceContext SOFA_TRACE_CONTEXT = new SofaTracerThreadLocalTraceContext();

    public static SofaTraceContext getSofaTraceContext() {
        return SOFA_TRACE_CONTEXT;
    }

    private SofaTraceContextHolder() {
    }
}