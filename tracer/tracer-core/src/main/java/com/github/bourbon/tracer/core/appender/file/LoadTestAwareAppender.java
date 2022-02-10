package com.github.bourbon.tracer.core.appender.file;

import com.github.bourbon.base.appender.TraceAppender;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 11:45
 */
public final class LoadTestAwareAppender implements TraceAppender {

    private final TraceAppender nonLoadTestTraceAppender;
    private final TraceAppender loadTestTraceAppender;

    private LoadTestAwareAppender(TraceAppender nonLoadTestTraceAppender, TraceAppender loadTestTraceAppender) {
        this.nonLoadTestTraceAppender = nonLoadTestTraceAppender;
        this.loadTestTraceAppender = loadTestTraceAppender;
    }

    public static LoadTestAwareAppender createLoadTestAwareTimedRollingFileAppender(String logName, boolean append) {
        return new LoadTestAwareAppender(new TimedRollingFileAppender(logName, append), new TimedRollingFileAppender("shadow/" + logName, append));
    }

    public static LoadTestAwareAppender createLoadTestAwareTimedRollingFileAppender(String logName, String rollingPolicy, String logReserveConfig) {
        return new LoadTestAwareAppender(new TimedRollingFileAppender(logName, rollingPolicy, logReserveConfig), new TimedRollingFileAppender("shadow/" + logName, rollingPolicy, logReserveConfig));
    }

    public void append(String log, boolean loadTest) throws IOException {
        if (loadTest) {
            loadTestTraceAppender.append(log);
        } else {
            nonLoadTestTraceAppender.append(log);
        }
    }

    @Override
    public void flush() throws IOException {
        nonLoadTestTraceAppender.flush();
        loadTestTraceAppender.flush();
    }

    @Override
    public void append(String log) throws IOException {
        append(log, true);
    }

    @Override
    public void cleanup() {
        nonLoadTestTraceAppender.cleanup();
        loadTestTraceAppender.cleanup();
    }
}