package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.service.tribe.annotation.OrderedTribe;

import java.util.Comparator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:05
 */
@OrderedTribe(ActivityService.ActivePriorityComparator.class)
public interface ActivityService {

    int PRIORITY_WITH_NO_DEPEND = -700;
    int PRIORITY_SYSTEM_LEVEL_SERVICE = -600;
    int PRIORITY_DEPEND_SYSTEM_LEVEL_SERVICE = -590;
    int PRIORITY_LOCAL_CONFIG = -500;
    int PRIORITY_BASIC_SERVICE_BY_LOCAL_CONFIG = -490;
    int PRIORITY_REMOTE_SERVICE_DISCOVER = -400;
    int PRIORITY_AGENT_REGISTRAR = -300;
    int PRIORITY_REMOTE_CONFIG = -200;
    int PRIORITY_BASIC_SERVICE_BY_REMOTE_CONFIG = -100;
    int PRIORITY_NATIVE_SERVICE = 0;
    int PRIORITY_BASIC_SERVICE_BY_NATIVE_SERVICE = 50;
    int PRIORITY_REGISTRAR = 100;
    int PRIORITY_REPORTER = 150;
    int PRIORITY_INTERCEPTOR_LOADER = 200;
    int PRIORITY_INTERCEPTOR = 250;

    void initialize(ProfilerContext var1);

    void shutdown();

    boolean isReady();

    int activeOrder();

    class ActivePriorityComparator implements Comparator<ActivityService> {
        @Override
        public int compare(ActivityService o1, ActivityService o2) {
            return o1.activeOrder() - o2.activeOrder();
        }
    }
}