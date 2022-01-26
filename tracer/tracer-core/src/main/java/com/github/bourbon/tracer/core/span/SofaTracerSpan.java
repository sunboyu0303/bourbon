package com.github.bourbon.tracer.core.span;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.extensions.SpanExtensionFactory;
import com.github.bourbon.tracer.core.reporter.facade.Reporter;
import com.github.bourbon.tracer.core.tags.SpanTags;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.tag.Tags;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

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
        SofaTracerSpanContext spanContext = this.sofaTracerSpanContext.cloneInstance();
        Map<String, Object> tags = new HashMap<>();
        tags.putAll(this.tagsWithBool);
        tags.putAll(this.tagsWithStr);
        tags.putAll(this.tagsWithNumber);
        SofaTracerSpan cloneSpan = new SofaTracerSpan(this.sofaTracer, this.startTime, this.spanReferences, this.operationName, spanContext, tags);
        if (this.logs.size() > 0) {
            for (LogData logData : this.logs) {
                cloneSpan.log(logData);
            }
        }
        cloneSpan.setEndTime(this.endTime);
        cloneSpan.setLogType(this.logType);
        cloneSpan.setParentSofaTracerSpan(this.parentSofaTracerSpan);

        return cloneSpan;
    }

    public SofaTracerSpan(SofaTracer sofaTracer, long startTime, String operationName, SofaTracerSpanContext context, Map<String, ?> tags) {
        this(sofaTracer, startTime, null, operationName, ObjectUtils.defaultSupplierIfNull(context, SofaTracerSpanContext::rootStart), tags);
    }

    public SofaTracerSpan(SofaTracer sofaTracer, long startTime, List<SofaTracerSpanReferenceRelationship> spanReferences, String operationName, SofaTracerSpanContext context, Map<String, ?> tags) {
        Assert.notNull(sofaTracer);
        Assert.notNull(context);
        this.sofaTracer = sofaTracer;
        this.startTime = startTime;
        this.spanReferences = ObjectUtils.defaultSupplierIfNull(spanReferences, ArrayList::new);
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
        this.setEndTime(endTime);
        this.sofaTracer.reportSpan(this);
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
        Map<String, String> fields = new HashMap<>();
        fields.put(LogData.EVENT_TYPE_KEY, eventValue);
        return this.log(currentTime, fields);
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
        return this.log(System.currentTimeMillis(), map);
    }

    @Override
    public Span log(String eventName, Object payload) {
        return log(Clock.currentTimeMillis(), eventName, payload);
    }

    @Override
    public Span log(long currentTime, String eventName, Object payload) {
        Assert.isTrue(currentTime >= startTime, "current time must greater than start time");
        return log(currentTime,  MapUtils.of(eventName, payload));
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

    public void reportError(String errorType, Map<String, String> context, Throwable e, String errorSourceApp, String... errorSources) {
        Tags.ERROR.set(this, true);
        //all tags set
        Map<String, Object> tags = MapUtils.newHashMap();
        tags.putAll(getTagsWithStr());
        tags.putAll(getTagsWithBool());
        tags.putAll(getTagsWithNumber());
        tags.put(SpanTags.CURR_APP_TAG.getKey(), errorSourceApp);
        //Construct new CommonLogSpan
        CommonLogSpan commonLogSpan = new CommonLogSpan(this.sofaTracer, Clock.currentTimeMillis(), this.getOperationName(), this.getSofaTracerSpanContext(), tags);
        commonLogSpan.addSlot(Thread.currentThread().getName());
        commonLogSpan.addSlot(errorType);
        // There may be a separator in the output of the business customization, now replace the separator with the corresponding escape character
        commonLogSpan.addSlot(StringUtils.arrayToString(errorSources, ARRAY_SEPARATOR, "", ""));
        commonLogSpan.addSlot(StringUtils.mapToString(context));
        commonLogSpan.addSlot(this.getSofaTracerSpanContext() == null ? StringConstants.EMPTY
                : this.getSofaTracerSpanContext().getBizSerializedBaggage());

        if (e == null) {
            commonLogSpan.addSlot(StringConstants.EMPTY);
        } else {
            StringWriter sw = new StringWriter(256);
            e.printStackTrace(new PrintWriter(sw));
            String exception = sw.getBuffer().toString();
            commonLogSpan.addSlot(exception);
        }
        CommonTracerManager.reportError(commonLogSpan);
    }

    public void profile(String profileApp, String protocolType, String profileMessage) {
        Map<String, Object> tags = new HashMap<>();
        tags.putAll(this.getTagsWithStr());
        tags.putAll(this.getTagsWithBool());
        tags.putAll(this.getTagsWithNumber());
        tags.put(SpanTags.CURR_APP_TAG.getKey(), profileApp);
        CommonLogSpan commonLogSpan = new CommonLogSpan(this.sofaTracer, Clock.currentTimeMillis(), this.getOperationName(), this.getSofaTracerSpanContext(), tags);

        commonLogSpan.addSlot(protocolType);
        commonLogSpan.addSlot(profileMessage);

        CommonTracerManager.reportProfile(commonLogSpan);
    }

    public SofaTracerSpan getThisAsParentWhenExceedLayer() {
        final SofaTracerSpan parent;
        String rpcId = this.sofaTracerSpanContext.getSpanId();
        if (StringUtils.countMatches(rpcId, '.') + 1 > SofaTracerConstant.MAX_LAYER) {
            SofaTracerSpanContext parentSpanContext = SofaTracerSpanContext.rootStart();
            Map<String, String> baggage = new HashMap<>();
            baggage.putAll(this.sofaTracerSpanContext.getBizBaggage());
            parentSpanContext.addBizBaggage(baggage);
            parent = new SofaTracerSpan(this.sofaTracer, Clock.currentTimeMillis(), this.operationName, parentSpanContext, null);
            // Record in the log to prevent this from happening but not to know quickly
            SelfLog.errorWithTraceId("OpenTracing Span layer exceed max layer limit " + SofaTracerConstant.MAX_LAYER, this.sofaTracerSpanContext.getTraceId());
        } else {
            parent = this;
        }
        return parent;
    }

    public List<SofaTracerSpanReferenceRelationship> getSpanReferences() {
        if (spanReferences == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(spanReferences);
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

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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

    public String getOperationName() {
        return operationName;
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

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public SofaTracerSpan getParentSofaTracerSpan() {
        if (this.parentSofaTracerSpan != null) {
            return this.parentSofaTracerSpan.getThisAsParentWhenExceedLayer();
        }
        return null;
    }

    public void setParentSofaTracerSpan(SofaTracerSpan parentSofaTracerSpan) {
        this.parentSofaTracerSpan = parentSofaTracerSpan;
    }

    public boolean isServer() {
        return Tags.SPAN_KIND_SERVER.equals(tagsWithStr.get(Tags.SPAN_KIND.getKey()));
    }

    public boolean isClient() {
        return Tags.SPAN_KIND_CLIENT.equals(tagsWithStr.get(Tags.SPAN_KIND.getKey()));
    }

    private void setTags(Map<String, ?> tags) {
        if (tags == null || tags.size() <= 0) {
            return;
        }
        for (Map.Entry<String, ?> entry : tags.entrySet()) {
            String key = entry.getKey();
            if (CharSequenceUtils.isBlank(key)) {
                continue;
            }
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof String) {
                this.setTag(key, (String) value);
            } else if (value instanceof Boolean) {
                this.setTag(key, (Boolean) value);
            } else if (value instanceof Number) {
                this.setTag(key, (Number) value);
            } else {
                SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstant.SPACE_ID, "01-00012"), value.getClass()));
            }
        }
    }

    @Override
    public String toString() {
        return "SofaTracerSpan{operationName='" + operationName + '\'' + ", sofaTracerSpanContext=" + sofaTracerSpanContext + '}';
    }
}