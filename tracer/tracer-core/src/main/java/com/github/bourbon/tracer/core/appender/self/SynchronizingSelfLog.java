package com.github.bourbon.tracer.core.appender.self;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.Timestamp;
import com.github.bourbon.tracer.core.appender.file.AbstractRollingFileAppender;
import com.github.bourbon.tracer.core.appender.file.TimedRollingFileAppender;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.constants.LogConstants;
import com.github.bourbon.tracer.core.utils.TracerUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 17:05
 */
public final class SynchronizingSelfLog {

    private static final AbstractRollingFileAppender APPENDER = new TimedRollingFileAppender("sync.log",
            BooleanUtils.defaultIfPredicate(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_GLOBAL_ROLLING_KEY), CharSequenceUtils::isNotBlank, t -> t, TimedRollingFileAppender.DAILY_ROLLING_PATTERN),
            SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_GLOBAL_LOG_RESERVE_DAY, String.valueOf(SofaTracerConfiguration.DEFAULT_LOG_RESERVE_DAY))
    );

    public static void error(String log, Throwable e) {
        try {
            StringWriter sw = new StringWriter(4096);
            PrintWriter pw = new PrintWriter(sw, false);
            pw.append(Timestamp.currentTime()).append(LogConstants.ERROR_PREFIX).append(log).append(StringConstants.NEWLINE);
            e.printStackTrace(pw);
            pw.println();
            pw.flush();
            APPENDER.append(sw.toString());
            APPENDER.flush();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void errorWithTraceId(String log, Throwable e) {
        try {
            StringWriter sw = new StringWriter(4096);
            PrintWriter pw = new PrintWriter(sw, false);
            pw.append(Timestamp.currentTime()).append(LogConstants.ERROR_PREFIX).append(CharConstants.LEFT_BRACKETS).append(TracerUtils.getTraceId()).append(CharConstants.RIGHT_BRACKETS).append(log).append(StringConstants.NEWLINE);
            e.printStackTrace(pw);
            pw.println();
            pw.flush();
            APPENDER.append(sw.toString());
            APPENDER.flush();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void info(String log) {
        doLog(log, LogConstants.INFO_PREFIX);
    }

    public static void infoWithTraceId(String log) {
        infoWithTraceId(log, TracerUtils.getTraceId());
    }

    public static void infoWithTraceId(String log, String traceId) {
        doLogWithTraceId(log, LogConstants.INFO_PREFIX, traceId);
    }

    public static void warn(String log) {
        doLog(log, LogConstants.WARN_PREFIX);
    }

    public static void warnWithTraceId(String log) {
        warnWithTraceId(log, TracerUtils.getTraceId());
    }

    public static void warnWithTraceId(String log, String traceId) {
        doLogWithTraceId(log, LogConstants.WARN_PREFIX, traceId);
    }

    public static void error(String log) {
        doLog(log, LogConstants.ERROR_PREFIX);
    }

    public static void errorWithTraceId(String log) {
        errorWithTraceId(log, TracerUtils.getTraceId());
    }

    public static void errorWithTraceId(String log, String traceId) {
        doLogWithTraceId(log, LogConstants.ERROR_PREFIX, traceId);
    }

    private static void doLogWithTraceId(String log, String prefix, String traceId) {
        doLog(log, prefix + CharConstants.LEFT_BRACKETS + traceId + CharConstants.RIGHT_BRACKETS);
    }

    private static void doLog(String log, String prefix) {
        try {
            APPENDER.append(Timestamp.currentTime() + prefix + log + StringConstants.NEWLINE);
            APPENDER.flush();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private SynchronizingSelfLog() {
    }
}