package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.common.protocol.ID;
import com.github.bourbon.pfinder.profiler.service.tribe.annotation.SingletonTribe;
import com.github.bourbon.pfinder.profiler.utils.IntIdHolder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 17:17
 */
@SingletonTribe
public interface IdGenerateService extends ActivityService {

    ID traceId();

    ID traceNodeId();

    int instanceId();

    int applicationId();

    IntIdHolder operationId(String var1);
}