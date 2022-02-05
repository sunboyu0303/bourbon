package com.github.bourbon.tracer.core.listener;

import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 17:35
 */
public interface SpanReportListener {

    void onSpanReport(SofaTracerSpan sofaTracerSpan);
}