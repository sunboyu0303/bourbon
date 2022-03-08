package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.service.tribe.annotation.SingletonTribe;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 23:24
 */
@SingletonTribe
public interface SystemEnvService {
    
    String getenv(String var1);

    Map<String, String> getenv();
}