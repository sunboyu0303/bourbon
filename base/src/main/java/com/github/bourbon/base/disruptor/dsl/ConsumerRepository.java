package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.WorkerPool;
import com.github.bourbon.base.disruptor.handler.EventHandler;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.processor.EventProcessor;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:32
 */
class ConsumerRepository<T> implements Iterable<ConsumerInfo> {

    private final Map<EventHandler<?>, EventProcessorInfo<T>> eventProcessorInfoByEventHandler = new IdentityHashMap<>();
    private final Map<Sequence, ConsumerInfo> eventProcessorInfoBySequence = new IdentityHashMap<>();
    private final Collection<ConsumerInfo> consumerInfos = new ArrayList<>();

    public void add(EventProcessor eventprocessor, EventHandler<? super T> handler, SequenceBarrier barrier) {
        EventProcessorInfo<T> consumerInfo = new EventProcessorInfo<>(eventprocessor, handler, barrier);
        eventProcessorInfoByEventHandler.put(handler, consumerInfo);
        eventProcessorInfoBySequence.put(eventprocessor.getSequence(), consumerInfo);
        consumerInfos.add(consumerInfo);
    }

    public void add(EventProcessor processor) {
        EventProcessorInfo<T> consumerInfo = new EventProcessorInfo<>(processor, null, null);
        eventProcessorInfoBySequence.put(processor.getSequence(), consumerInfo);
        consumerInfos.add(consumerInfo);
    }

    public void add(WorkerPool<T> workerPool, SequenceBarrier sequenceBarrier) {
        WorkerPoolInfo<T> workerPoolInfo = new WorkerPoolInfo<>(workerPool, sequenceBarrier);
        consumerInfos.add(workerPoolInfo);
        for (Sequence sequence : workerPool.getWorkerSequences()) {
            eventProcessorInfoBySequence.put(sequence, workerPoolInfo);
        }
    }

    public Sequence[] getLastSequenceInChain(boolean includeStopped) {
        List<Sequence> lastSequence = ListUtils.newArrayList();
        for (ConsumerInfo consumerInfo : consumerInfos) {
            if ((includeStopped || consumerInfo.isRunning()) && consumerInfo.isEndOfChain()) {
                Collections.addAll(lastSequence, consumerInfo.getSequences());
            }
        }
        return lastSequence.toArray(new Sequence[0]);
    }

    public EventProcessor getEventProcessorFor(EventHandler<T> handler) {
        EventProcessorInfo<T> eventprocessorInfo = getEventProcessorInfo(handler);
        if (eventprocessorInfo == null) {
            throw new IllegalArgumentException("The event handler " + handler + " is not processing events.");
        }
        return eventprocessorInfo.getEventProcessor();
    }

    public Sequence getSequenceFor(EventHandler<T> handler) {
        return getEventProcessorFor(handler).getSequence();
    }

    public void unMarkEventProcessorsAsEndOfChain(Sequence... barrierEventProcessors) {
        for (Sequence barrierEventProcessor : barrierEventProcessors) {
            getEventProcessorInfo(barrierEventProcessor).markAsUsedInBarrier();
        }
    }

    @Override
    public Iterator<ConsumerInfo> iterator() {
        return consumerInfos.iterator();
    }

    public SequenceBarrier getBarrierFor(final EventHandler<T> handler) {
        return ObjectUtils.defaultIfNullElseFunction(getEventProcessorInfo(handler), EventProcessorInfo::getBarrier);
    }

    private EventProcessorInfo<T> getEventProcessorInfo(EventHandler<T> handler) {
        return eventProcessorInfoByEventHandler.get(handler);
    }

    private ConsumerInfo getEventProcessorInfo(Sequence barrierEventProcessor) {
        return eventProcessorInfoBySequence.get(barrierEventProcessor);
    }
}