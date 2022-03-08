package com.github.bourbon.base.disruptor.lang;

import com.github.bourbon.base.disruptor.strategy.WaitStrategy;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 16:39
 */
abstract class SingleProducerSequencerFields extends SingleProducerSequencerPad {

    protected long nextValue = Sequence.INITIAL_VALUE;
    protected long cachedValue = Sequence.INITIAL_VALUE;

    SingleProducerSequencerFields(int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }
}