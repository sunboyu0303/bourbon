package com.github.bourbon.tracer.core.appender.manager;

import com.github.bourbon.base.disruptor.EventFactory;
import com.github.bourbon.base.lang.VolatileObject;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 13:08
 */
public class StringEventFactory implements EventFactory<VolatileObject<String>> {

    @Override
    public VolatileObject<String> newInstance() {
        return new VolatileObject<>();
    }
}