package com.github.bourbon.tracer.core.tags;

import com.github.bourbon.base.code.LogCode2Description;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.constants.ComponentNameConstants;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;
import io.opentracing.tag.StringTag;

import java.util.function.Consumer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 11:27
 */
public final class SpanTags {

    public static final StringTag CURR_APP_TAG = new StringTag("curr.app");

    public static void putTags(String key, String val) {
        putTags(s -> s.setTag(key, val));
    }

    public static void putTags(String key, boolean val) {
        putTags(s -> s.setTag(key, val));
    }

    public static void putTags(String key, Number val) {
        putTags(s -> s.setTag(key, val));
    }

    private static void putTags(Consumer<SofaTracerSpan> consumer) {
        SofaTracerSpan currentSpan = SofaTraceContextHolder.getSofaTraceContext().getCurrentSpan();
        if (checkTags(currentSpan)) {
            consumer.accept(currentSpan);
        }
    }

    private static boolean checkTags(SofaTracerSpan currentSpan) {
        if (currentSpan == null) {
            SelfLog.error(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00013"));
            return false;
        }
        String componentType = currentSpan.getSofaTracer().getTracerType();
        if (!ComponentNameConstants.FLEXIBLE.equalsIgnoreCase(componentType)) {
            SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00014"), componentType));
            return false;
        }
        return true;
    }

    private SpanTags() {
    }
}