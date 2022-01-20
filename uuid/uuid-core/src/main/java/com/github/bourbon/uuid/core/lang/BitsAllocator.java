package com.github.bourbon.uuid.core.lang;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.IntUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 15:42
 */
public class BitsAllocator {

    private static final int TOTAL_BITS = 1 << 6;
    private static final int SIGN_BITS = 1;

    private final long maxTimestamp;
    private final long maxWorkerId;
    private final long maxSequence;

    private final int timestampShift;
    private final int workerIdShift;

    public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {

        IntUtils.checkPositive(timestampBits, "timestampBits greater than to zero!");
        IntUtils.checkPositive(workerIdBits, "workerIdBits greater than to zero!");
        IntUtils.checkPositive(sequenceBits, "sequenceBits greater than to zero!");

        int allocateTotalBits = SIGN_BITS + timestampBits + workerIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        this.maxTimestamp = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        this.timestampShift = workerIdBits + sequenceBits;
        this.workerIdShift = sequenceBits;
    }

    public long allocate(long t, long w) {
        return allocate(t, w, 0L);
    }

    public long allocate(long t, long w, long s) {
        return (t << timestampShift) | (w << workerIdShift) | s;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public long getMaxWorkerId() {
        return maxWorkerId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }
}