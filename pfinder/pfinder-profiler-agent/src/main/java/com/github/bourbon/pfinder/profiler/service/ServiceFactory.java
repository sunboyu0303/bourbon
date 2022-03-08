package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.load.Addon;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:43
 */
public interface ServiceFactory {

    Addon addon();

    Class<?> type();

    Object get();
}