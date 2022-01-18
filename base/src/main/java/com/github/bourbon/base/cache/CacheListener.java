package com.github.bourbon.base.cache;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 17:57
 */
@FunctionalInterface
public interface CacheListener<K, V> {

    void onRemove(K k, V v);
}