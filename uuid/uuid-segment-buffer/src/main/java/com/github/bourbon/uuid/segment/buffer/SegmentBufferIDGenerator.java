package com.github.bourbon.uuid.segment.buffer;

import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.uuid.core.IDGenerator;
import com.github.bourbon.uuid.core.lang.Segment;
import com.github.bourbon.uuid.core.lang.SegmentBuffer;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 18:00
 */
public class SegmentBufferIDGenerator implements IDGenerator {

    private static final int MIN_STEP = 50_000;

    private static final int MAX_STEP = 1_000_000;

    private static final long MIN_SEGMENT_DURATION = TimeUnit.MINUTES.toMillis(15);

    private static final long MAX_SEGMENT_DURATION = 2 * MIN_SEGMENT_DURATION;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new NamedThreadFactory("SegmentBufferIDGenerator", true));

    @Inject
    private SegmentBufferService segmentBufferService;

    private final SegmentBuffer buffer = new SegmentBuffer(s -> {
        SegmentBuffer b = s.getBuffer();
        if (b.getUpdateTimestamp() == 0) {
            b.setStep(MIN_STEP);
        } else {
            long duration = SystemClock.currentTimeMillis() - b.getUpdateTimestamp();
            long nextStep = b.getStep();
            if (duration < MIN_SEGMENT_DURATION) {
                nextStep <<= 1;
                if (nextStep > MAX_STEP) {
                    nextStep = MAX_STEP;
                }
            } else if (duration >= MAX_SEGMENT_DURATION) {
                nextStep >>= 1;
                if (nextStep < MIN_STEP) {
                    nextStep = MIN_STEP;
                }
            }
            b.setStep(nextStep);
        }
        b.setUpdateTimestamp(SystemClock.currentTimeMillis());
        long max = segmentBufferService.getMaxId(b.getStep());
        long min = max - b.getStep();
        s.setValue(min);
        s.setMin(min);
        s.setMax(max);
    });

    @Override
    public long getId() {
        while (true) {
            final Segment segment = buffer.getCurrent();
            if (!buffer.isReady() && segment.refresh() && buffer.setRunning()) {
                executor.execute(() -> {
                    try {
                        buffer.getHandler().updateSegment(buffer.getNext());
                        buffer.setReady();
                    } finally {
                        buffer.resetRunning();
                    }
                });
            }
            long value = segment.getAndIncrement();
            if (value < segment.getMax()) {
                return value;
            }
            if (!buffer.isRunning()) {
                if (buffer.isReady()) {
                    try {
                        buffer.lock();
                        if (buffer.isReady()) {
                            buffer.switchPos();
                            buffer.resetReady();
                        }
                    } finally {
                        buffer.unlock();
                    }
                }
            }
        }
    }
}