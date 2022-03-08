package com.github.bourbon.pfinder.profiler.common.util;

import com.github.bourbon.pfinder.profiler.common.exception.PfinderEncodeException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:45
 */
public interface ChunkEncoder<T> {

    void writeChunk(T t, BytesEncodingWriter writer) throws PfinderEncodeException;
}