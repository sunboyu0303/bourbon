package com.github.bourbon.tracer.core.context.trace;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.EmptyStackException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:31
 */
public class SofaTracerThreadLocalTraceContext implements SofaTraceContext {

    private final ThreadLocal<SofaTracerSpan> threadLocal = new ThreadLocal<>();

    @Override
    public void push(SofaTracerSpan span) {
        if (span != null) {
            threadLocal.set(span);
        }
    }

    @Override
    public SofaTracerSpan getCurrentSpan() throws EmptyStackException {
        return threadLocal.get();
    }

    @Override
    public SofaTracerSpan pop() throws EmptyStackException {
        SofaTracerSpan sofaTracerSpan = threadLocal.get();
        if (sofaTracerSpan != null) {
            this.clear();
        }
        return sofaTracerSpan;
    }

    @Override
    public int getThreadLocalSpanSize() {
        return BooleanUtils.defaultIfFalse(threadLocal.get() == null, 0, 1);
    }

    @Override
    public boolean isEmpty() {
        return threadLocal.get() == null;
    }

    @Override
    public void clear() {
        threadLocal.remove();
    }
}