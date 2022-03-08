package com.github.bourbon.pfinder.profiler.common.util;

import com.github.bourbon.pfinder.profiler.common.exception.PfinderDecodeException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:49
 */
public interface FieldDecoder<T> {

    T decode(byte[] bytes) throws PfinderDecodeException;
}