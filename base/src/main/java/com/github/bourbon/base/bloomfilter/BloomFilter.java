package com.github.bourbon.base.bloomfilter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:48
 */
public interface BloomFilter {

    boolean contains(String key);

    boolean add(String key);
}