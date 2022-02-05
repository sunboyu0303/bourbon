package com.github.bourbon.tracer.core.appender.manager;

import com.github.bourbon.base.disruptor.EventFactory;
import com.github.bourbon.base.lang.VolatileObject;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 13:17
 */
public class SofaTracerSpanEventFactory implements EventFactory<VolatileObject<SofaTracerSpan>> {

    @Override
    public VolatileObject<SofaTracerSpan> newInstance() {
        return new VolatileObject<>();
    }
}