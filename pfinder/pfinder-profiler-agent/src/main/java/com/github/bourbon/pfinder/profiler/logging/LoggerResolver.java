package com.github.bourbon.pfinder.profiler.logging;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:18
 */
public interface LoggerResolver {

    Logger getLogger(Class<?> var1);
}