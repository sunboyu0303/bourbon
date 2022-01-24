package com.github.bourbon.tracer.core.context.trace;

import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:28
 */
public interface SofaTraceContext {

    void push(SofaTracerSpan span);

    SofaTracerSpan getCurrentSpan();

    SofaTracerSpan pop();

    int getThreadLocalSpanSize();

    void clear();

    boolean isEmpty();
}