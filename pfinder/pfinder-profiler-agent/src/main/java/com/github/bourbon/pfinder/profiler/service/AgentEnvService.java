package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.service.tribe.annotation.SingletonTribe;

import java.lang.instrument.Instrumentation;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 17:08
 */
@SingletonTribe
public interface AgentEnvService {

    String agentArgs();

    Instrumentation instrumentation();
}