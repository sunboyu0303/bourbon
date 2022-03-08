package com.github.bourbon.pfinder.profiler.service;

import java.util.Collection;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 14:57
 */
public interface ProfilerContext {
    
    <T> T getService(Class<T> var1);

    <T> Collection<T> getAllService(Class<T> var1);

    boolean isRunning();
}