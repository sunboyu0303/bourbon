package com.github.bourbon.ump.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.LocalDateTimeUtils;
import com.github.bourbon.ump.LogHandler;
import com.github.bourbon.ump.RuntimeEnvironmentService;
import com.github.bourbon.ump.UmpLoggerFactory;
import com.github.bourbon.ump.constant.UMPConstants;
import com.github.bourbon.ump.domain.LogType;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 17:32
 */
public class DiskFileReporter extends AbstractReporter {

    private static final String NAME = "UMP-DiskFileReporter";
    private static final String LINE_SEP = SystemInfo.osInfo.getLineSeparator();

    @Inject
    private UmpLoggerFactory umpLoggerFactory;

    @Inject
    private RuntimeEnvironmentService runtimeEnvironmentService;

    @Override
    protected LogHandler getLogHandler(LogType logType) {
        switch (logType) {
            case TP:
                return new TpBatchLogger(umpLoggerFactory.tpLogger());
            case JVM:
                return new BatchLogger(umpLoggerFactory.jvmLogger());
            case ALIVE:
                return new BatchLogger(umpLoggerFactory.aliveLogger());
            default:
                throw new IllegalArgumentException("logType in ( TP, JVM, ALIVE )");
        }
    }

    private class BatchLogger implements LogHandler {
        private final Logger logger;

        private BatchLogger(Logger logger) {
            this.logger = logger;
        }

        public void handle(List<String> col) {
            if (!CollectionUtils.isEmpty(col)) {
                info(LocalDateTimeUtils.localDateTimeNowFormat(), String.join(StringConstants.COMMA, col));
            }
        }

        void info(Object o1, Object o2) {
            logger.info("@{\"t\":" + o1 + ",\"i\":\"" + runtimeEnvironmentService.getIp() + "\",\"h\":\"" + runtimeEnvironmentService.getHostName() + "\",\"v\":" + UMPConstants.PROFILER_VERSION + ",\"l\":[" + o2 + "]}" + LINE_SEP);
        }
    }

    private class TpBatchLogger extends BatchLogger {

        private TpBatchLogger(Logger logger) {
            super(logger);
        }

        @Override
        public void handle(List<String> col) {
            if (!CollectionUtils.isEmpty(col)) {
                String[] elements = col.toArray(StringConstants.EMPTY_STRING_ARRAY);
                long currentTime = Long.parseLong(elements[0].substring(0, 10));
                StringBuilder sb = new StringBuilder(2048);
                boolean isLogBegin = true;
                int i = 0;

                for (int len = elements.length; i < len; ++i) {
                    long time = Long.parseLong(elements[i].substring(0, 10));
                    String log = elements[i].substring(10);
                    if (time != currentTime && sb.length() != 0) {
                        info(currentTime, sb);
                        currentTime = time;
                        isLogBegin = true;
                        sb.delete(0, sb.length());
                    }

                    if (isLogBegin) {
                        isLogBegin = false;
                        sb.append(log);
                    } else {
                        sb.append(StringConstants.COMMA).append(log);
                    }

                    if (i == len - 1 || sb.length() > 4096) {
                        info(currentTime, sb);
                        sb.delete(0, sb.length());
                        currentTime = time;
                        isLogBegin = true;
                    }
                }
            }
        }
    }

    @Override
    protected String getName() {
        return NAME;
    }
}