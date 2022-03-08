package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.lang.RingBuffer;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.processor.EventProcessor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:41
 */
public interface EventProcessorFactory<T> {

    EventProcessor createEventProcessor(RingBuffer<T> ringBuffer, Sequence[] barrierSequences);
}