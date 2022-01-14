package com.github.bourbon.springframework.cache.ehcache.annotation;

import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.springframework.cache.core.annotation.CacheAspect;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 15:20
 */
public interface EhcacheCacheAspect extends CacheAspect {

    CacheAdapter cacheAdapter = loader.getExtension("ehcache");
}