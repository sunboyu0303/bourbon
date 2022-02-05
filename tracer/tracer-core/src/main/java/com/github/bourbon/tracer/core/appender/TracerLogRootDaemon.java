package com.github.bourbon.tracer.core.appender;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.PidUtils;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.appender.self.TracerDaemon;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 13:52
 */
public final class TracerLogRootDaemon {

    public static String LOG_FILE_DIR;

    static {
        String loggingRoot = System.getProperty("SOFA_TRACER_LOGGING_PATH");
        if (CharSequenceUtils.isBlank(loggingRoot)) {
            loggingRoot = System.getenv("SOFA_TRACER_LOGGING_PATH");
        }
        if (CharSequenceUtils.isBlank(loggingRoot)) {
            loggingRoot = System.getProperty("loggingRoot");
        }
        if (CharSequenceUtils.isBlank(loggingRoot)) {
            loggingRoot = System.getProperty("logging.path");
        }
        if (CharSequenceUtils.isBlank(loggingRoot)) {
            loggingRoot = SystemInfo.userInfo.getHome() + "logs";
        }
        String temp = loggingRoot + "/tracelog";
        LOG_FILE_DIR = BooleanUtils.defaultIfPredicate(System.getProperty("tracer_append_pid_to_log_path"), BooleanUtils::toBoolean, v -> temp + StringConstants.SLASH + PidUtils.pid(), temp);

        TracerDaemon.start();
        SelfLog.info("LOG_FILE_DIR is " + LOG_FILE_DIR);
    }

    private TracerLogRootDaemon() {
    }
}