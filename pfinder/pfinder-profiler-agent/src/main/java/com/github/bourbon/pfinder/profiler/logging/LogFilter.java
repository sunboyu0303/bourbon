package com.github.bourbon.pfinder.profiler.logging;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:55
 */
public interface LogFilter {

    List<LogFilter> FILTERS = new CopyOnWriteArrayList<>();

    boolean filter(String var1, LogLevel var2, String var3, Throwable var4);
}