package com.github.bourbon.base.appender.file;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.base.utils.MapUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 17:10
 */
public class CompositeTraceAppender implements TraceAppender {

    private Map<String, TraceAppender> traceAppenderMap = MapUtils.newConcurrentHashMap();

    public TraceAppender getAppender(String logName) {
        return traceAppenderMap.get(logName);
    }

    public void putAppender(String logName, TraceAppender traceAppender) {
        traceAppenderMap.put(logName, traceAppender);
    }

    @Override
    public void flush() throws IOException {
        for (TraceAppender traceAppender : traceAppenderMap.values()) {
            traceAppender.flush();
        }
    }

    @Override
    public void append(String log) throws IOException {
        for (TraceAppender traceAppender : traceAppenderMap.values()) {
            traceAppender.append(log);
        }
    }

    @Override
    public void cleanup() {
        for (TraceAppender traceAppender : traceAppenderMap.values()) {
            traceAppender.cleanup();
        }
    }
}