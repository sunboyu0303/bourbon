package com.github.bourbon.base.disruptor.processor;

import com.github.bourbon.base.disruptor.lang.Sequence;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:43
 */
public interface EventProcessor extends Runnable {

    Sequence getSequence();

    void halt();

    boolean isRunning();
}