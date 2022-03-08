package com.github.bourbon.pfinder.profiler.logging;

import com.github.bourbon.pfinder.profiler.logging.basic.PFinderLoggerResolver;
import com.github.bourbon.pfinder.profiler.logging.basic.SystemOutLogPrinter;
import com.github.bourbon.pfinder.profiler.logging.slf4j.Slf4jLoggerResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:12
 */
public class LoggerFactory {
    private static final List<LoggerResolver> RESOLVER = new ArrayList<>();
    private static final SimpleConfig CONFIG = new SimpleConfig();

    public static void updateLogLevel(LogLevel logLevel) {
        CONFIG.setLogLevel(logLevel);
    }

    public static Logger getLogger(Class<?> clazz) {
        for (LoggerResolver resolver : RESOLVER) {
            try {
                return resolver.getLogger(clazz);
            } catch (Exception e) {
                System.out.println("get Logger by resolver error. resolver=" + resolver + " class=" + clazz.getName() + " error=" + e.getMessage());
            }
        }
        throw new RuntimeException("can't get any Logger.");
    }

    static {
        PFinderLoggerResolver systemOutLoggerResolver = new PFinderLoggerResolver(new SystemOutLogPrinter(), CONFIG);
        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            RESOLVER.add(new Slf4jLoggerResolver(CONFIG, systemOutLoggerResolver));
        } catch (ClassNotFoundException e) {
            // ignore
        }
        RESOLVER.add(systemOutLoggerResolver);
    }
}