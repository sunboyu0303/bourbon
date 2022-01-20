package com.github.bourbon.uuid.snowflake;

import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.LongUtils;
import com.github.bourbon.uuid.core.IDGenerator;
import com.github.bourbon.uuid.core.constant.UUIDConstants;
import com.github.bourbon.uuid.core.lang.BitsAllocator;
import com.github.bourbon.uuid.core.UUIDProperties;

import java.util.concurrent.TimeUnit;

/**
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          31bits              14bits         18bits
 * }</pre>
 *
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 14:27
 */
public class SnowflakeIDGenerator implements IDGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeIDGenerator.class);

    private final int timestampBits;

    private final int workerIdBits;

    private final int sequenceBits;

    protected final long epoch;

    protected BitsAllocator bitsAllocator;

    protected long workerId;

    private volatile long sequence = 0L;

    private volatile long lastTimestamp = -1L;

    @Inject
    protected SnowflakeService snowflakeService;

    public SnowflakeIDGenerator() {
        this(
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_TIMESTAMP_BITS, 31),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_WORKER_BITS, 14),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_SEQUENCE_BITS, 18),
                UUIDProperties.getLong(UUIDConstants.SNOWFLAKE_EPOCH, UUIDConstants.DEFAULT_EPOCH_SECONDS)
        );
    }

    public SnowflakeIDGenerator(int timestampBits, int workerIdBits, int sequenceBits, long epoch) {
        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
        this.sequenceBits = sequenceBits;
        this.epoch = epoch;
        initialize();
    }

    private void initialize() {
        LongUtils.checkPositive(epoch, "epoch");
        bitsAllocator = new BitsAllocator(timestampBits, workerIdBits, sequenceBits);
        workerId = snowflakeService.assignWorkerId();
        long maxWorkerId = bitsAllocator.getMaxWorkerId();
        Assert.isTrue(workerId <= maxWorkerId, "Worker id: %d, exceeds the max: %d", workerId, maxWorkerId);
        doInitialize();
    }

    protected void doInitialize() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("SnowflakeIDGenerator doInitialize");
        }
    }

    @Override
    public long getId() throws Exception {
        synchronized (this) {
            long timestamp = getCurrentTimestamp();
            Assert.isTrue(timestamp >= lastTimestamp, "Clock moved backwards.");
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
                if (sequence == 0) {
                    timestamp = getNextTimestamp();
                }
            } else {
                sequence = 0L;
            }
            lastTimestamp = timestamp;
            return bitsAllocator.allocate(timestamp - epoch, workerId, sequence);
        }
    }

    private long getNextTimestamp() {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }

    private long getCurrentTimestamp() {
        // current second
        long timestamp = getTimestamp();
        Assert.isTrue((timestamp - epoch) <= bitsAllocator.getMaxTimestamp(), "Timestamp bits is exhausted. Refusing UID generate.");
        return timestamp;
    }

    protected long getTimestamp() {
        return TimeUnit.MILLISECONDS.toSeconds(SystemClock.currentTimeMillis());
    }
}