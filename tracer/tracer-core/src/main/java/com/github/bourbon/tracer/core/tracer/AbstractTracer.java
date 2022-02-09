package com.github.bourbon.tracer.core.tracer;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.SofaTracer;
import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.context.trace.SofaTraceContext;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;
import com.github.bourbon.tracer.core.reporter.digest.DiskReporterImpl;
import com.github.bourbon.tracer.core.reporter.facade.Reporter;
import com.github.bourbon.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.github.bourbon.tracer.core.span.CommonSpanTags;
import com.github.bourbon.tracer.core.span.LogData;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;
import io.opentracing.tag.Tags;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 08:29
 */
public abstract class AbstractTracer {

    protected final SofaTracer sofaTracer;

    protected AbstractTracer(String tracerType) {
        this(tracerType, true, true);
    }

    protected AbstractTracer(String tracerType, boolean clientTracer, boolean serverTracer) {
        SofaTracer.Builder builder = new SofaTracer.Builder(tracerType);
        if (clientTracer) {
            ObjectUtils.nonNullConsumer(generateReporter(generateClientStatReporter(), getClientDigestReporterLogName(), getClientDigestReporterRollingKey(), getClientDigestReporterLogNameKey(), getClientDigestEncoder()), builder::withClientReporter);
        }
        if (serverTracer) {
            ObjectUtils.nonNullConsumer(generateReporter(generateServerStatReporter(), getServerDigestReporterLogName(), getServerDigestReporterRollingKey(), getServerDigestReporterLogNameKey(), getServerDigestEncoder()), builder::withServerReporter);
        }
        this.sofaTracer = builder.build();
    }

    protected Reporter generateReporter(AbstractSofaTracerStatisticReporter statReporter, String logName, String logRollingKey, String logNameKey, SpanEncoder<SofaTracerSpan> spanEncoder) {
        return new DiskReporterImpl(logName, SofaTracerConfiguration.getRollingPolicy(logRollingKey), SofaTracerConfiguration.getLogReserveConfig(logNameKey), spanEncoder, statReporter, logNameKey);
    }

    protected abstract String getClientDigestReporterLogName();

    protected abstract String getClientDigestReporterRollingKey();

    protected abstract String getClientDigestReporterLogNameKey();

    protected abstract SpanEncoder<SofaTracerSpan> getClientDigestEncoder();

    protected abstract AbstractSofaTracerStatisticReporter generateClientStatReporter();

    protected abstract String getServerDigestReporterLogName();

    protected abstract String getServerDigestReporterRollingKey();

    protected abstract String getServerDigestReporterLogNameKey();

    protected abstract SpanEncoder<SofaTracerSpan> getServerDigestEncoder();

    protected abstract AbstractSofaTracerStatisticReporter generateServerStatReporter();

    public SofaTracerSpan clientSend(String operationName) {
        SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
        SofaTracerSpan serverSpan = sofaTraceContext.pop();
        SofaTracerSpan clientSpan = null;
        try {
            clientSpan = (SofaTracerSpan) sofaTracer.buildSpan(operationName).asChildOf(serverSpan).start();
            // Need to actively cache your own serverSpan, because: asChildOf is concerned about spanContext
            clientSpan.setParentSofaTracerSpan(serverSpan);
            return clientSpan;
        } catch (Throwable throwable) {
            SelfLog.errorWithTraceId("Client Send Error And Restart by Root Span", throwable);
            Map<String, String> bizBaggage = null;
            Map<String, String> sysBaggage = null;
            if (serverSpan != null) {
                bizBaggage = serverSpan.getSofaTracerSpanContext().getBizBaggage();
                sysBaggage = serverSpan.getSofaTracerSpanContext().getSysBaggage();
            }
            clientSpan = errorRecover(bizBaggage, sysBaggage);
        } finally {
            ObjectUtils.nonNullConsumer(clientSpan, s -> {
                s.setTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT).setTag(CommonSpanTags.CURRENT_THREAD_NAME, Thread.currentThread().getName()).log(LogData.CLIENT_SEND_EVENT_VALUE);
                // Put into the thread context
                sofaTraceContext.push(s);
            });
        }
        return clientSpan;
    }

    public void clientReceive(String resultCode) {
        SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
        ObjectUtils.nonNullConsumer(sofaTraceContext.pop(), s -> {
            // finish and to report
            clientReceiveTagFinish(s, resultCode);
            // restore parent span
            ObjectUtils.nonNullConsumer(s.getParentSofaTracerSpan(), sofaTraceContext::push);
        });
    }

    public void clientReceiveTagFinish(SofaTracerSpan clientSpan, String resultCode) {
        ObjectUtils.nonNullConsumer(clientSpan, s -> s.log(LogData.CLIENT_RECEIVE_EVENT_VALUE).setTag(CommonSpanTags.RESULT_CODE, resultCode).finish());
    }

    public SofaTracerSpan serverReceive() {
        return serverReceive(null);
    }

    public SofaTracerSpan serverReceive(SofaTracerSpanContext sofaTracerSpanContext) {
        SofaTracerSpan newSpan = null;
        // pop LogContext
        SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
        SofaTracerSpan serverSpan = sofaTraceContext.pop();
        try {
            if (serverSpan == null) {
                newSpan = (SofaTracerSpan) this.sofaTracer.buildSpan(StringConstants.EMPTY).asChildOf(sofaTracerSpanContext).start();
            } else {
                newSpan = (SofaTracerSpan) this.sofaTracer.buildSpan(StringConstants.EMPTY).asChildOf(serverSpan).start();
            }
        } catch (Throwable throwable) {
            SelfLog.errorWithTraceId("Middleware server received and restart root span", throwable);
            Map<String, String> bizBaggage = null;
            Map<String, String> sysBaggage = null;
            if (serverSpan != null) {
                bizBaggage = serverSpan.getSofaTracerSpanContext().getBizBaggage();
                sysBaggage = serverSpan.getSofaTracerSpanContext().getSysBaggage();
            }
            newSpan = errorRecover(bizBaggage, sysBaggage);
        } finally {
            ObjectUtils.nonNullConsumer(newSpan, s -> {
                s.log(LogData.SERVER_RECEIVE_EVENT_VALUE).setTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER).setTag(CommonSpanTags.CURRENT_THREAD_NAME, Thread.currentThread().getName());
                // push to sofaTraceContext
                sofaTraceContext.push(s);
            });
        }
        return newSpan;
    }

    public void serverSend(String resultCode) {
        try {
            ObjectUtils.nonNullConsumer(SofaTraceContextHolder.getSofaTraceContext().pop(), s -> s.log(LogData.SERVER_SEND_EVENT_VALUE).setTag(CommonSpanTags.RESULT_CODE, resultCode).finish());
        } finally {
            clearTreadLocalContext();
        }
    }

    protected SofaTracerSpan genSeverSpanInstance(long startTime, String operationName, SofaTracerSpanContext sofaTracerSpanContext, Map<String, ?> tags) {
        return new SofaTracerSpan(sofaTracer, startTime, null, operationName, sofaTracerSpanContext, tags);
    }

    /**
     * Clean up all call context information: Note that the server can be cleaned up after receiving it;
     * the client does not have the right time to clean up (can only judge size <= 1)
     */
    private void clearTreadLocalContext() {
        SofaTraceContextHolder.getSofaTraceContext().clear();
    }

    /**
     * When an error occurs to remedy, start counting from the root node
     *
     * @param bizBaggage Business transparent transmission
     * @param sysBaggage System transparent transmission
     * @return root span
     */
    protected SofaTracerSpan errorRecover(Map<String, String> bizBaggage, Map<String, String> sysBaggage) {
        return genSeverSpanInstance(Clock.currentTimeMillis(), StringConstants.EMPTY, SofaTracerSpanContext.rootStart().addBizBaggage(bizBaggage).addSysBaggage(sysBaggage), null);
    }

    public SofaTracer getSofaTracer() {
        return sofaTracer;
    }
}