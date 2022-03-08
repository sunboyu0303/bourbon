package com.github.bourbon.base.disruptor.lang;

import com.github.bourbon.base.disruptor.AbstractSequencer;
import com.github.bourbon.base.disruptor.strategy.WaitStrategy;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 16:38
 */
abstract class SingleProducerSequencerPad extends AbstractSequencer {
    protected long p1, p2, p3, p4, p5, p6, p7;

    SingleProducerSequencerPad(int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }
}