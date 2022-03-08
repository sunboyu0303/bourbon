package com.github.bourbon.base.disruptor.handler;

import com.github.bourbon.base.extension.Lifecycle;
import com.github.bourbon.base.utils.SetUtils;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:51
 */
public final class AggregateEventHandler<T> implements EventHandler<T>, Lifecycle {

    private final Set<EventHandler<T>> eventHandlers;

    public AggregateEventHandler(EventHandler<T>... eventHandlers) {
        this.eventHandlers = SetUtils.newLinkedHashSet(eventHandlers);
    }

    @Override
    public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        for (EventHandler<T> eventHandler : eventHandlers) {
            eventHandler.onEvent(event, sequence, endOfBatch);
        }
    }

    @Override
    public void initialize() throws Exception {
        for (EventHandler<T> eventHandler : eventHandlers) {
            if (eventHandler instanceof Lifecycle) {
                ((Lifecycle) eventHandler).initialize();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        for (EventHandler<T> eventHandler : eventHandlers) {
            if (eventHandler instanceof Lifecycle) {
                ((Lifecycle) eventHandler).destroy();
            }
        }
    }
}