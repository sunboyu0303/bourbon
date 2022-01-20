package com.github.bourbon.uuid.core.support;

import com.github.bourbon.uuid.core.lang.RingBuffer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 16:33
 */
@FunctionalInterface
public interface RingBufferRejectedPutHandler {

    void reject(RingBuffer r, long u);
}