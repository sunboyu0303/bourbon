package com.github.bourbon.tracer.core.appender;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 13:44
 */
public interface TraceAppender {

    void flush() throws IOException;

    void append(String log) throws IOException;

    void cleanup();
}