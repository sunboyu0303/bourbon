package com.github.bourbon.uuid.core.lang;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.concurrent.atomic.PaddedAtomicLong;
import com.github.bourbon.uuid.core.support.RingBufferPaddingExecutor;
import com.github.bourbon.uuid.core.support.RingBufferRejectedPutHandler;
import com.github.bourbon.uuid.core.support.RingBufferRejectedTakeHandler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 16:28
 */
public class RingBuffer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int START_POINT = -1;
    private static final long CAN_PUT_FLAG = 0L;
    private static final long CAN_TAKE_FLAG = 1L;

    private final AtomicLong tail = new PaddedAtomicLong(START_POINT);
    private final AtomicLong cursor = new PaddedAtomicLong(START_POINT);
    private final int bufferSize;
    private final int indexMask;
    private final int paddingThreshold;
    private final long[] slots;
    private final PaddedAtomicLong[] flags;

    private RingBufferRejectedPutHandler ringBufferRejectedPutHandler = (r, u) ->
            log.error("Rejected putting buffer: " + r + " for uid: " + u);

    private RingBufferRejectedTakeHandler ringBufferRejectedTakeHandler = r -> {
        throw new Exception("Rejected take buffer: " + r);
    };

    private RingBufferPaddingExecutor ringBufferPaddingExecutor;

    public RingBuffer(int bufferSize, int paddingFactor) {
        this(bufferSize, paddingFactor, null, null);
    }

    public RingBuffer(int bufferSize, int paddingFactor, RingBufferRejectedPutHandler putHandler, RingBufferRejectedTakeHandler takeHandler) {

        IntUtils.checkPositive(bufferSize, "bufferSize greater than to zero!");
        IntUtils.checkInRange(paddingFactor, 0, 100, "paddingFactor");
        Assert.isTrue(Integer.bitCount(bufferSize) == 1, "bufferSize must be a power of 2");

        this.bufferSize = bufferSize;
        indexMask = bufferSize - 1;
        paddingThreshold = bufferSize * paddingFactor / 100;
        slots = new long[bufferSize];

        PaddedAtomicLong[] tmp = new PaddedAtomicLong[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            tmp[i] = new PaddedAtomicLong(CAN_PUT_FLAG);
        }
        flags = tmp;

        setRingBufferRejectedPutHandler(putHandler);
        setRingBufferRejectedTakeHandler(takeHandler);
    }

    public RingBuffer setRingBufferRejectedPutHandler(RingBufferRejectedPutHandler putHandler) {
        if (!ObjectUtils.isNull(putHandler)) {
            ringBufferRejectedPutHandler = putHandler;
        }
        return this;
    }

    public RingBuffer setRingBufferRejectedTakeHandler(RingBufferRejectedTakeHandler takeHandler) {
        if (!ObjectUtils.isNull(takeHandler)) {
            ringBufferRejectedTakeHandler = takeHandler;
        }
        return this;
    }

    public RingBuffer setRingBufferPaddingExecutor(RingBufferPaddingExecutor executor) {
        ringBufferPaddingExecutor = ObjectUtils.requireNonNull(executor, "executor is not null!");
        return this;
    }

    public synchronized boolean put(long uid) {
        long currentTail = tail.get();
        long currentCursor = cursor.get();
        long distance = currentTail - (currentCursor == START_POINT ? 0 : currentCursor);
        if (distance == bufferSize - 1) {
            ringBufferRejectedPutHandler.reject(this, uid);
            return false;
        }
        int nextTailIndex = (int) ((currentTail + 1) & indexMask);
        if (flags[nextTailIndex].get() != CAN_PUT_FLAG) {
            ringBufferRejectedPutHandler.reject(this, uid);
            return false;
        }
        slots[nextTailIndex] = uid;
        flags[nextTailIndex].set(CAN_TAKE_FLAG);
        tail.incrementAndGet();
        return true;
    }

    public long take() throws Exception {
        long currentCursor = cursor.get();
        long nextCursor = cursor.updateAndGet(p -> p == tail.get() ? p : p + 1);
        Assert.isTrue(nextCursor >= currentCursor, "Cursor can't move back");
        long currentTail = tail.get();
        if (currentTail - nextCursor < paddingThreshold) {
            ringBufferPaddingExecutor.asyncPadding();
        }
        if (nextCursor == currentCursor) {
            ringBufferRejectedTakeHandler.reject(this);
        }
        int nextCursorIndex = (int) (nextCursor & indexMask);
        Assert.isTrue(flags[nextCursorIndex].get() == CAN_TAKE_FLAG, "Cursor not in can take status");
        long uid = slots[nextCursorIndex];
        flags[nextCursorIndex].set(CAN_PUT_FLAG);
        return uid;
    }
}