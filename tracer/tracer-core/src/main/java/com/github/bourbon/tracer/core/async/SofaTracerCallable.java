package com.github.bourbon.tracer.core.async;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.tracer.core.context.trace.SofaTraceContext;
import com.github.bourbon.tracer.core.extensions.SpanExtensionFactory;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.concurrent.Callable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:09
 */
public class SofaTracerCallable<T> implements Callable<T> {

    private final long tid = Thread.currentThread().getId();
    private final Callable<T> wrappedCallable;
    private final SofaTraceContext traceContext;
    private final SofaTracerSpan currentSpan;

    public SofaTracerCallable(Callable<T> wrappedCallable) {
        this(wrappedCallable, SofaTraceContextHolder.getSofaTraceContext());
    }

    public SofaTracerCallable(Callable<T> wrappedCallable, SofaTraceContext traceContext) {
        this.wrappedCallable = wrappedCallable;
        this.traceContext = traceContext;
        this.currentSpan = BooleanUtils.defaultIfFalse(!this.traceContext.isEmpty(), this.traceContext::getCurrentSpan);
    }

    @Override
    public T call() throws Exception {
        if (Thread.currentThread().getId() != tid) {
            if (currentSpan != null) {
                traceContext.push(currentSpan);
                SpanExtensionFactory.logStartedSpan(currentSpan);
            }
        }
        try {
            return wrappedCallable.call();
        } finally {
            if (Thread.currentThread().getId() != tid) {
                if (currentSpan != null) {
                    traceContext.pop();
                }
            }
        }
    }
}