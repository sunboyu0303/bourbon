package com.github.bourbon.tracer.core.appender.self;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.Timestamp;
import com.github.bourbon.tracer.core.appender.manager.AsyncCommonAppenderManager;
import com.github.bourbon.tracer.core.constants.LogConstants;
import com.github.bourbon.tracer.core.utils.TracerUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 13:55
 */
public final class SelfLog {

    private static final AsyncCommonAppenderManager APPENDER = new AsyncCommonAppenderManager(1024, "tracer-self.log").start("SelfLogAppender");

    public static void error(String log, Throwable e) {
        StringWriter sw = new StringWriter(4096);
        PrintWriter pw = new PrintWriter(sw, false);
        pw.append(Timestamp.currentTime()).append(LogConstants.ERROR_PREFIX).append(log).append(StringConstants.NEWLINE);
        e.printStackTrace(pw);
        pw.println();
        pw.flush();
        APPENDER.append(sw.toString());
    }

    public static void errorWithTraceId(String log, Throwable e) {
        StringWriter sw = new StringWriter(4096);
        PrintWriter pw = new PrintWriter(sw, false);
        pw.append(Timestamp.currentTime()).append(LogConstants.ERROR_PREFIX).append(CharConstants.LEFT_BRACKETS).append(TracerUtils.getTraceId()).append(CharConstants.RIGHT_BRACKETS).append(log).append(StringConstants.NEWLINE);
        e.printStackTrace(pw);
        pw.println();
        pw.flush();
        APPENDER.append(sw.toString());
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
        APPENDER.append(Timestamp.currentTime() + prefix + log + StringConstants.NEWLINE);
    }

    private SelfLog() {
    }
}