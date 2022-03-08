package com.github.bourbon.uuid.segment.buffer.support;

import com.github.bourbon.uuid.segment.buffer.SegmentBufferService;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 14:12
 */
public class RedisSegmentBufferService implements SegmentBufferService {

    @Override
    public long getMaxId(long step) {
        return 0;
    }
}