package com.github.bourbon.tracer.core.reporter.digest;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.appender.file.LoadTestAwareAppender;
import com.github.bourbon.tracer.core.appender.manager.AsyncCommonDigestAppenderManager;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.reporter.digest.manager.SofaTracerDigestReporterAsyncManager;
import com.github.bourbon.tracer.core.reporter.stat.SofaTracerStatisticReporter;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 11:22
 */
public class DiskReporterImpl extends AbstractDiskReporter {

    private final AtomicBoolean isDigestFileInited = new AtomicBoolean(false);

    private final String digestLogType;

    private final String digestRollingPolicy;

    private String digestLogReserveConfig;

    private final SpanEncoder contextEncoder;

    private String logNameKey;

    private SofaTracerStatisticReporter statReporter;

    public DiskReporterImpl(String digestLogType, SpanEncoder contextEncoder) {
        this(digestLogType, StringConstants.EMPTY, StringConstants.EMPTY, contextEncoder);
    }

    public DiskReporterImpl(String digestLogType, String digestRollingPolicy, String digestLogReserveConfig, SpanEncoder contextEncoder) {
        this(digestLogType, digestRollingPolicy, digestLogReserveConfig, contextEncoder, null);
    }

    public DiskReporterImpl(String digestLogType, String digestRollingPolicy, String digestLogReserveConfig, SpanEncoder contextEncoder, SofaTracerStatisticReporter statReporter) {
        this(digestLogType, digestRollingPolicy, digestLogReserveConfig, contextEncoder, statReporter, null);
    }

    public DiskReporterImpl(String digestLogType, String digestRollingPolicy, String digestLogReserveConfig, SpanEncoder contextEncoder, SofaTracerStatisticReporter statReporter, String logNameKey) {
        Assert.notBlank(digestLogType, "digestLogType can't be empty");
        this.digestLogType = digestLogType;
        this.digestRollingPolicy = digestRollingPolicy;
        this.digestLogReserveConfig = digestLogReserveConfig;
        this.contextEncoder = contextEncoder;
        this.statReporter = statReporter;
        this.logNameKey = logNameKey;
    }

    public SofaTracerStatisticReporter getStatReporter() {
        return statReporter;
    }

    public void setStatReporter(SofaTracerStatisticReporter statReporter) {
        this.statReporter = statReporter;
    }

    @Override
    public String getDigestReporterType() {
        return this.digestLogType;
    }

    @Override
    public String getStatReporterType() {
        return ObjectUtils.defaultIfNull(statReporter, SofaTracerStatisticReporter::getStatTracerName, StringConstants.EMPTY);
    }

    @Override
    public void digestReport(SofaTracerSpan span) {
        // lazy initialization
        if (!isDigestFileInited.get()) {
            initDigestFile();
        }
        AsyncCommonDigestAppenderManager asyncDigestManager = SofaTracerDigestReporterAsyncManager.getSofaTracerDigestReporterAsyncManager();
        if (asyncDigestManager.isAppenderAndEncoderExist(digestLogType)) {
            // Print only when appender and encoder are present
            asyncDigestManager.append(span);
        } else {
            SelfLog.warn(span.toString() + " have no logType set, so ignore data persistence.");
        }
    }

    @Override
    public void statisticReport(SofaTracerSpan span) {
        if (statReporter != null) {
            statReporter.reportStat(span);
        }
    }

    public AtomicBoolean getIsDigestFileInited() {
        return isDigestFileInited;
    }

    public String getDigestLogType() {
        return digestLogType;
    }

    public String getDigestRollingPolicy() {
        return digestRollingPolicy;
    }

    public String getDigestLogReserveConfig() {
        return digestLogReserveConfig;
    }

    public SpanEncoder getContextEncoder() {
        return contextEncoder;
    }

    public String getLogNameKey() {
        return logNameKey;
    }

    private synchronized void initDigestFile() {
        if (isDigestFileInited.get()) {
            // double check init
            return;
        }
        if (CharSequenceUtils.isNotBlank(logNameKey)) {
            String currentDigestLogReserveConfig = SofaTracerConfiguration.getLogReserveConfig(logNameKey);
            if (!currentDigestLogReserveConfig.equals(digestLogReserveConfig)) {
                SelfLog.info("the lognamekey : " + logNameKey + " take effect. the old logreserveconfig is " + digestLogReserveConfig + " and the new logreverseconfig is " + currentDigestLogReserveConfig);
                digestLogReserveConfig = currentDigestLogReserveConfig;
            }
        }
        // registry digest
        AsyncCommonDigestAppenderManager asyncDigestManager = SofaTracerDigestReporterAsyncManager.getSofaTracerDigestReporterAsyncManager();
        if (!asyncDigestManager.isAppenderAndEncoderExist(digestLogType)) {
            asyncDigestManager.addAppender(digestLogType, LoadTestAwareAppender.createLoadTestAwareTimedRollingFileAppender(this.digestLogType, this.digestRollingPolicy, this.digestLogReserveConfig), this.contextEncoder);
        }
        // Already exists or created for the first time
        isDigestFileInited.set(true);
    }
}