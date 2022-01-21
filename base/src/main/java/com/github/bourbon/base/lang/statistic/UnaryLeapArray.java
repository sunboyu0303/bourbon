package com.github.bourbon.base.lang.statistic;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 16:53
 */
public class UnaryLeapArray extends LeapArray<LongAdder> {

    public UnaryLeapArray(int sampleCount, long intervalInMs) {
        super(sampleCount, intervalInMs);
    }

    @Override
    public LongAdder newEmptyBucket(long timeMillis) {
        return new LongAdder();
    }

    @Override
    protected WindowWrap<LongAdder> resetWindowTo(WindowWrap<LongAdder> windowWrap, long startTime) {
        windowWrap.resetTo(startTime).get().reset();
        return windowWrap;
    }
}