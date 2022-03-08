package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.handler.EventHandler;
import com.github.bourbon.base.disruptor.handler.WorkHandler;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.processor.EventProcessor;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:38
 */
public class EventHandlerGroup<T> {
    private final Disruptor<T> disruptor;
    private final ConsumerRepository<T> consumerRepository;
    private final Sequence[] sequences;

    EventHandlerGroup(Disruptor<T> disruptor, ConsumerRepository<T> consumerRepository, Sequence[] sequences) {
        this.disruptor = disruptor;
        this.consumerRepository = consumerRepository;
        this.sequences = Arrays.copyOf(sequences, sequences.length);
    }

    public EventHandlerGroup<T> and(EventHandlerGroup<T> otherHandlerGroup) {
        Sequence[] combinedSequences = new Sequence[this.sequences.length + otherHandlerGroup.sequences.length];
        System.arraycopy(this.sequences, 0, combinedSequences, 0, this.sequences.length);
        System.arraycopy(otherHandlerGroup.sequences, 0, combinedSequences, this.sequences.length, otherHandlerGroup.sequences.length);
        return new EventHandlerGroup<>(disruptor, consumerRepository, combinedSequences);
    }

    public EventHandlerGroup<T> and(EventProcessor... processors) {
        Sequence[] combinedSequences = new Sequence[sequences.length + processors.length];
        for (int i = 0; i < processors.length; i++) {
            consumerRepository.add(processors[i]);
            combinedSequences[i] = processors[i].getSequence();
        }
        System.arraycopy(sequences, 0, combinedSequences, processors.length, sequences.length);
        return new EventHandlerGroup<>(disruptor, consumerRepository, combinedSequences);
    }

    public EventHandlerGroup<T> then(EventHandler<? super T>... handlers) {
        return handleEventsWith(handlers);
    }

    public EventHandlerGroup<T> then(EventProcessorFactory<T>... eventProcessorFactories) {
        return handleEventsWith(eventProcessorFactories);
    }

    public EventHandlerGroup<T> thenHandleEventsWithWorkerPool(WorkHandler<? super T>... handlers) {
        return handleEventsWithWorkerPool(handlers);
    }

    public EventHandlerGroup<T> handleEventsWith(EventHandler<? super T>... handlers) {
        return disruptor.createEventProcessors(sequences, handlers);
    }

    public EventHandlerGroup<T> handleEventsWith(EventProcessorFactory<T>... eventProcessorFactories) {
        return disruptor.createEventProcessors(sequences, eventProcessorFactories);
    }

    public EventHandlerGroup<T> handleEventsWithWorkerPool(WorkHandler<? super T>... handlers) {
        return disruptor.createWorkerPool(sequences, handlers);
    }

    public SequenceBarrier asSequenceBarrier() {
        return disruptor.getRingBuffer().newBarrier(sequences);
    }
}