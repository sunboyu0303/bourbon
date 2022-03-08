package com.github.bourbon.pfinder.profiler.logging.basic;

import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerResolver;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:28
 */
public class PFinderLoggerResolver implements LoggerResolver {

    private final LogPrinter logPrinter;
    private final PFinderLoggerConfig config;

    public PFinderLoggerResolver(LogPrinter logPrinter, PFinderLoggerConfig config) {
        this.logPrinter = logPrinter;
        this.config = config;
    }

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new PFinderLogger(clazz, this.logPrinter, this.config);
    }
}