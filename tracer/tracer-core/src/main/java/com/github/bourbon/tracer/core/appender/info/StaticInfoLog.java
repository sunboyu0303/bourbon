package com.github.bourbon.tracer.core.appender.info;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.tracer.core.utils.TracerUtils;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 13:45
 */
public final class StaticInfoLog {

    private static final TraceAppender appender = new TimedRollingFileAppender("static-info.log", true);

    public static synchronized void logStaticInfo() {
        try {
            String log = TracerUtils.getPID() + "," + TracerUtils.getInetAddress() + "," + TracerUtils.getCurrentZone() + "," + TracerUtils.getDefaultTimeZone();
            appender.append(log + "\n");
            appender.flush();
        } catch (IOException e) {
            SelfLog.error("", e);
        }
    }

    private StaticInfoLog() {
    }
}