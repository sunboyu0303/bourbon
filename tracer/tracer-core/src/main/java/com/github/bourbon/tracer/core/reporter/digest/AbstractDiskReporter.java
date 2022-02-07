package com.github.bourbon.tracer.core.reporter.digest;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.reporter.facade.AbstractReporter;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 11:30
 */
public abstract class AbstractDiskReporter extends AbstractReporter {

    @Override
    public String getReporterType() {
        // By default, the type of the digest log is used as the type of span
        return getDigestReporterType();
    }

    @Override
    public void doReport(SofaTracerSpan span) {
        // Set the log type for easy printing, otherwise it will not print correctly.
        span.setLogType(getDigestReporterType());
        if (!isDisableDigestLog(span)) {
            digestReport(span);
        }
        statisticReport(span);
    }

    public abstract String getDigestReporterType();

    public abstract String getStatReporterType();

    public abstract void digestReport(SofaTracerSpan span);

    public abstract void statisticReport(SofaTracerSpan span);

    protected boolean isDisableDigestLog(SofaTracerSpan span) {
        if (span == null || span.context() == null) {
            return true;
        }
        SofaTracerSpanContext sofaTracerSpanContext = (SofaTracerSpanContext) span.context();
        // sampled is false; this span will not be report
        if (!sofaTracerSpanContext.isSampled()) {
            return true;
        }
        boolean allDisabled = Boolean.TRUE.toString().equalsIgnoreCase(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.DISABLE_MIDDLEWARE_DIGEST_LOG_KEY));

        if (allDisabled) {
            return true;
        }
        // digest log type
        String logType = span.getLogType();
        if (CharSequenceUtils.isBlank(logType)) {
            // if the digest log type is empty, it will not be printed.
            return true;
        }
        // Rpc-2-jvm special handling, adapting rpc2jvm to close digest and only print stat
        if (SofaTracerConstants.RPC_2_JVM_DIGEST_LOG_NAME.equals(logType)) {
            if (Boolean.FALSE.toString().equalsIgnoreCase(SofaTracerConfiguration.getProperty("enable_rpc_2_jvm_digest_log"))) {
                return true;
            }
        }
        return Boolean.TRUE.toString().equalsIgnoreCase(SofaTracerConfiguration.getMapEmptyIfNull(SofaTracerConfiguration.DISABLE_DIGEST_LOG_KEY).get(logType));
    }
}