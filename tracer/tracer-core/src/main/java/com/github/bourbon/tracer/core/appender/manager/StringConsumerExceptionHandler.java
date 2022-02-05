package com.github.bourbon.tracer.core.appender.manager;

import com.github.bourbon.base.disruptor.handler.ExceptionHandler;
import com.github.bourbon.base.lang.VolatileObject;
import com.github.bourbon.tracer.core.appender.self.SynchronizingSelfLog;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 13:13
 */
public class StringConsumerExceptionHandler implements ExceptionHandler<VolatileObject<String>> {

    @Override
    public void handleEventException(Throwable ex, long sequence, VolatileObject<String> event) {
        if (event != null) {
            SynchronizingSelfLog.error("AsyncConsumer occurs exception during handle VolatileObject, The String is [" + event.getValue() + "].", ex);
        } else {
            SynchronizingSelfLog.error("AsyncConsumer occurs exception during handle VolatileObject, The String is null.", ex);
        }
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        SynchronizingSelfLog.error("AsyncConsumer occurs exception on start.", ex);

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        SynchronizingSelfLog.error("Disruptor or AsyncConsumer occurs exception on shutdown.", ex);
    }
}