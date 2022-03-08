package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.lang.Sequence;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:41
 */
public interface Sequencer extends Cursored, Sequenced {

    long INITIAL_CURSOR_VALUE = -1L;

    void claim(long sequence);

    boolean isAvailable(long sequence);

    void addGatingSequences(Sequence... gatingSequences);

    boolean removeGatingSequence(Sequence sequence);

    SequenceBarrier newBarrier(Sequence... sequencesToTrack);

    long getMinimumSequence();

    long getHighestPublishedSequence(long nextSequence, long availableSequence);

    <T> EventPoller<T> newPoller(DataProvider<T> provider, Sequence... gatingSequences);
}