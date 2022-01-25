package com.github.bourbon.base.appender;

import com.github.bourbon.base.lang.clean.Cleanable;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 13:44
 */
public interface TraceAppender extends Cleanable {

    void flush() throws IOException;

    void append(String log) throws IOException;
}