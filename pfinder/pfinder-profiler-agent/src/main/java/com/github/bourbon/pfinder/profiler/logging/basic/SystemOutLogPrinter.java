package com.github.bourbon.pfinder.profiler.logging.basic;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:57
 */
public class SystemOutLogPrinter implements LogPrinter {

    @Override
    public void print(String logMessage) {
        System.out.println(logMessage);
    }
}