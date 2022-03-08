package com.github.bourbon.base.disruptor.handler;

import com.github.bourbon.base.disruptor.lang.Sequence;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 16:36
 */
public interface SequenceReportingEventHandler<T> extends EventHandler<T> {

    void setSequenceCallback(Sequence sequenceCallback);
}