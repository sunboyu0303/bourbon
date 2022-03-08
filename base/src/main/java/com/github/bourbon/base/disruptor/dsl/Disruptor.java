package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.EventFactory;
import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.WorkerPool;
import com.github.bourbon.base.disruptor.exception.TimeoutException;
import com.github.bourbon.base.disruptor.handler.EventHandler;
import com.github.bourbon.base.disruptor.handler.ExceptionHandler;
import com.github.bourbon.base.disruptor.handler.WorkHandler;
import com.github.bourbon.base.disruptor.lang.RingBuffer;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.processor.BatchEventProcessor;
import com.github.bourbon.base.disruptor.processor.EventProcessor;
import com.github.bourbon.base.disruptor.strategy.WaitStrategy;
import com.github.bourbon.base.disruptor.translator.EventTranslator;
import com.github.bourbon.base.disruptor.translator.EventTranslatorOneArg;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.Utils;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:24
 */
public class Disruptor<T> {
    private final RingBuffer<T> ringBuffer;
    private final Executor executor;
    private final ConsumerRepository<T> consumerRepository = new ConsumerRepository<>();
    private final AtomicBoolean started = new AtomicBoolean(false);
    private ExceptionHandler<? super T> exceptionHandler = new ExceptionHandlerWrapper<>();

    public Disruptor(EventFactory<T> eventFactory, int ringBufferSize, Executor executor) {
        this(RingBuffer.createMultiProducer(eventFactory, ringBufferSize), executor);
    }

    public Disruptor(EventFactory<T> eventFactory, int ringBufferSize, Executor executor, ProducerType producerType, WaitStrategy waitStrategy) {
        this(RingBuffer.create(producerType, eventFactory, ringBufferSize, waitStrategy), executor);
    }

    public Disruptor(EventFactory<T> eventFactory, int ringBufferSize, ThreadFactory threadFactory) {
        this(RingBuffer.createMultiProducer(eventFactory, ringBufferSize), new BasicExecutor(threadFactory));
    }

    public Disruptor(EventFactory<T> eventFactory, int ringBufferSize, ThreadFactory threadFactory, ProducerType producerType, WaitStrategy waitStrategy) {
        this(RingBuffer.create(producerType, eventFactory, ringBufferSize, waitStrategy), new BasicExecutor(threadFactory));
    }

    private Disruptor(RingBuffer<T> ringBuffer, Executor executor) {
        this.ringBuffer = ringBuffer;
        this.executor = executor;
    }

    @SuppressWarnings("varargs")
    public EventHandlerGroup<T> handleEventsWith(EventHandler<? super T>... handlers) {
        return createEventProcessors(new Sequence[0], handlers);
    }

    public EventHandlerGroup<T> handleEventsWith(EventProcessorFactory<T>... eventProcessorFactories) {
        return createEventProcessors(new Sequence[0], eventProcessorFactories);
    }

    public EventHandlerGroup<T> handleEventsWith(EventProcessor... processors) {
        Arrays.stream(processors).forEach(consumerRepository::add);
        int len = processors.length;
        Sequence[] sequences = new Sequence[len];
        for (int i = 0; i < len; i++) {
            sequences[i] = processors[i].getSequence();
        }
        ringBuffer.addGatingSequences(sequences);
        return new EventHandlerGroup<>(this, consumerRepository, Utils.getSequencesFor(processors));
    }

    @SuppressWarnings("varargs")
    public EventHandlerGroup<T> handleEventsWithWorkerPool(WorkHandler<T>... workHandlers) {
        return createWorkerPool(new Sequence[0], workHandlers);
    }

    public void handleExceptionsWith(ExceptionHandler<? super T> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @SuppressWarnings("unchecked")
    public void setDefaultExceptionHandler(final ExceptionHandler<? super T> exceptionHandler) {
        checkNotStarted();
        if (!(this.exceptionHandler instanceof ExceptionHandlerWrapper)) {
            throw new IllegalStateException("setDefaultExceptionHandler can not be used after handleExceptionsWith");
        }
        ((ExceptionHandlerWrapper<T>) this.exceptionHandler).switchTo(exceptionHandler);
    }

    public ExceptionHandlerSetting<T> handleExceptionsFor(EventHandler<T> eventHandler) {
        return new ExceptionHandlerSetting<>(eventHandler, consumerRepository);
    }

    @SuppressWarnings("varargs")
    public EventHandlerGroup<T> after(EventHandler<T>... handlers) {
        int handlersLength = handlers.length;
        Sequence[] sequences = new Sequence[handlersLength];
        for (int i = 0; i < handlersLength; i++) {
            sequences[i] = consumerRepository.getSequenceFor(handlers[i]);
        }
        return new EventHandlerGroup<>(this, consumerRepository, sequences);
    }

    public EventHandlerGroup<T> after(EventProcessor... processors) {
        Arrays.stream(processors).forEach(consumerRepository::add);
        return new EventHandlerGroup<>(this, consumerRepository, Utils.getSequencesFor(processors));
    }

    public void publishEvent(EventTranslator<T> eventTranslator) {
        ringBuffer.publishEvent(eventTranslator);
    }

    public <A> void publishEvent(EventTranslatorOneArg<T, A> eventTranslator, A arg) {
        ringBuffer.publishEvent(eventTranslator, arg);
    }

    public <A> void publishEvents(EventTranslatorOneArg<T, A> eventTranslator, A[] arg) {
        ringBuffer.publishEvents(eventTranslator, arg);
    }

    public RingBuffer<T> start() {
        checkOnlyStartedOnce();
        consumerRepository.forEach(info -> info.start(executor));
        return ringBuffer;
    }

    public void halt() {
        consumerRepository.forEach(ConsumerInfo::halt);
    }

    public void shutdown() {
        try {
            shutdown(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            exceptionHandler.handleOnShutdownException(e);
        }
    }

    public void shutdown(long timeout, TimeUnit timeUnit) throws TimeoutException {
        long timeOutAt = Clock.currentTimeMillis() + timeUnit.toMillis(timeout);
        while (hasBacklog()) {
            if (timeout >= 0 && Clock.currentTimeMillis() > timeOutAt) {
                throw TimeoutException.INSTANCE;
            }
        }
        halt();
    }

    public RingBuffer<T> getRingBuffer() {
        return ringBuffer;
    }

    public long getCursor() {
        return ringBuffer.getCursor();
    }

    public long getBufferSize() {
        return ringBuffer.getBufferSize();
    }

    public T get(long sequence) {
        return ringBuffer.get(sequence);
    }

    public SequenceBarrier getBarrierFor(EventHandler<T> handler) {
        return consumerRepository.getBarrierFor(handler);
    }

    public long getSequenceValueFor(EventHandler<T> b1) {
        return consumerRepository.getSequenceFor(b1).get();
    }

    private boolean hasBacklog() {
        long cursor = ringBuffer.getCursor();
        for (Sequence consumer : consumerRepository.getLastSequenceInChain(false)) {
            if (cursor > consumer.get()) {
                return true;
            }
        }
        return false;
    }

    EventHandlerGroup<T> createEventProcessors(Sequence[] barrierSequences, EventHandler<? super T>[] eventHandlers) {
        checkNotStarted();

        Sequence[] processorSequences = new Sequence[eventHandlers.length];
        SequenceBarrier barrier = ringBuffer.newBarrier(barrierSequences);

        for (int i = 0, eventHandlersLength = eventHandlers.length; i < eventHandlersLength; i++) {
            EventHandler<? super T> eventHandler = eventHandlers[i];
            BatchEventProcessor<T> batchEventProcessor = new BatchEventProcessor<>(ringBuffer, barrier, eventHandler);
            ObjectUtils.nonNullConsumer(exceptionHandler, batchEventProcessor::setExceptionHandler);
            consumerRepository.add(batchEventProcessor, eventHandler, barrier);
            processorSequences[i] = batchEventProcessor.getSequence();
        }

        updateGatingSequencesForNextInChain(barrierSequences, processorSequences);

        return new EventHandlerGroup<>(this, consumerRepository, processorSequences);
    }

    private void updateGatingSequencesForNextInChain(Sequence[] barrierSequences, Sequence[] processorSequences) {
        if (processorSequences.length > 0) {
            ringBuffer.addGatingSequences(processorSequences);
            Arrays.stream(barrierSequences).forEach(ringBuffer::removeGatingSequence);
            consumerRepository.unMarkEventProcessorsAsEndOfChain(barrierSequences);
        }
    }

    EventHandlerGroup<T> createEventProcessors(Sequence[] barrierSequences, EventProcessorFactory<T>[] processorFactories) {
        int len = processorFactories.length;
        EventProcessor[] eventProcessors = new EventProcessor[len];
        for (int i = 0; i < len; i++) {
            eventProcessors[i] = processorFactories[i].createEventProcessor(ringBuffer, barrierSequences);
        }
        return handleEventsWith(eventProcessors);
    }

    EventHandlerGroup<T> createWorkerPool(Sequence[] barrierSequences, WorkHandler<? super T>[] workHandlers) {
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier(barrierSequences);
        WorkerPool<T> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, exceptionHandler, workHandlers);
        consumerRepository.add(workerPool, sequenceBarrier);
        Sequence[] workerSequences = workerPool.getWorkerSequences();
        updateGatingSequencesForNextInChain(barrierSequences, workerSequences);
        return new EventHandlerGroup<>(this, consumerRepository, workerSequences);
    }

    private void checkNotStarted() {
        if (started.get()) {
            throw new IllegalStateException("All event handlers must be added before calling starts.");
        }
    }

    private void checkOnlyStartedOnce() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("Disruptor.start() must only be called once.");
        }
    }

    @Override
    public String toString() {
        return "Disruptor{ringBuffer=" + ringBuffer + ", started=" + started + ", executor=" + executor + '}';
    }
}