package com.github.bourbon.tracer.core;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.registry.RegistryExtractorInjector;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:05
 */
public class SofaTracer implements Tracer {

    public static final String ROOT_SPAN_ID = "0";

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
        if (!MapUtils.isEmpty(tracerTags)) {
            this.tracerTags.putAll(tracerTags);
        }
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
        RegistryExtractorInjector<C> registryExtractor = TracerFormatRegistry.getRegistry(format);
        if (registryExtractor == null) {
            throw new IllegalArgumentException("Unsupported extractor format: " + format);
        }
        return registryExtractor.extract(carrier);
    }

    public void reportSpan(SofaTracerSpan span) {
        if (span == null) {
            return;
        }
        if (sampler != null && span.getParentSofaTracerSpan() == null) {
            span.getSofaTracerSpanContext().setSampled(sampler.sample(span).isSampled());
        }
        this.invokeReportListeners(span);
        if (span.isClient() || this.getTracerType().equalsIgnoreCase(ComponentNameConstants.FLEXIBLE)) {
            if (this.clientReporter != null) {
                this.clientReporter.report(span);
            }
        } else if (span.isServer()) {
            if (this.serverReporter != null) {
                this.serverReporter.report(span);
            }
        } else {
            SelfLog.warn("Span reported neither client nor server.Ignore!");
        }
    }

    public void close() {
        if (this.clientReporter != null) {
            this.clientReporter.close();
        }
        if (this.serverReporter != null) {
            this.serverReporter.close();
        }
        if (sampler != null) {
            this.sampler.close();
        }
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
        return "SofaTracer{" + "tracerType='" + tracerType + '}';
    }

    protected void invokeReportListeners(SofaTracerSpan sofaTracerSpan) {
        List<SpanReportListener> listeners = SpanReportListenerHolder.getSpanReportListenersHolder();
        if (listeners != null && listeners.size() > 0) {
            listeners.forEach(listener -> listener.onSpanReport(sofaTracerSpan));
        }
    }

    public class SofaTracerSpanBuilder implements io.opentracing.Tracer.SpanBuilder {

        private String operationName;

        private long startTime = -1;

        private List<SofaTracerSpanReferenceRelationship> references = Collections.emptyList();

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
            return ObjectUtils.defaultSupplierIfNull(parentSpan, span -> addReference(References.CHILD_OF, span.context()), () -> this);
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
            if (references.isEmpty()) {
                references = Collections.singletonList(new SofaTracerSpanReferenceRelationship((SofaTracerSpanContext) referencedContext, referenceType));
            } else {
                if (references.size() == 1) {
                    references = ListUtils.newArrayList(references);
                }
                references.add(new SofaTracerSpanReferenceRelationship((SofaTracerSpanContext) referencedContext, referenceType));
            }
            return this;
        }

        @Override
        public Tracer.SpanBuilder withTag(String key, String value) {
            this.tags.put(key, value);
            return this;
        }

        @Override
        public Tracer.SpanBuilder withTag(String key, boolean value) {
            this.tags.put(key, value);
            return this;
        }

        @Override
        public Tracer.SpanBuilder withTag(String key, Number value) {
            this.tags.put(key, value);
            return this;
        }

        @Override
        public Tracer.SpanBuilder withStartTimestamp(long microseconds) {
            this.startTime = microseconds;
            return this;
        }

        @Override
        public Span start() {
            SofaTracerSpanContext sofaTracerSpanContext = BooleanUtils.defaultSupplierIfFalse(!CollectionUtils.isEmpty(this.references), this::createChildContext, this::createRootSpanContext);
            long begin = this.startTime > 0 ? this.startTime : Clock.currentTimeMillis();
            SofaTracerSpan sofaTracerSpan = new SofaTracerSpan(SofaTracer.this, begin, this.references, this.operationName, sofaTracerSpanContext, this.tags);
            sofaTracerSpanContext.setSampled(calculateSampler(sofaTracerSpan));
            return sofaTracerSpan;
        }

        private boolean calculateSampler(SofaTracerSpan sofaTracerSpan) {
            boolean isSampled = false;
            if (!CollectionUtils.isEmpty(this.references)) {
                isSampled = preferredReference().isSampled();
            } else {
                if (sampler != null) {
                    SamplingStatus samplingStatus = sampler.sample(sofaTracerSpan);
                    if (samplingStatus.isSampled()) {
                        isSampled = true;
                        this.tags.putAll(samplingStatus.getTags());
                    }
                }
            }
            return isSampled;
        }

        private SofaTracerSpanContext createRootSpanContext() {
            return new SofaTracerSpanContext(TraceIdGenerator.generate(), ROOT_SPAN_ID, StringConstants.EMPTY);
        }

        private SofaTracerSpanContext createChildContext() {
            SofaTracerSpanContext context = preferredReference();
            return new SofaTracerSpanContext(context.getTraceId(), context.nextChildContextId(), context.getSpanId(), context.isSampled()).addBizBaggage(this.createChildBaggage(true)).addSysBaggage(this.createChildBaggage(false));
        }

        private Map<String, String> createChildBaggage(boolean isBiz) {
            if (references.size() == 1) {
                SofaTracerSpanContext context = references.get(0).getSofaTracerSpanContext();
                return BooleanUtils.defaultSupplierIfFalse(isBiz, context::getBizBaggage, context::getSysBaggage);
            }
            Map<String, String> baggage = null;
            for (SofaTracerSpanReferenceRelationship reference : references) {
                SofaTracerSpanContext context = reference.getSofaTracerSpanContext();
                Map<String, String> referenceBaggage = BooleanUtils.defaultSupplierIfFalse(isBiz, context::getBizBaggage, context::getSysBaggage);
                if (!MapUtils.isEmpty(referenceBaggage)) {
                    if (baggage == null) {
                        baggage = MapUtils.newHashMap();
                    }
                    baggage.putAll(referenceBaggage);
                }
            }
            return baggage;
        }

        private SofaTracerSpanContext preferredReference() {
            SofaTracerSpanReferenceRelationship preferredReference = references.get(0);
            for (SofaTracerSpanReferenceRelationship reference : references) {
                if (References.CHILD_OF.equals(reference.getReferenceType()) && !References.CHILD_OF.equals(preferredReference.getReferenceType())) {
                    preferredReference = reference;
                    break;
                }
            }
            return preferredReference.getSofaTracerSpanContext();
        }
    }

    public static final class Builder {

        private final String tracerType;

        private Reporter clientReporter;

        private Reporter serverReporter;

        private Map<String, Object> tracerTags = MapUtils.newHashMap();

        private Sampler sampler;

        public Builder(String tracerType) {
            Assert.notBlank(tracerType, "tracerType must be not empty");
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
            if (MapUtils.isEmpty(tags)) {
                return this;
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
                    this.withTag(key, (String) value);
                } else if (value instanceof Boolean) {
                    this.withTag(key, (Boolean) value);
                } else if (value instanceof Number) {
                    this.withTag(key, (Number) value);
                } else {
                    SelfLog.error(String.format(LogCode2Description.convert(SPACE_ID, "01-00003"), value.getClass().toString()));
                }
            }
            return this;
        }

        public SofaTracer build() {
            try {
                sampler = SamplerFactory.getSampler();
            } catch (Exception e) {
                SelfLog.error(LogCode2Description.convert(SPACE_ID, "01-00002"));
            }
            return new SofaTracer(this.tracerType, this.clientReporter, this.serverReporter, this.sampler, this.tracerTags);
        }
    }
}