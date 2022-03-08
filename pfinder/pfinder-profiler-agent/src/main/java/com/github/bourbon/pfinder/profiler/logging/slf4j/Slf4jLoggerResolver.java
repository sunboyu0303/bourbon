package com.github.bourbon.pfinder.profiler.logging.slf4j;

import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerResolver;
import com.github.bourbon.pfinder.profiler.logging.basic.PFinderLoggerConfig;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 00:05
 */
public class Slf4jLoggerResolver implements LoggerResolver {
    private final PFinderLoggerConfig config;
    private final LoggerResolver systemOutLoggerResolver;

    public Slf4jLoggerResolver(PFinderLoggerConfig config, LoggerResolver systemOutLoggerResolver) {
        this.config = config;
        this.systemOutLoggerResolver = systemOutLoggerResolver;
    }

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Slf4jLoggerProxy(clazz, this.config, this.systemOutLoggerResolver.getLogger(clazz));
    }
}