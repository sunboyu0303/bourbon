package com.github.bourbon.pfinder.profiler.common.util;

import com.github.bourbon.pfinder.profiler.common.exception.PfinderDecodeException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:44
 */
public interface ChunkDecoder<T> {

    T readChunk(BytesDecodeReader reader) throws PfinderDecodeException;
}