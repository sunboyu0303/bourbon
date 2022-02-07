package com.github.bourbon.tracer.core.span;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 08:46
 */
public class CommonLogSpan extends SofaTracerSpan {

    private final List<String> slots = ListUtils.newArrayList();

    private final AtomicInteger slotCounter = new AtomicInteger(0);

    public CommonLogSpan(SofaTracer sofaTracer, long startTime, String operationName, SofaTracerSpanContext sofaTracerSpanContext, Map<String, ?> tags) {
        this(sofaTracer, startTime, null, operationName, sofaTracerSpanContext, tags);
    }

    public CommonLogSpan(SofaTracer sofaTracer, long startTime, List<SofaTracerSpanReferenceRelationship> spanReferences, String operationName, SofaTracerSpanContext sofaTracerSpanContext, Map<String, ?> tags) {
        super(sofaTracer, startTime, spanReferences, operationName, sofaTracerSpanContext, tags);
    }

    public CommonLogSpan addSlot(String slot) {
        if (slot == null) {
            slot = StringConstants.EMPTY;
        }
        if (slotCounter.incrementAndGet() <= 32) {
            slots.add(slot);
        } else {
            SelfLog.warn("Slots count（32）Fully");
        }
        return this;
    }

    public List<String> getSlots() {
        return slots;
    }

    public CommonLogSpan addSlots(List<String> stringArrayList) {
        if (CollectionUtils.isNotEmpty(stringArrayList)) {
            stringArrayList.forEach(this::addSlot);
        }
        return this;
    }
}