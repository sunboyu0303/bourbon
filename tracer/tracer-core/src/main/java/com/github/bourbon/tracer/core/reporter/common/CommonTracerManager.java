package com.github.bourbon.tracer.core.reporter.common;

import com.github.bourbon.base.code.LogCode2Description;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.appender.file.LoadTestAwareAppender;
import com.github.bourbon.tracer.core.appender.manager.AsyncCommonDigestAppenderManager;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.reporter.type.TracerSystemLogEnum;
import com.github.bourbon.tracer.core.span.CommonLogSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 10:38
 */
public class CommonTracerManager {

    private static final AsyncCommonDigestAppenderManager commonReporterAsyncManager = new AsyncCommonDigestAppenderManager(1024);

    private static final SpanEncoder commonSpanEncoder = new CommonSpanEncoder();

    static {
        String logName = TracerSystemLogEnum.MIDDLEWARE_ERROR.getDefaultLogName();
        commonReporterAsyncManager.addAppender(logName, LoadTestAwareAppender.createLoadTestAwareTimedRollingFileAppender(logName, SofaTracerConfiguration.getProperty(TracerSystemLogEnum.MIDDLEWARE_ERROR.getRollingKey()), SofaTracerConfiguration.getProperty(TracerSystemLogEnum.MIDDLEWARE_ERROR.getLogReverseKey())), commonSpanEncoder);
        String profileLogName = TracerSystemLogEnum.RPC_PROFILE.getDefaultLogName();
        commonReporterAsyncManager.addAppender(profileLogName, LoadTestAwareAppender.createLoadTestAwareTimedRollingFileAppender(profileLogName, SofaTracerConfiguration.getProperty(TracerSystemLogEnum.RPC_PROFILE.getRollingKey()), SofaTracerConfiguration.getProperty(TracerSystemLogEnum.RPC_PROFILE.getLogReverseKey())), commonSpanEncoder);

        commonReporterAsyncManager.start("CommonProfileErrorAppender");
    }

    public static void register(String logFileName, String rollingPolicy, String logReserveDay) {
        if (CharSequenceUtils.isBlank(logFileName)) {
            return;
        }
        if (commonReporterAsyncManager.isAppenderAndEncoderExist(logFileName)) {
            SelfLog.warn(logFileName + " has existed in CommonTracerManager");
            return;
        }
        commonReporterAsyncManager.addAppender(logFileName, LoadTestAwareAppender.createLoadTestAwareTimedRollingFileAppender(logFileName, rollingPolicy, logReserveDay), commonSpanEncoder);
    }

    /**
     * Note: The logType of this {@link CommonLogSpan} must be set, otherwise it will not print.
     *
     * @param commonLogSpan The span will be printed
     */
    public static void reportCommonSpan(CommonLogSpan commonLogSpan) {
        if (commonLogSpan != null) {
            String logType = commonLogSpan.getLogType();
            if (CharSequenceUtils.isBlank(logType)) {
                SelfLog.error(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00011"));
                return;
            }
            commonReporterAsyncManager.append(commonLogSpan);
        }
    }

    public static void reportProfile(CommonLogSpan sofaTracerSpan) {
        if (sofaTracerSpan != null) {
            sofaTracerSpan.setLogType(TracerSystemLogEnum.RPC_PROFILE.getDefaultLogName());
            commonReporterAsyncManager.append(sofaTracerSpan);
        }
    }

    public static void reportError(CommonLogSpan sofaTracerSpan) {
        if (sofaTracerSpan != null) {
            sofaTracerSpan.setLogType(TracerSystemLogEnum.MIDDLEWARE_ERROR.getDefaultLogName());
            commonReporterAsyncManager.append(sofaTracerSpan);
        }
    }
}