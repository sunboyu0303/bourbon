package com.github.bourbon.tracer.core.span;

import com.github.bourbon.base.code.LogCode2Description;
import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.extensions.SpanExtensionFactory;
import com.github.bourbon.tracer.core.reporter.common.CommonTracerManager;
import com.github.bourbon.tracer.core.reporter.facade.Reporter;
import com.github.bourbon.tracer.core.tags.SpanTags;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.tag.Tags;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:37
 */
public class SofaTracerSpan implements Span {

    private final SofaTracer sofaTracer;
    private final List<SofaTracerSpanReferenceRelationship> spanReferences;
    private final Map<String, String> tagsWithStr = MapUtils.newLinkedHashMap();
    private final Map<String, Boolean> tagsWithBool = MapUtils.newLinkedHashMap();
    private final Map<String, Number> tagsWithNumber = MapUtils.newLinkedHashMap();
    private final List<LogData> logs = ListUtils.newLinkedList();

    private String operationName;
    private final SofaTracerSpanContext sofaTracerSpanContext;
    private long startTime;
    private long endTime = -1;
    private String logType = StringConstants.EMPTY;

    private SofaTracerSpan parentSofaTracerSpan = null;

    public SofaTracerSpan cloneInstance() {
        Map<String, Object> tags = MapUtils.newHashMap();
        tags.putAll(tagsWithBool);
        tags.putAll(tagsWithStr);
        tags.putAll(tagsWithNumber);

        SofaTracerSpan cloneSpan = new SofaTracerSpan(sofaTracer, startTime, spanReferences, operationName, sofaTracerSpanContext.cloneInstance(), tags);
        if (logs.size() > 0) {
            logs.forEach(cloneSpan::log);
        }
        cloneSpan.setEndTime(this.endTime).setLogType(this.logType).setParentSofaTracerSpan(this.parentSofaTracerSpan);
        return cloneSpan;
    }

    public SofaTracerSpan(SofaTracer sofaTracer, long startTime, String operationName, SofaTracerSpanContext context, Map<String, ?> tags) {
        this(sofaTracer, startTime, null, operationName, ObjectUtils.defaultSupplierIfNull(context, (Supplier<SofaTracerSpanContext>) SofaTracerSpanContext::rootStart), tags);
    }

    public SofaTracerSpan(SofaTracer sofaTracer, long startTime, List<SofaTracerSpanReferenceRelationship> spanReferences, String operationName, SofaTracerSpanContext context, Map<String, ?> tags) {
        Assert.notNull(sofaTracer);
        Assert.notNull(context);
        this.sofaTracer = sofaTracer;
        this.startTime = startTime;
        this.spanReferences = ObjectUtils.defaultSupplierIfNull(spanReferences, (Function<List<SofaTracerSpanReferenceRelationship>, ArrayList<SofaTracerSpanReferenceRelationship>>) ArrayList::new);
        this.operationName = operationName;
        this.sofaTracerSpanContext = context;
        this.setTags(tags);

        SpanExtensionFactory.logStartedSpan(this);
    }

    @Override
    public SpanContext context() {
        return sofaTracerSpanContext;
    }

    @Override
    public void finish() {
        finish(Clock.currentTimeMillis());
    }

    @Override
    public void finish(long endTime) {
        setEndTime(endTime);
        sofaTracer.reportSpan(this);
        SpanExtensionFactory.logStoppedSpan(this);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public Span setTag(String key, String value) {
        if (CharSequenceUtils.isBlank(key) || CharSequenceUtils.isBlank(value)) {
            return this;
        }
        tagsWithStr.put(key, value);
        if (isServer()) {
            Reporter serverReporter = this.sofaTracer.getServerReporter();
            if (serverReporter != null) {
                this.setLogType(serverReporter.getReporterType());
            }
        } else if (isClient()) {
            Reporter clientReporter = this.sofaTracer.getClientReporter();
            if (clientReporter != null) {
                this.setLogType(clientReporter.getReporterType());
            }
        }
        return this;
    }

    @Override
    public Span setTag(String key, boolean value) {
        this.tagsWithBool.put(key, value);
        return this;
    }

    @Override
    public Span setTag(String key, Number number) {
        if (number != null) {
            tagsWithNumber.put(key, number);
        }
        return this;
    }

    @Override
    public Span log(String eventValue) {
        return log(Clock.currentTimeMillis(), eventValue);
    }

    @Override
    public Span log(long currentTime, String eventValue) {
        Assert.isTrue(currentTime >= startTime, "Current time must greater than start time");
        return log(currentTime, MapUtils.of(LogData.EVENT_TYPE_KEY, eventValue));
    }

    public Span log(LogData logData) {
        if (logData != null) {
            logs.add(logData);
        }
        return this;
    }

    @Override
    public Span log(long currentTime, Map<String, ?> map) {
        Assert.isTrue(currentTime >= startTime, "current time must greater than start time");
        logs.add(new LogData(currentTime, map));
        return this;
    }

    @Override
    public Span log(Map<String, ?> map) {
        return log(Clock.currentTimeMillis(), map);
    }

    @Override
    public Span log(String eventName, Object payload) {
        return log(Clock.currentTimeMillis(), eventName, payload);
    }

    @Override
    public Span log(long currentTime, String eventName, Object payload) {
        Assert.isTrue(currentTime >= startTime, "current time must greater than start time");
        return log(currentTime, MapUtils.of(eventName, payload));
    }

    @Override
    public Span setBaggageItem(String key, String value) {
        sofaTracerSpanContext.setBizBaggageItem(key, value);
        return this;
    }

    @Override
    public String getBaggageItem(String key) {
        return sofaTracerSpanContext.getBizBaggageItem(key);
    }

    @Override
    public Span setOperationName(String operationName) {
        this.operationName = operationName;
        return this;
    }

    public String getOperationName() {
        return operationName;
    }

    public void reportError(String errorType, Map<String, String> context, Throwable e, String errorSourceApp, String... errorSources) {
        Tags.ERROR.set(this, true);

        Map<String, Object> tags = MapUtils.of(SpanTags.CURR_APP_TAG.getKey(), errorSourceApp);
        tags.putAll(getTagsWithStr());
        tags.putAll(getTagsWithBool());
        tags.putAll(getTagsWithNumber());

        CommonLogSpan commonLogSpan = new CommonLogSpan(this.sofaTracer, Clock.currentTimeMillis(), this.getOperationName(), this.getSofaTracerSpanContext(), tags)
                .addSlot(Thread.currentThread().getName()).addSlot(errorType)
                .addSlot(ArrayUtils.toString(errorSources, CharConstants.ARRAY_SEPARATOR, StringConstants.EMPTY, StringConstants.EMPTY))
                .addSlot(MapUtils.mapToString(context))
                .addSlot(ObjectUtils.defaultIfNull(this.getSofaTracerSpanContext(), SofaTracerSpanContext::getBizSerializedBaggage, StringConstants.EMPTY));

        if (e == null) {
            commonLogSpan.addSlot(StringConstants.EMPTY);
        } else {
            StringWriter sw = new StringWriter(256);
            e.printStackTrace(new PrintWriter(sw));
            commonLogSpan.addSlot(sw.getBuffer().toString());
        }

        CommonTracerManager.reportError(commonLogSpan);
    }

    public void profile(String profileApp, String protocolType, String profileMessage) {
        Map<String, Object> tags = MapUtils.of(SpanTags.CURR_APP_TAG.getKey(), profileApp);
        tags.putAll(getTagsWithStr());
        tags.putAll(getTagsWithBool());
        tags.putAll(getTagsWithNumber());

        CommonTracerManager.reportProfile(new CommonLogSpan(this.sofaTracer, Clock.currentTimeMillis(), this.getOperationName(), this.getSofaTracerSpanContext(), tags).addSlot(protocolType).addSlot(profileMessage));
    }

    public SofaTracerSpan getThisAsParentWhenExceedLayer() {
        if (CharSequenceUtils.countMatches(this.sofaTracerSpanContext.getSpanId(), CharConstants.DOT) + 1 > SofaTracerConstants.MAX_LAYER) {
            // Record in the log to prevent this from happening but not to know quickly
            SelfLog.errorWithTraceId("OpenTracing Span layer exceed max layer limit " + SofaTracerConstants.MAX_LAYER, this.sofaTracerSpanContext.getTraceId());
            return new SofaTracerSpan(this.sofaTracer, Clock.currentTimeMillis(), this.operationName, SofaTracerSpanContext.rootStart().addBizBaggage(MapUtils.newHashMap(this.sofaTracerSpanContext.getBizBaggage())), null);
        }
        return this;
    }

    public List<SofaTracerSpanReferenceRelationship> getSpanReferences() {
        return ObjectUtils.defaultSupplierIfNull(spanReferences, Collections::unmodifiableList, Collections::emptyList);
    }

    public SofaTracer getSofaTracer() {
        return sofaTracer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public SofaTracerSpan setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    public long getDurationMicroseconds() {
        return this.endTime - this.startTime;
    }

    public Map<String, String> getTagsWithStr() {
        return tagsWithStr;
    }

    public Map<String, Boolean> getTagsWithBool() {
        return tagsWithBool;
    }

    public Map<String, Number> getTagsWithNumber() {
        return tagsWithNumber;
    }

    public SofaTracerSpanContext getSofaTracerSpanContext() {
        return sofaTracerSpanContext;
    }

    public List<LogData> getLogs() {
        return logs;
    }

    public String getLogType() {
        return logType;
    }

    public SofaTracerSpan setLogType(String logType) {
        this.logType = logType;
        return this;
    }

    public SofaTracerSpan getParentSofaTracerSpan() {
        return ObjectUtils.defaultIfNull(parentSofaTracerSpan, SofaTracerSpan::getThisAsParentWhenExceedLayer);
    }

    public SofaTracerSpan setParentSofaTracerSpan(SofaTracerSpan parentSofaTracerSpan) {
        this.parentSofaTracerSpan = parentSofaTracerSpan;
        return this;
    }

    public boolean isServer() {
        return Tags.SPAN_KIND_SERVER.equals(tagsWithStr.get(Tags.SPAN_KIND.getKey()));
    }

    public boolean isClient() {
        return Tags.SPAN_KIND_CLIENT.equals(tagsWithStr.get(Tags.SPAN_KIND.getKey()));
    }

    private void setTags(Map<String, ?> tags) {
        if (MapUtils.isNotEmpty(tags)) {
            for (Map.Entry<String, ?> entry : tags.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CharSequenceUtils.isBlank(key) || value == null) {
                    continue;
                }
                if (value instanceof String) {
                    this.setTag(key, (String) value);
                } else if (value instanceof Boolean) {
                    this.setTag(key, (Boolean) value);
                } else if (value instanceof Number) {
                    this.setTag(key, (Number) value);
                } else {
                    SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00012"), value.getClass()));
                }
            }
        }
    }
}