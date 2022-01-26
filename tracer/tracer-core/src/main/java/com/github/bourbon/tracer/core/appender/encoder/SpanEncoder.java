package com.github.bourbon.tracer.core.appender.encoder;

import io.opentracing.Span;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/26 10:39
 */
public interface SpanEncoder<T extends Span> {

    String encode(T span) throws IOException;
}