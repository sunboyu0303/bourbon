package com.github.bourbon.tracer.core.listener;

import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.ListUtils;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 17:36
 */
public final class SpanReportListenerHolder {

    private static final List<SpanReportListener> spanReportListenersHolder = ListUtils.newCopyOnWriteArrayList();

    public static List<SpanReportListener> getSpanReportListenersHolder() {
        return spanReportListenersHolder;
    }

    public static void addSpanReportListeners(List<SpanReportListener> spanReportListenersHolder) {
        if (CollectionUtils.isNotEmpty(spanReportListenersHolder)) {
            SpanReportListenerHolder.spanReportListenersHolder.addAll(spanReportListenersHolder);
        }
    }

    public static void addSpanReportListener(SpanReportListener spanReportListener) {
        if (spanReportListener != null) {
            spanReportListenersHolder.add(spanReportListener);
        }
    }

    public static void clear() {
        spanReportListenersHolder.clear();
    }

    private SpanReportListenerHolder() {
    }
}