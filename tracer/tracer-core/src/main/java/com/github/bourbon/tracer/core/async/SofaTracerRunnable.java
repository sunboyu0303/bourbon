package com.github.bourbon.tracer.core.async;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.tracer.core.context.trace.SofaTraceContext;
import com.github.bourbon.tracer.core.extensions.SpanExtensionFactory;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:16
 */
public class SofaTracerRunnable implements Runnable {

    private final long tid = Thread.currentThread().getId();
    private final Runnable wrappedRunnable;
    private final SofaTraceContext traceContext;
    private final SofaTracerSpan currentSpan;

    public SofaTracerRunnable(Runnable wrappedRunnable) {
        this(wrappedRunnable, SofaTraceContextHolder.getSofaTraceContext());
    }

    public SofaTracerRunnable(Runnable wrappedRunnable, SofaTraceContext traceContext) {
        this.wrappedRunnable = wrappedRunnable;
        this.traceContext = traceContext;
        this.currentSpan = BooleanUtils.defaultIfFalse(!this.traceContext.isEmpty(), this.traceContext::getCurrentSpan);
    }

    @Override
    public void run() {
        if (Thread.currentThread().getId() != tid) {
            if (currentSpan != null) {
                traceContext.push(currentSpan);
                SpanExtensionFactory.logStartedSpan(currentSpan);
            }
        }
        try {
            wrappedRunnable.run();
        } finally {
            if (Thread.currentThread().getId() != tid) {
                if (currentSpan != null) {
                    traceContext.pop();
                }
            }
        }
    }
}