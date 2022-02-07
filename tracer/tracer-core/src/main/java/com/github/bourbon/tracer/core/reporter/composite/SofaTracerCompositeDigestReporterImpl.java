package com.github.bourbon.tracer.core.reporter.composite;

import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.tracer.core.reporter.facade.AbstractReporter;
import com.github.bourbon.tracer.core.reporter.facade.Reporter;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 11:15
 */
public class SofaTracerCompositeDigestReporterImpl extends AbstractReporter {

    private final Map<String, Reporter> compositedReporters = MapUtils.newConcurrentHashMap();

    public synchronized boolean addReporter(Reporter reporter) {
        if (reporter == null) {
            return false;
        }
        String reporterType = reporter.getReporterType();
        if (compositedReporters.containsKey(reporterType)) {
            return false;
        }
        compositedReporters.put(reporterType, reporter);
        return true;
    }

    @Override
    public String getReporterType() {
        return COMPOSITE_REPORTER;
    }

    @Override
    public void doReport(SofaTracerSpan span) {
        compositedReporters.values().forEach(reporter -> reporter.report(span));
    }
}