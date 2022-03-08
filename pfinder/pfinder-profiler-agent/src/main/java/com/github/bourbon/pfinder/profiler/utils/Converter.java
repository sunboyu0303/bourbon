package com.github.bourbon.pfinder.profiler.utils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 23:07
 */
public interface Converter<T, R> {
    
    R convert(T t);
}