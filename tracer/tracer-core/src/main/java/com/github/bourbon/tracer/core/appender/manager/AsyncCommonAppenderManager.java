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
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.concurrent.atomic.PaddedAtomicLong;
import com.github.bourbon.tracer.core.appender.file.TimedRollingFileAppender;
import com.github.bourbon.tracer.core.appender.self.SynchronizingSelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 13:03
 */
public class AsyncCommonAppenderManager {

    private final TraceAppender appender;
    private final Disruptor<VolatileObject<String>> disruptor;
    private RingBuffer<VolatileObject<String>> ringBuffer;
    private final ConsumerThreadFactory threadFactory = new ConsumerThreadFactory();

    private final List<Consumer> consumers;

    private final boolean allowDiscard;
    private boolean isOutDiscardNumber;
    private boolean isOutDiscardId;
    private long discardOutThreshold;
    private PaddedAtomicLong discardCount;

    public AsyncCommonAppenderManager(int queueSize, String logName) {
        this(queueSize, 1, logName);
    }

    public AsyncCommonAppenderManager(int queueSize, int consumerNumber, String logName) {
        int realQueueSize = 1 << (32 - Integer.numberOfLeadingZeros(queueSize - 1));
        this.disruptor = new Disruptor<>(new StringEventFactory(), realQueueSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
        this.consumers = ListUtils.newArrayList(consumerNumber);

        for (int i = 0; i < consumerNumber; i++) {
            Consumer consumer = new Consumer();
            consumers.add(consumer);
            disruptor.setDefaultExceptionHandler(new StringConsumerExceptionHandler());
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

        this.appender = new TimedRollingFileAppender(logName,
                BooleanUtils.defaultIfPredicate(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_GLOBAL_ROLLING_KEY), CharSequenceUtils::isNotBlank, s -> s, TimedRollingFileAppender.DAILY_ROLLING_PATTERN),
                SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_GLOBAL_LOG_RESERVE_DAY, String.valueOf(SofaTracerConfiguration.DEFAULT_LOG_RESERVE_DAY))
        );
    }

    public AsyncCommonAppenderManager start(final String workerName) {
        this.threadFactory.setWorkName(workerName);
        this.ringBuffer = disruptor.start();
        return this;
    }

    public boolean append(String string) {
        long sequence;
        if (allowDiscard) {
            try {
                sequence = ringBuffer.tryNext();
            } catch (InsufficientCapacityException e) {
                if (isOutDiscardId) {
                    if (string != null) {
                        SynchronizingSelfLog.warn("discarded self log");
                    }
                }
                if (isOutDiscardNumber && discardCount.incrementAndGet() == discardOutThreshold) {
                    discardCount.set(0);
                    SynchronizingSelfLog.warn("discarded " + discardOutThreshold + " self logs");
                }
                return false;
            }
        } else {
            sequence = ringBuffer.next();
        }
        try {
            ringBuffer.get(sequence).setValue(string);
            ringBuffer.publish(sequence);
            return true;
        } catch (Exception e) {
            SynchronizingSelfLog.error("fail to add event");
            return false;
        }
    }

    private class Consumer implements EventHandler<VolatileObject<String>> {

        @Override
        public void onEvent(VolatileObject<String> event, long sequence, boolean endOfBatch) {
            String string = event.getValue();
            if (string != null) {
                try {
                    appender.append(string);
                    appender.flush();
                } catch (Exception e) {
                    SynchronizingSelfLog.error("fail to async write log", e);
                }
            }
        }
    }
}