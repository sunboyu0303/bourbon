package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import io.opentracing.propagation.TextMap;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 16:49
 */
public abstract class AbstractTextB3Formatter implements RegistryExtractorInjector<TextMap> {
    /**
     * 128/64 bit traceId lower-hex string (required)
     */
    public static final String TRACE_ID_KEY_HEAD = "X-B3-TraceId";
    /**
     * 64 bit spanId lower-hex string (required)
     */
    public static final String SPAN_ID_KEY_HEAD = "X-B3-SpanId";
    /**
     * 64 bit parentSpanId lower-hex string (absent on root span)
     */
    public static final String PARENT_SPAN_ID_KEY_HEAD = "X-B3-ParentSpanId";
    /**
     * "1" means report this span to the tracing system, "0" means do not. (absent means defer the decision to the receiver of this header).
     */
    public static final String SAMPLED_KEY_HEAD = "X-B3-Sampled";
    /**
     * "1" implies sampled and is a request to override collection-tier sampling policy.
     */
    static final String FLAGS_KEY_HEAD = "X-B3-Flags";
    /**
     * Baggage items prefix
     */
    static final String BAGGAGE_KEY_PREFIX = "baggage-";
    /**
     * System Baggage items prefix
     */
    static final String BAGGAGE_SYS_KEY_PREFIX = "baggage-sys-";

    @Override
    public SofaTracerSpanContext extract(TextMap carrier) {
        if (carrier == null) {
            // There not have tracing propagation head,start root span
            return SofaTracerSpanContext.rootStart();
        }

        String traceId = null;
        String spanId = null;
        String parentId = null;
        boolean sampled = false;
        boolean isGetSampled = false;
        Map<String, String> sysBaggage = MapUtils.newConcurrentHashMap();
        Map<String, String> bizBaggage = MapUtils.newConcurrentHashMap();
        // Get others trace context items, the first value wins.
        for (Map.Entry<String, String> entry : carrier) {
            String key = entry.getKey();
            if (CharSequenceUtils.isBlank(key)) {
                continue;
            }
            if (traceId == null && TRACE_ID_KEY_HEAD.equalsIgnoreCase(key)) {
                traceId = decodedValue(entry.getValue());
            }
            if (spanId == null && SPAN_ID_KEY_HEAD.equalsIgnoreCase(key)) {
                spanId = decodedValue(entry.getValue());
            }
            if (parentId == null && PARENT_SPAN_ID_KEY_HEAD.equalsIgnoreCase(key)) {
                parentId = decodedValue(entry.getValue());
            }
            if (!isGetSampled && SAMPLED_KEY_HEAD.equalsIgnoreCase(key)) {
                String valueTmp = decodedValue(entry.getValue());
                if ("1".equals(valueTmp)) {
                    sampled = true;
                } else if ("0".equals(valueTmp)) {
                    sampled = false;
                } else {
                    sampled = Boolean.parseBoolean(valueTmp);
                }
                isGetSampled = true;
            }
            if (key.indexOf(BAGGAGE_SYS_KEY_PREFIX) == 0) {
                sysBaggage.put(key.substring(BAGGAGE_SYS_KEY_PREFIX.length()), decodedValue(entry.getValue()));
            }
            if (key.indexOf(BAGGAGE_KEY_PREFIX) == 0) {
                bizBaggage.put(key.substring(BAGGAGE_KEY_PREFIX.length()), decodedValue(entry.getValue()));
            }
        }

        if (traceId == null) {
            // There not have trace id, assumed not have tracing propagation head also,start root span
            return SofaTracerSpanContext.rootStart();
        }

        if (spanId == null) {
            spanId = SofaTracer.ROOT_SPAN_ID;
        }
        if (parentId == null) {
            parentId = StringConstants.EMPTY;
        }
        return new SofaTracerSpanContext(traceId, spanId, parentId, sampled).addSysBaggage(sysBaggage).addBizBaggage(bizBaggage);
    }

    @Override
    public void inject(SofaTracerSpanContext spanContext, TextMap carrier) {
        if (carrier == null || spanContext == null) {
            return;
        }
        // Tracing Context
        carrier.put(TRACE_ID_KEY_HEAD, encodedValue(spanContext.getTraceId()));
        carrier.put(SPAN_ID_KEY_HEAD, encodedValue(spanContext.getSpanId()));
        carrier.put(PARENT_SPAN_ID_KEY_HEAD, encodedValue(spanContext.getParentId()));
        carrier.put(SPAN_ID_KEY_HEAD, encodedValue(spanContext.getSpanId()));
        carrier.put(SAMPLED_KEY_HEAD, encodedValue(String.valueOf(spanContext.isSampled())));

        // System Baggage items
        spanContext.getSysBaggage().forEach((k, v) -> carrier.put(BAGGAGE_SYS_KEY_PREFIX + k, encodedValue(v)));
        // Business Baggage items
        spanContext.getBizBaggage().forEach((k, v) -> carrier.put(BAGGAGE_KEY_PREFIX + k, encodedValue(v)));
    }

    protected abstract String encodedValue(String value);

    protected abstract String decodedValue(String value);
}