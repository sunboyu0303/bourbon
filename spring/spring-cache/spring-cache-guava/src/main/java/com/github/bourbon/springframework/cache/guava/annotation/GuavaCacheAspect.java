package com.github.bourbon.springframework.cache.guava.annotation;

import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.springframework.cache.core.annotation.CacheAspect;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 15:31
 */
public interface GuavaCacheAspect extends CacheAspect {

    CacheAdapter cacheAdapter = loader.getExtension("guava");
}