package com.github.bourbon.pfinder.profiler.common.util;

import com.github.bourbon.pfinder.profiler.common.exception.PfinderEncodeException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:50
 */
public interface FieldEncoder<T> {
    
    byte[] encode(T t) throws PfinderEncodeException;
}