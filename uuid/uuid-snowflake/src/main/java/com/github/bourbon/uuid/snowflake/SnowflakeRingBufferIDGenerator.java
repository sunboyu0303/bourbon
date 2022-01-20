package com.github.bourbon.uuid.snowflake;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.uuid.core.UUIDProperties;
import com.github.bourbon.uuid.core.constant.UUIDConstants;
import com.github.bourbon.uuid.core.lang.RingBuffer;
import com.github.bourbon.uuid.core.support.RingBufferPaddingExecutor;
import com.github.bourbon.uuid.core.support.RingBufferRejectedPutHandler;
import com.github.bourbon.uuid.core.support.RingBufferRejectedTakeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              22bits         13bits
 * }</pre>
 *
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 14:27
 */
public class SnowflakeRingBufferIDGenerator extends SnowflakeIDGenerator {

    private int boostPower;
    private long scheduleInterval;
    private int paddingFactor;
    private RingBufferRejectedPutHandler ringBufferRejectedPutHandler;
    private RingBufferRejectedTakeHandler ringBufferRejectedTakeHandler;

    private RingBuffer ringBuffer;

    public SnowflakeRingBufferIDGenerator() {
        this(
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_RING_BUFFER_TIMESTAMP_BITS, 28),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_RING_BUFFER_WORKER_BITS, 22),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_RING_BUFFER_SEQUENCE_BITS, 13),
                UUIDProperties.getLong(UUIDConstants.SNOWFLAKE_RING_BUFFER_EPOCH, UUIDConstants.DEFAULT_EPOCH_SECONDS),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_RING_BUFFER_BOOST_POWER, 3),
                UUIDProperties.getLong(UUIDConstants.SNOWFLAKE_RING_BUFFER_SCHEDULE_INTERVAL, 0),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_RING_BUFFER_PADDING_FACTOR, 50)
        );
    }

    public SnowflakeRingBufferIDGenerator(int timestampBits, int workerIdBits, int sequenceBits, long epoch, int boostPower, long scheduleInterval, int paddingFactor) {
        this(timestampBits, workerIdBits, sequenceBits, epoch, boostPower, scheduleInterval, paddingFactor, null, null);
    }

    public SnowflakeRingBufferIDGenerator(int timestampBits, int workerIdBits, int sequenceBits, long epoch, int boostPower, long scheduleInterval, int paddingFactor, RingBufferRejectedPutHandler putHandler, RingBufferRejectedTakeHandler takeHandler) {
        super(timestampBits, workerIdBits, sequenceBits, epoch);
        this.boostPower = boostPower;
        this.scheduleInterval = scheduleInterval;
        this.paddingFactor = paddingFactor;
        ringBufferRejectedPutHandler = putHandler;
        ringBufferRejectedTakeHandler = takeHandler;
    }

    @Override
    protected void doInitialize() {
        Assert.isTrue(boostPower > 0, "boostPower greater than to zero!");
        ringBuffer = new RingBuffer(((int) bitsAllocator.getMaxSequence() + 1) << boostPower, paddingFactor, ringBufferRejectedPutHandler, ringBufferRejectedTakeHandler)
                .setRingBufferPaddingExecutor(new RingBufferPaddingExecutor(ringBuffer, timestamp -> {
                    int listSize = (int) bitsAllocator.getMaxSequence() + 1;
                    List<Long> uidList = new ArrayList<>(listSize);
                    long firstSeqUid = bitsAllocator.allocate(timestamp - epoch, workerId);
                    for (int offset = 0; offset < listSize; offset++) {
                        uidList.add(firstSeqUid + offset);
                    }
                    return uidList;
                }, scheduleInterval));
    }

    @Override
    public long getId() throws Exception {
        return ringBuffer.take();
    }
}