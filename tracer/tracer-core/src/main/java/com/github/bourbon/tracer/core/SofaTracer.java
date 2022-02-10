package com.github.bourbon.tracer.core;

import com.github.bourbon.base.code.LogCode2Description;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.generator.TraceIdGenerator;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.constants.ComponentNameConstants;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.listener.SpanReportListenerHolder;
import com.github.bourbon.tracer.core.registry.TracerFormatRegistry;
import com.github.bourbon.tracer.core.reporter.facade.Reporter;
import com.github.bourbon.tracer.core.samplers.Sampler;
import com.github.bourbon.tracer.core.samplers.SamplerFactory;
import com.github.bourbon.tracer.core.samplers.SamplingStatus;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;
import com.github.bourbon.tracer.core.span.SofaTracerSpanReferenceRelationship;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;

import java.util.List;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:05
 */
public class SofaTracer implements Tracer {

    public static final String ROOT_SPAN_ID = StringConstants.ZERO;

    private final String tracerType;
    private final Reporter clientReporter;
    private final Reporter serverReporter;
    private final Map<String, Object> tracerTags = MapUtils.newConcurrentHashMap();
    private final Sampler sampler;

    protected SofaTracer(String tracerType, Reporter clientReporter, Reporter serverReporter, Sampler sampler, Map<String, Object> tracerTags) {
        this.tracerType = tracerType;
        this.clientReporter = clientReporter;
        this.serverReporter = serverReporter;
        this.sampler = sampler;
        BooleanUtils.defaultIfPredicateElseConsumer(tracerTags, MapUtils::isNotEmpty, this.tracerTags::putAll);
    }

    protected SofaTracer(String tracerType, Sampler sampler) {
        this.tracerType = tracerType;
        this.clientReporter = null;
        this.serverReporter = null;
        this.sampler = sampler;
    }

    @Override
    public SpanBuilder buildSpan(String operationName) {
        return new SofaTracerSpanBuilder(operationName);
    }

    @Override
    public <C> void inject(SpanContext spanContext, Format<C> format, C carrier) {
        ObjectUtils.requireNonNull(TracerFormatRegistry.getRegistry(format), () -> new IllegalArgumentException("Unsupported injector format: " + format)).inject((SofaTracerSpanContext) spanContext, carrier);
    }

    @Override
    public <C> SpanContext extract(Format<C> format, C carrier) {
        return ObjectUtils.requireNonNull(TracerFormatRegistry.getRegistry(format), () -> new IllegalArgumentException("Unsupported extractor format: " + format)).extract(carrier);
    }

    public void reportSpan(SofaTracerSpan span) {
        if (span == null) {
            return;
        }
        if (ObjectUtils.nonNull(sampler) && ObjectUtils.isNull(span.getParentSofaTracerSpan())) {
            span.getSofaTracerSpanContext().setSampled(sampler.sample(span).isSampled());
        }
        invokeReportListeners(span);
        if (span.isClient() || ComponentNameConstants.FLEXIBLE.equalsIgnoreCase(getTracerType())) {
            ObjectUtils.nonNullConsumer(clientReporter, r -> r.report(span));
        } else if (span.isServer()) {
            ObjectUtils.nonNullConsumer(serverReporter, r -> r.report(span));
        } else {
            SelfLog.warn("Span reported neither client nor server.Ignore!");
        }
    }

    public void close() {
        ObjectUtils.nonNullConsumer(clientReporter, Reporter::close);
        ObjectUtils.nonNullConsumer(serverReporter, Reporter::close);
        ObjectUtils.nonNullConsumer(sampler, Sampler::close);
    }

    public String getTracerType() {
        return tracerType;
    }

    public Reporter getClientReporter() {
        return clientReporter;
    }

    public Reporter getServerReporter() {
        return serverReporter;
    }

    public Sampler getSampler() {
        return sampler;
    }

    public Map<String, Object> getTracerTags() {
        return tracerTags;
    }

    @Override
    public String toString() {
        return "SofaTracer{tracerType='" + tracerType + "'}";
    }

    protected void invokeReportListeners(SofaTracerSpan sofaTracerSpan) {
        BooleanUtils.defaultIfPredicateElseConsumer(SpanReportListenerHolder.getSpanReportListenersHolder(), CollectionUtils::isNotEmpty, list -> list.forEach(listener -> listener.onSpanReport(sofaTracerSpan)));
    }

    public class SofaTracerSpanBuilder implements SpanBuilder {

        private final String operationName;

        private volatile long startTime = -1;

        private final List<SofaTracerSpanReferenceRelationship> references = ListUtils.newArrayList();

        private final Map<String, Object> tags = MapUtils.newHashMap();

        public SofaTracerSpanBuilder(String operationName) {
            this.operationName = operationName;
        }

        @Override
        public Tracer.SpanBuilder asChildOf(SpanContext parent) {
            return addReference(References.CHILD_OF, parent);
        }

        @Override
        public Tracer.SpanBuilder asChildOf(Span parentSpan) {
            return ObjectUtils.defaultIfNullElseFunction(parentSpan, s -> addReference(References.CHILD_OF, s.context()), this);
        }

        @Override
        public Tracer.SpanBuilder addReference(String referenceType, SpanContext referencedContext) {
            if (referencedContext == null) {
                return this;
            }
            if (!(referencedContext instanceof SofaTracerSpanContext)) {
                return this;
            }
            if (!References.CHILD_OF.equals(referenceType) && !References.FOLLOWS_FROM.equals(referenceType)) {
                return this;
            }
            references.add(new SofaTracerSpanReferenceRelationship((SofaTracerSpanContext) referencedContext, referenceType));
            return this;
        }

        @Override
        public Tracer.SpanBuilder withTag(String key, String value) {
            tags.put(key, value);
            return this;
        }

        @Override
        public Tracer.SpanBuilder withTag(String key, boolean value) {
            tags.put(key, value);
            return this;
        }

        @Override
        public Tracer.SpanBuilder withTag(String key, Number value) {
            tags.put(key, value);
            return this;
        }

        @Override
        public Tracer.SpanBuilder withStartTimestamp(long microseconds) {
            startTime = microseconds;
            return this;
        }

        @Override
        public Span start() {
            SofaTracerSpanContext context = BooleanUtils.defaultSupplierIfFalse(CollectionUtils.isNotEmpty(references), this::createChildContext, this::createRootSpanContext);
            SofaTracerSpan span = new SofaTracerSpan(SofaTracer.this, startTime > 0 ? startTime : Clock.currentTimeMillis(), references, operationName, context, tags);
            context.setSampled(calculateSampler(span));
            return span;
        }

        private boolean calculateSampler(SofaTracerSpan sofaTracerSpan) {
            boolean isSampled = false;
            if (CollectionUtils.isNotEmpty(references)) {
                isSampled = preferredReference().isSampled();
            } else {
                if (sampler != null) {
                    SamplingStatus samplingStatus = sampler.sample(sofaTracerSpan);
                    if (samplingStatus.isSampled()) {
                        isSampled = true;
                        tags.putAll(samplingStatus.getTags());
                    }
                }
            }
            return isSampled;
        }

        private SofaTracerSpanContext createRootSpanContext() {
            return new SofaTracerSpanContext(TraceIdGenerator.getTraceId(), ROOT_SPAN_ID, StringConstants.EMPTY);
        }

        private SofaTracerSpanContext createChildContext() {
            SofaTracerSpanContext context = preferredReference();
            return new SofaTracerSpanContext(context.getTraceId(), context.nextChildContextId(), context.getSpanId(), context.isSampled()).addBizBaggage(createChildBaggage(true)).addSysBaggage(createChildBaggage(false));
        }

        private Map<String, String> createChildBaggage(boolean isBiz) {
            if (references.size() == 1) {
                SofaTracerSpanContext context = references.get(0).getSofaTracerSpanContext();
                return BooleanUtils.defaultSupplierIfFalse(isBiz, context::getBizBaggage, context::getSysBaggage);
            }
            Map<String, String> baggage = MapUtils.newHashMap();
            for (SofaTracerSpanReferenceRelationship reference : references) {
                SofaTracerSpanContext context = reference.getSofaTracerSpanContext();
                BooleanUtils.defaultIfPredicateElseConsumer(BooleanUtils.defaultSupplierIfFalse(isBiz, context::getBizBaggage, context::getSysBaggage), MapUtils::isNotEmpty, baggage::putAll);
            }
            return baggage;
        }

        private SofaTracerSpanContext preferredReference() {
            final SofaTracerSpanReferenceRelationship preferredReference = references.get(0);
            for (SofaTracerSpanReferenceRelationship reference : references) {
                if (References.CHILD_OF.equals(reference.getReferenceType()) && !References.CHILD_OF.equals(preferredReference.getReferenceType())) {
                    return reference.getSofaTracerSpanContext();
                }
            }
            return preferredReference.getSofaTracerSpanContext();
        }
    }

    public static final class Builder {

        private final String tracerType;

        private Reporter clientReporter;

        private Reporter serverReporter;

        private final Map<String, Object> tracerTags = MapUtils.newHashMap();

        private Sampler sampler;

        public Builder(String tracerType) {
            Assert.notNull(tracerType, "tracerType must be not empty");
            this.tracerType = tracerType;
        }

        public Builder withClientReporter(Reporter clientReporter) {
            this.clientReporter = clientReporter;
            return this;
        }

        public Builder withServerReporter(Reporter serverReporter) {
            this.serverReporter = serverReporter;
            return this;
        }

        public Builder withSampler(Sampler sampler) {
            this.sampler = sampler;
            return this;
        }

        public Builder withTag(String key, String value) {
            tracerTags.put(key, value);
            return this;
        }

        public Builder withTag(String key, Boolean value) {
            tracerTags.put(key, value);
            return this;
        }

        public Builder withTag(String key, Number value) {
            tracerTags.put(key, value);
            return this;
        }

        public Builder withTags(Map<String, ?> tags) {
            if (MapUtils.isNotEmpty(tags)) {
                for (Map.Entry<String, ?> entry : tags.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (CharSequenceUtils.isBlank(key) || value == null) {
                        continue;
                    }
                    if (value instanceof String) {
                        withTag(key, (String) value);
                    } else if (value instanceof Boolean) {
                        withTag(key, (Boolean) value);
                    } else if (value instanceof Number) {
                        withTag(key, (Number) value);
                    } else {
                        SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00003"), value.getClass().toString()));
                    }
                }
            }
            return this;
        }

        public SofaTracer build() {
            try {
                sampler = SamplerFactory.getSampler();
            } catch (Exception e) {
                SelfLog.error(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00002"));
            }
            return new SofaTracer(tracerType, clientReporter, serverReporter, sampler, tracerTags);
        }
    }
}