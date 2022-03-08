package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.handler.EventHandler;
import com.github.bourbon.base.disruptor.handler.ExceptionHandler;
import com.github.bourbon.base.disruptor.processor.BatchEventProcessor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:46
 */
public class ExceptionHandlerSetting<T> {
    private final EventHandler<T> eventHandler;
    private final ConsumerRepository<T> consumerRepository;

    ExceptionHandlerSetting(EventHandler<T> eventHandler, ConsumerRepository<T> consumerRepository) {
        this.eventHandler = eventHandler;
        this.consumerRepository = consumerRepository;
    }

    @SuppressWarnings("unchecked")
    public void with(ExceptionHandler<? super T> exceptionHandler) {
        ((BatchEventProcessor<T>) consumerRepository.getEventProcessorFor(eventHandler)).setExceptionHandler(exceptionHandler);
        consumerRepository.getBarrierFor(eventHandler).alert();
    }
}