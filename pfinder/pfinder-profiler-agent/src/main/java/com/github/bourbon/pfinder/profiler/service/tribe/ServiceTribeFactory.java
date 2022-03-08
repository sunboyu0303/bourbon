package com.github.bourbon.pfinder.profiler.service.tribe;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 19:41
 */
public interface ServiceTribeFactory {

    ServiceTribe build(Class<?> type);
}