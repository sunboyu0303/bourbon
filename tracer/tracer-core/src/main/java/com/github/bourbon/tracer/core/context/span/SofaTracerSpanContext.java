package com.github.bourbon.tracer.core.context.span;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.generator.TraceIdGenerator;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.utils.TracerUtils;
import io.opentracing.SpanContext;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:36
 */
public class SofaTracerSpanContext implements SpanContext {

    private static final String RPC_ID_SEPARATOR = StringConstants.DOT;

    private static final String TRACE_ID_KET = "tcid";

    private static final String SPAN_ID_KET = "spid";

    private static final String PARENT_SPAN_ID_KET = "pspid";

    private static final String SAMPLE_KET = "sample";

    private static final String SYS_BAGGAGE_PREFIX_KEY = "_sys_";

    private final String traceId;

    private final String spanId;

    private final String parentId;

    private boolean isSampled;

    private final Map<String, String> sysBaggage = MapUtils.newConcurrentHashMap();

    private final Map<String, String> bizBaggage = MapUtils.newConcurrentHashMap();

    private AtomicInteger childContextIndex = new AtomicInteger(0);

    public SofaTracerSpanContext cloneInstance() {
        return new SofaTracerSpanContext(traceId, spanId, parentId, isSampled).addSysBaggage(sysBaggage).addBizBaggage(bizBaggage).setChildContextIndex(childContextIndex);
    }

    public SofaTracerSpanContext(String traceId, String spanId) {
        this(traceId, spanId, null);
    }

    public SofaTracerSpanContext(String traceId, String spanId, String parentId) {
        this(traceId, spanId, parentId, false);
    }

    public SofaTracerSpanContext(String traceId, String spanId, String parentId, boolean isSampled) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentId = BooleanUtils.defaultSupplierIfPredicate(parentId, CharSequenceUtils::isNotBlank, t -> t, () -> genParentSpanId(spanId));
        this.isSampled = isSampled;
    }

    public SofaTracerSpanContext addBizBaggage(Map<String, String> bizBaggage) {
        if (MapUtils.isNotEmpty(bizBaggage)) {
            this.bizBaggage.putAll(bizBaggage);
        }
        return this;
    }

    public SofaTracerSpanContext addSysBaggage(Map<String, String> sysBaggage) {
        if (MapUtils.isNotEmpty(sysBaggage)) {
            this.sysBaggage.putAll(sysBaggage);
        }
        return this;
    }

    private SofaTracerSpanContext setChildContextIndex(AtomicInteger childContextIndex) {
        this.childContextIndex = childContextIndex;
        return this;
    }

    public AtomicInteger getChildContextIndex() {
        return childContextIndex;
    }

    @Override
    public Iterable<Map.Entry<String, String>> baggageItems() {
        Map<String, String> allBaggage = MapUtils.newHashMap();
        if (MapUtils.isNotEmpty(bizBaggage)) {
            allBaggage.putAll(bizBaggage);
        }
        if (MapUtils.isNotEmpty(sysBaggage)) {
            allBaggage.putAll(sysBaggage);
        }
        return allBaggage.entrySet();
    }

    private String contextAsString() {
        return String.format("%s:%s:%s:%s", traceId, spanId, parentId, isSampled);
    }

    public String getBizSerializedBaggage() {
        return MapUtils.mapToString(bizBaggage);
    }

    public String getSysSerializedBaggage() {
        return MapUtils.mapToString(sysBaggage);
    }

    public void deserializeBizBaggage(String bizBaggageAttrs) {
        MapUtils.stringToMap(bizBaggageAttrs, bizBaggage);
        if (CharSequenceUtils.isNotBlank(bizBaggageAttrs)) {
            if (bizBaggageAttrs.length() > TracerUtils.getBaggageMaxLength() / 2) {
                SelfLog.infoWithTraceId("Get biz baggage from upstream system, and the length is " + bizBaggageAttrs.length());
            }
        }
    }

    public void deserializeSysBaggage(String sysBaggageAttrs) {
        MapUtils.stringToMap(sysBaggageAttrs, sysBaggage);
        if (CharSequenceUtils.isNotBlank(sysBaggageAttrs)) {
            if (sysBaggageAttrs.length() > TracerUtils.getSysBaggageMaxLength() / 2) {
                SelfLog.infoWithTraceId("Get system baggage from upstream system, and the length is " + sysBaggageAttrs.length());
            }
        }
    }

    public String serializeSpanContext() {
        StringBuilder sb = new StringBuilder();
        sb.append(TRACE_ID_KET).append(StringConstants.EQUAL).append(traceId).append(StringConstants.AND)
                .append(SPAN_ID_KET).append(StringConstants.EQUAL).append(spanId).append(StringConstants.AND)
                .append(PARENT_SPAN_ID_KET).append(StringConstants.EQUAL).append(parentId).append(StringConstants.AND)
                .append(SAMPLE_KET).append(StringConstants.EQUAL).append(isSampled).append(StringConstants.AND);
        if (sysBaggage.size() > 0) {
            sb.append(MapUtils.mapToStringWithPrefix(sysBaggage, SYS_BAGGAGE_PREFIX_KEY));
        }
        if (bizBaggage.size() > 0) {
            sb.append(MapUtils.mapToString(bizBaggage));
        }
        return sb.toString();
    }

    public static SofaTracerSpanContext deserializeFromString(String deserializeValue) {
        if (CharSequenceUtils.isBlank(deserializeValue)) {
            return SofaTracerSpanContext.rootStart();
        }
        String traceId = TraceIdGenerator.getTraceId();
        String spanId = SofaTracer.ROOT_SPAN_ID;
        String parentId = StringConstants.EMPTY;
        boolean sampled = false;

        Map<String, String> sysBaggage = MapUtils.newHashMap();
        Map<String, String> baggage = MapUtils.newHashMap();
        Map<String, String> spanContext = MapUtils.newHashMap();
        MapUtils.stringToMap(deserializeValue, spanContext);

        for (Map.Entry<String, String> entry : spanContext.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (CharSequenceUtils.isBlank(key) || CharSequenceUtils.isBlank(value)) {
                continue;
            }
            if (TRACE_ID_KET.equals(key)) {
                traceId = value;
                continue;
            }
            if (SPAN_ID_KET.equals(key)) {
                spanId = value;
                continue;
            }
            if (PARENT_SPAN_ID_KET.equals(key)) {
                parentId = value;
                continue;
            }
            if (SAMPLE_KET.equals(key)) {
                sampled = Boolean.parseBoolean(value);
                continue;
            }
            int sysIndex = key.indexOf(SYS_BAGGAGE_PREFIX_KEY);
            if (sysIndex == 0) {
                sysBaggage.put(key.substring(SYS_BAGGAGE_PREFIX_KEY.length()), value);
            } else {
                baggage.put(key, value);
            }
        }
        SofaTracerSpanContext sofaTracerSpanContext = new SofaTracerSpanContext(traceId, spanId, parentId, sampled);
        if (sysBaggage.size() > 0) {
            sofaTracerSpanContext.addSysBaggage(sysBaggage);
        }
        if (baggage.size() > 0) {
            sofaTracerSpanContext.addBizBaggage(baggage);
        }
        return sofaTracerSpanContext;
    }

    public static SofaTracerSpanContext rootStart() {
        return rootStart(false);
    }

    public static SofaTracerSpanContext rootStart(boolean isSampled) {
        return new SofaTracerSpanContext(TraceIdGenerator.getTraceId(), SofaTracer.ROOT_SPAN_ID, StringConstants.EMPTY, isSampled);
    }

    private String genParentSpanId(String spanId) {
        return CharSequenceUtils.isBlank(spanId) || spanId.lastIndexOf(RPC_ID_SEPARATOR) < 0 ? StringConstants.EMPTY : spanId.substring(0, spanId.lastIndexOf(RPC_ID_SEPARATOR));
    }

    public String getTraceId() {
        return BooleanUtils.defaultIfPredicate(traceId, CharSequenceUtils::isNotBlank, t -> t, StringConstants.EMPTY);
    }

    public String getSpanId() {
        return BooleanUtils.defaultIfPredicate(spanId, CharSequenceUtils::isNotBlank, s -> s, StringConstants.EMPTY);
    }

    public String getParentId() {
        return BooleanUtils.defaultIfPredicate(parentId, CharSequenceUtils::isNotBlank, p -> p, StringConstants.EMPTY);
    }

    public SofaTracerSpanContext setBizBaggageItem(String key, String value) {
        if (CharSequenceUtils.isNotBlank(key)) {
            bizBaggage.put(key, value);
        }
        return this;
    }

    public String getBizBaggageItem(String key) {
        return bizBaggage.get(key);
    }

    public Map<String, String> getBizBaggage() {
        return bizBaggage;
    }

    public SofaTracerSpanContext setSysBaggageItem(String key, String value) {
        if (CharSequenceUtils.isNotBlank(key) && CharSequenceUtils.isNotBlank(value)) {
            sysBaggage.put(key, value);
        }
        return this;
    }

    public String getSysBaggageItem(String key) {
        return sysBaggage.get(key);
    }

    public Map<String, String> getSysBaggage() {
        return sysBaggage;
    }

    public boolean isSampled() {
        return isSampled;
    }

    public void setSampled(boolean sampled) {
        isSampled = sampled;
    }

    public String nextChildContextId() {
        return spanId + RPC_ID_SEPARATOR + childContextIndex.incrementAndGet();
    }

    public String lastChildContextId() {
        return spanId + RPC_ID_SEPARATOR + childContextIndex.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SofaTracerSpanContext)) {
            return false;
        }
        SofaTracerSpanContext that = (SofaTracerSpanContext) o;
        if (!traceId.equals(that.traceId)) {
            return false;
        }
        if (!spanId.equals(that.spanId)) {
            return false;
        }
        if (CharSequenceUtils.isBlank(parentId)) {
            return CharSequenceUtils.isBlank(that.parentId);
        }
        return parentId.equals(that.parentId);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(traceId, spanId, parentId);
    }
}