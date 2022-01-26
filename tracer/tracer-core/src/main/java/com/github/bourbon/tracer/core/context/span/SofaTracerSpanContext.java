package com.github.bourbon.tracer.core.context.span;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.utils.TracerUtils;
import io.opentracing.SpanContext;

import java.util.HashMap;
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

    private String traceId;

    private String spanId;

    private String parentId;

    private boolean isSampled;

    private final Map<String, String> sysBaggage = MapUtils.newConcurrentHashMap();

    private final Map<String, String> bizBaggage = MapUtils.newConcurrentHashMap();

    private AtomicInteger childContextIndex = new AtomicInteger(0);

    public SofaTracerSpanContext cloneInstance() {
        SofaTracerSpanContext context = new SofaTracerSpanContext(traceId, spanId, parentId, isSampled).addSysBaggage(sysBaggage).addBizBaggage(bizBaggage);
        context.childContextIndex = childContextIndex;
        return context;
    }

    public SofaTracerSpanContext() {
        this(StringConstants.EMPTY, StringConstants.EMPTY, null, false);
    }

    public SofaTracerSpanContext(String traceId, String spanId) {
        this(traceId, spanId, null, false);
    }

    public SofaTracerSpanContext(String traceId, String spanId, String parentId) {
        this(traceId, spanId, parentId, false);
    }

    public SofaTracerSpanContext(String traceId, String spanId, String parentId, boolean isSampled) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentId = BooleanUtils.defaultSupplierIfPredicate(parentId, pid -> !CharSequenceUtils.isBlank(pid), t -> t, () -> genParentSpanId(spanId));
        this.isSampled = isSampled;
    }

    public SofaTracerSpanContext addBizBaggage(Map<String, String> bizBaggage) {
        if (!MapUtils.isEmpty(bizBaggage)) {
            this.bizBaggage.putAll(bizBaggage);
        }
        return this;
    }

    public SofaTracerSpanContext addSysBaggage(Map<String, String> sysBaggage) {
        if (!MapUtils.isEmpty(sysBaggage)) {
            this.sysBaggage.putAll(sysBaggage);
        }
        return this;
    }

    @Override
    public Iterable<Map.Entry<String, String>> baggageItems() {
        Map<String, String> allBaggage = MapUtils.newHashMap();
        if (!MapUtils.isEmpty(bizBaggage)) {
            allBaggage.putAll(bizBaggage);
        }
        if (!MapUtils.isEmpty(sysBaggage)) {
            allBaggage.putAll(sysBaggage);
        }
        return allBaggage.entrySet();
    }

    private String contextAsString() {
        return String.format("%s:%s:%s:%s", traceId, spanId, parentId, isSampled);
    }

    public String getBizSerializedBaggage() {
        return StringUtils.mapToString(bizBaggage);
    }

    public String getSysSerializedBaggage() {
        return StringUtils.mapToString(sysBaggage);
    }

    public void deserializeBizBaggage(String bizBaggageAttrs) {
        StringUtils.stringToMap(bizBaggageAttrs, bizBaggage);
        if (!CharSequenceUtils.isBlank(bizBaggageAttrs)) {
            if (bizBaggageAttrs.length() > TracerUtils.getBaggageMaxLength() / 2) {
                SelfLog.infoWithTraceId("Get biz baggage from upstream system, and the length is " + bizBaggageAttrs.length());
            }
        }
    }

    public void deserializeSysBaggage(String sysBaggageAttrs) {
        StringUtils.stringToMap(sysBaggageAttrs, sysBaggage);
        if (!CharSequenceUtils.isBlank(sysBaggageAttrs)) {
            if (sysBaggageAttrs.length() > TracerUtils.getSysBaggageMaxLength() / 2) {
                SelfLog.infoWithTraceId("Get system baggage from upstream system, and the length is " + sysBaggageAttrs.length());
            }
        }
    }

    public String serializeSpanContext() {
        StringBuilder serializedValue = new StringBuilder();
        serializedValue.append(TRACE_ID_KET).append(StringConstants.EQUAL).append(traceId).append(StringConstants.AND);
        serializedValue.append(SPAN_ID_KET).append(StringConstants.EQUAL).append(spanId).append(StringConstants.AND);
        serializedValue.append(PARENT_SPAN_ID_KET).append(StringConstants.EQUAL).append(parentId).append(StringConstants.AND);
        serializedValue.append(SAMPLE_KET).append(StringConstants.EQUAL).append(isSampled).append(StringConstants.AND);
        if (this.sysBaggage.size() > 0) {
            serializedValue.append(StringUtils.mapToStringWithPrefix(sysBaggage, SYS_BAGGAGE_PREFIX_KEY));
        }
        if (this.bizBaggage.size() > 0) {
            serializedValue.append(StringUtils.mapToString(bizBaggage));
        }
        return serializedValue.toString();
    }

    public static SofaTracerSpanContext deserializeFromString(String deserializeValue) {
        if (CharSequenceUtils.isBlank(deserializeValue)) {
            return SofaTracerSpanContext.rootStart();
        }
        // default value for SofaTracerSpanContext
        String traceId = TraceIdGenerator.generate();
        String spanId = SofaTracer.ROOT_SPAN_ID;
        String parentId = StringConstants.EMPTY;
        // sampled default is false
        boolean sampled = false;

        Map<String, String> sysBaggage = MapUtils.newHashMap();
        Map<String, String> baggage = MapUtils.newHashMap();

        Map<String, String> spanContext = new HashMap<>();
        StringUtils.stringToMap(deserializeValue, spanContext);

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
        return new SofaTracerSpanContext(TraceIdGenerator.generate(), SofaTracer.ROOT_SPAN_ID, StringConstants.EMPTY, isSampled);
    }

    private String genParentSpanId(String spanId) {
        return (CharSequenceUtils.isBlank(spanId) || spanId.lastIndexOf(RPC_ID_SEPARATOR) < 0) ? StringConstants.EMPTY : spanId.substring(0, spanId.lastIndexOf(RPC_ID_SEPARATOR));
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
        this.parentId = genParentSpanId(spanId);
    }

    public SofaTracerSpanContext setBizBaggageItem(String key, String value) {
        if (!CharSequenceUtils.isBlank(key)) {
            bizBaggage.put(key, value);
        }
        return this;
    }

    public String getBizBaggageItem(String key) {
        return bizBaggage.get(key);
    }

    public SofaTracerSpanContext setSysBaggageItem(String key, String value) {
        if (!CharSequenceUtils.isBlank(key) && !CharSequenceUtils.isBlank(value)) {
            sysBaggage.put(key, value);
        }
        return this;
    }

    public String getSysBaggageItem(String key) {
        return sysBaggage.get(key);
    }

    public String getTraceId() {
        return BooleanUtils.defaultIfFalse(CharSequenceUtils.isBlank(traceId), StringConstants.EMPTY, traceId);
    }

    public String getSpanId() {
        return BooleanUtils.defaultIfFalse(CharSequenceUtils.isBlank(spanId), StringConstants.EMPTY, spanId);
    }

    public String getParentId() {
        return BooleanUtils.defaultIfFalse(CharSequenceUtils.isBlank(parentId), StringConstants.EMPTY, parentId);
    }

    public Map<String, String> getBizBaggage() {
        return bizBaggage;
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

    public AtomicInteger getChildContextIndex() {
        return childContextIndex;
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
        int result = traceId.hashCode();
        result = 31 * result + spanId.hashCode();
        result = 31 * result + parentId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SofaTracerSpanContext{traceId='" + traceId + '\'' + ", spanId='" + spanId + '\'' + ", parentId='" + parentId + '\'' + ", isSampled=" + isSampled + ", bizBaggage=" + bizBaggage + ", sysBaggage=" + sysBaggage + ", childContextIndex=" + childContextIndex + '}';
    }
}