package com.github.bourbon.tracer.core.appender.info;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.LocalHostUtils;
import com.github.bourbon.base.utils.PidUtils;
import com.github.bourbon.tracer.core.appender.file.TimedRollingFileAppender;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.utils.TracerUtils;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 13:45
 */
public final class StaticInfoLog {

    private static final TraceAppender APPENDER = new TimedRollingFileAppender("static-info.log", true);

    public static synchronized void logStaticInfo() {
        try {
            APPENDER.append(PidUtils.pid() + StringConstants.COMMA + LocalHostUtils.ip() + StringConstants.COMMA + TracerUtils.getCurrentZone() + StringConstants.COMMA + TracerUtils.getDefaultTimeZoneId() + CharConstants.LF);
            APPENDER.flush();
        } catch (IOException e) {
            SelfLog.error(StringConstants.EMPTY, e);
        }
    }

    private StaticInfoLog() {
    }
}