package com.github.bourbon.uuid.core.support;

import com.github.bourbon.uuid.core.lang.RingBuffer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 16:32
 */
@FunctionalInterface
public interface RingBufferRejectedTakeHandler {

    void reject(RingBuffer r) throws Exception;
}