package com.github.bourbon.tracer.core.tracer;

import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 15:00
 */
public abstract class AbstractServerTracer extends AbstractTracer {

    protected AbstractServerTracer(String tracerType) {
        super(tracerType, false, true);
    }

    @Override
    protected String getClientDigestReporterLogName() {
        return null;
    }

    @Override
    protected String getClientDigestReporterRollingKey() {
        return null;
    }

    @Override
    protected String getClientDigestReporterLogNameKey() {
        return null;
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getClientDigestEncoder() {
        return null;
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateClientStatReporter() {
        return null;
    }
}