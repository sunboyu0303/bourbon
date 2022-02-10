package com.github.bourbon.tracer.core.extensions.log;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.constants.MDCKeyConstants;
import com.github.bourbon.tracer.core.extensions.SpanExtension;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;
import io.opentracing.Span;
import org.slf4j.MDC;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/7 23:26
 */
public final class MDCSpanExtension implements SpanExtension {

    @Override
    public void logStartedSpan(Span currentSpan) {
        ObjectUtils.nonNullConsumer(currentSpan, s -> ObjectUtils.nonNullConsumer(((SofaTracerSpan) s).getSofaTracerSpanContext(), c -> {
            MDC.put(MDCKeyConstants.MDC_TRACE_ID, c.getTraceId());
            MDC.put(MDCKeyConstants.MDC_SPAN_ID, c.getSpanId());
        }));
    }

    @Override
    public void logStoppedSpan(Span currentSpan) {
        MDC.remove(MDCKeyConstants.MDC_TRACE_ID);
        MDC.remove(MDCKeyConstants.MDC_SPAN_ID);
        ObjectUtils.nonNullConsumer(currentSpan, s -> ObjectUtils.nonNullConsumer(((SofaTracerSpan) s).getParentSofaTracerSpan(), p -> ObjectUtils.nonNullConsumer(p.getSofaTracerSpanContext(), c -> {
            MDC.put(MDCKeyConstants.MDC_TRACE_ID, c.getTraceId());
            MDC.put(MDCKeyConstants.MDC_SPAN_ID, c.getSpanId());
        })));
    }

    @Override
    public void logStoppedSpanInRunnable(Span currentSpan) {
        MDC.remove(MDCKeyConstants.MDC_TRACE_ID);
        MDC.remove(MDCKeyConstants.MDC_SPAN_ID);
    }

    @Override
    public String supportName() {
        return "slf4j-mdc";
    }
}