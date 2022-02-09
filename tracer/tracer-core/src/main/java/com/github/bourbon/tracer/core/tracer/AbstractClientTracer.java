package com.github.bourbon.tracer.core.tracer;

import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 14:59
 */
public abstract class AbstractClientTracer extends AbstractTracer {

    protected AbstractClientTracer(String tracerType) {
        super(tracerType, true, false);
    }

    @Override
    protected String getServerDigestReporterLogName() {
        return null;
    }

    @Override
    protected String getServerDigestReporterRollingKey() {
        return null;
    }

    @Override
    protected String getServerDigestReporterLogNameKey() {
        return null;
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getServerDigestEncoder() {
        return null;
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateServerStatReporter() {
        return null;
    }
}