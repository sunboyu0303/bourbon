package com.github.bourbon.pfinder.profiler.service;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:42
 */
public interface PFinderServiceLoader {
    
    Iterable<ServiceFactory> load();
}