package com.github.bourbon.springframework.cache.multi.annotation;

import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.springframework.cache.core.annotation.CacheAspect;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 18:02
 */
public interface MultiCacheAspect extends CacheAspect {

    CacheAdapter cacheAdapter = loader.getExtension("multi");
}