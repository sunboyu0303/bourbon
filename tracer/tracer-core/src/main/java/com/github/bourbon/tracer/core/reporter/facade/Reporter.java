package com.github.bourbon.tracer.core.reporter.facade;

import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 10:21
 */
public interface Reporter {

    String REMOTE_REPORTER = "REMOTE_REPORTER";

    String COMPOSITE_REPORTER = "COMPOSITE_REPORTER";

    String getReporterType();

    void report(SofaTracerSpan span);

    void close();
}