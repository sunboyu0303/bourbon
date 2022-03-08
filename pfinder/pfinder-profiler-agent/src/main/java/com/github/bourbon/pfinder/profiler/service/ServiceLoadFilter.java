package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.load.Addon;
import com.github.bourbon.pfinder.profiler.service.tribe.annotation.SimpleTribe;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:54
 */
@SimpleTribe
public interface ServiceLoadFilter {

    boolean filterServiceAddon(Addon addon);
}