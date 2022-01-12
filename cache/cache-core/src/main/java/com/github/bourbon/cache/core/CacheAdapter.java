package com.github.bourbon.cache.core;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 18:31
 */
@SPI(scope = ExtensionScope.FRAMEWORK)
public interface CacheAdapter {

    <K, V> Cache<K, V> getCache(CacheParamInfo<K, V> info);
}