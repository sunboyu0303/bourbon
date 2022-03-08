package com.github.bourbon.pfinder.profiler.sdk;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
@FunctionalInterface
public interface PfinderProcessor {

    void process(PfinderContext context);
}