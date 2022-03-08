package com.github.bourbon.tracer.core.appender.manager;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.base.disruptor.dsl.Disruptor;
import com.github.bourbon.base.disruptor.dsl.ProducerType;
import com.github.bourbon.base.disruptor.exception.InsufficientCapacityException;
import com.github.bourbon.base.disruptor.handler.EventHandler;
import com.github.bourbon.base.disruptor.lang.RingBuffer;
import com.github.bourbon.base.disruptor.strategy.BlockingWaitStrategy;
import com.github.bourbon.base.lang.VolatileObject;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.base.utils.concurrent.atomic.PaddedAtomicLong;
import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.appender.file.LoadTestAwareAppender;
import com.github.bourbon.tracer.core.appender.self.SynchronizingSelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;
import com.github.bourbon.tracer.core.utils.TracerUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 16:00
 */
public class AsyncCommonDigestAppenderManager {

    private final Map<String, TraceAppender> appenders = MapUtils.newConcurrentHashMap();
    private final Map<String, SpanEncoder> contextEncoders = MapUtils.newConcurrentHashMap();

    private final Disruptor<VolatileObject<SofaTracerSpan>> disruptor;
    private RingBuffer<VolatileObject<SofaTracerSpan>> ringBuffer;
    private final ConsumerThreadFactory threadFactory = new ConsumerThreadFactory();

    private List<Consumer> consumers;
    private final AtomicInteger index = new AtomicInteger(0);

    private boolean allowDiscard;
    private boolean isOutDiscardNumber;
    private boolean isOutDiscardId;
    private long discardOutThreshold;
    private PaddedAtomicLong discardCount;

    public AsyncCommonDigestAppenderManager(int queueSize) {
        this(queueSize, 3);
    }

    public AsyncCommonDigestAppenderManager(int queueSize, int consumerNumber) {
        int realQueueSize = 1 << (32 - Integer.numberOfLeadingZeros(queueSize - 1));
        this.disruptor = new Disruptor<>(new SofaTracerSpanEventFactory(), realQueueSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
        this.consumers = ListUtils.newArrayList(consumerNumber);
        for (int i = 0; i < consumerNumber; i++) {
            Consumer consumer = new Consumer();
            consumers.add(consumer);
            disruptor.setDefaultExceptionHandler(new ConsumerExceptionHandler());
            disruptor.handleEventsWith(consumer);
        }

        this.allowDiscard = BooleanUtils.toBoolean(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_ASYNC_APPENDER_ALLOW_DISCARD, "true"));
        if (allowDiscard) {
            this.isOutDiscardNumber = BooleanUtils.toBoolean(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_ASYNC_APPENDER_IS_OUT_DISCARD_NUMBER, "true"));
            this.isOutDiscardId = BooleanUtils.toBoolean(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_ASYNC_APPENDER_IS_OUT_DISCARD_ID, "false"));
            this.discardOutThreshold = Long.parseLong(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_ASYNC_APPENDER_DISCARD_OUT_THRESHOLD, "500"));
            if (isOutDiscardNumber) {
                this.discardCount = new PaddedAtomicLong(0L);
            }
        }
    }

    public AsyncCommonDigestAppenderManager start(final String workerName) {
        this.threadFactory.setWorkName(workerName);
        this.ringBuffer = this.disruptor.start();
        return this;
    }

    public void addAppender(String logType, TraceAppender appender, SpanEncoder encoder) {
        if (isAppenderOrEncoderExist(logType)) {
            SynchronizingSelfLog.error("logType[" + logType + "] already is added AsyncCommonDigestAppenderManager");
            return;
        }
        appenders.put(logType, appender);
        contextEncoders.put(logType, encoder);
        consumers.get(index.incrementAndGet() % consumers.size()).addLogType(logType);
    }

    public boolean isAppenderOrEncoderExist(String logType) {
        return appenders.containsKey(logType) || contextEncoders.containsKey(logType);
    }

    public boolean isAppenderAndEncoderExist(String logType) {
        return appenders.containsKey(logType) && contextEncoders.containsKey(logType);
    }

    public boolean append(SofaTracerSpan sofaTracerSpan) {
        long sequence;
        if (allowDiscard) {
            try {
                sequence = ringBuffer.tryNext();
            } catch (InsufficientCapacityException e) {
                if (isOutDiscardId) {
                    SofaTracerSpanContext sofaTracerSpanContext = sofaTracerSpan.getSofaTracerSpanContext();
                    if (sofaTracerSpanContext != null) {
                        SynchronizingSelfLog.warn("discarded tracer: traceId[" + sofaTracerSpanContext.getTraceId() + "];spanId[" + sofaTracerSpanContext.getSpanId() + "]");
                    }
                }
                if ((isOutDiscardNumber) && discardCount.incrementAndGet() == discardOutThreshold) {
                    discardCount.set(0);
                    if (isOutDiscardNumber) {
                        SynchronizingSelfLog.warn("discarded " + discardOutThreshold + " logs");
                    }
                }
                return false;
            }
        } else {
            sequence = ringBuffer.next();
        }

        try {
            ringBuffer.get(sequence).setValue(sofaTracerSpan);
            ringBuffer.publish(sequence);
            return true;
        } catch (Exception e) {
            SynchronizingSelfLog.error("fail to add event");
            return false;
        }
    }

    private class Consumer implements EventHandler<VolatileObject<SofaTracerSpan>> {

        private Set<String> logTypes = SetUtils.synchronizedSet(SetUtils.newHashSet());

        @Override
        public void onEvent(VolatileObject<SofaTracerSpan> event, long sequence, boolean endOfBatch) {
            SofaTracerSpan sofaTracerSpan = event.getValue();
            if (sofaTracerSpan != null) {
                try {
                    String logType = sofaTracerSpan.getLogType();
                    if (logTypes.contains(logType)) {
                        TraceAppender appender = appenders.get(logType);
                        String encodedStr = contextEncoders.get(logType).encode(sofaTracerSpan);
                        if (appender instanceof LoadTestAwareAppender) {
                            ((LoadTestAwareAppender) appender).append(encodedStr, TracerUtils.isLoadTest(sofaTracerSpan));
                        } else {
                            appender.append(encodedStr);
                        }
                        appender.flush();
                    }
                } catch (Exception e) {
                    SofaTracerSpanContext sofaTracerSpanContext = sofaTracerSpan.getSofaTracerSpanContext();
                    if (sofaTracerSpanContext != null) {
                        SynchronizingSelfLog.error("fail to async write log, tracerId[" + sofaTracerSpanContext.getTraceId() + "];spanId[" + sofaTracerSpanContext.getSpanId() + "].", e);
                    } else {
                        SynchronizingSelfLog.error("fail to async write log. And the sofaTracerSpanContext is null.", e);
                    }
                }
            }
        }

        private void addLogType(String logType) {
            logTypes.add(logType);
        }
    }
}