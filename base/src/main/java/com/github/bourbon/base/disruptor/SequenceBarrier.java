package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.exception.TimeoutException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:34
 */
public interface SequenceBarrier {

    long waitFor(long sequence) throws AlertException, InterruptedException, TimeoutException;

    long getCursor();

    boolean isAlerted();

    void alert();

    void clearAlert();

    void checkAlert() throws AlertException;
}