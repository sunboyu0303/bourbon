package com.github.bourbon.uuid.snowflake;

import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.uuid.core.constant.UUIDConstants;
import com.github.bourbon.uuid.core.UUIDProperties;

/**
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |   delta millSeconds  | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          41bits             10bits         12bits
 * }</pre>
 *
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 14:28
 */
public class TwitterSnowflakeIDGenerator extends SnowflakeIDGenerator {

    public TwitterSnowflakeIDGenerator() {
        this(
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_TWITTER_TIMESTAMP_BITS, 41),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_TWITTER_WORKER_BITS, 10),
                UUIDProperties.getInteger(UUIDConstants.SNOWFLAKE_TWITTER_SEQUENCE_BITS, 12),
                UUIDProperties.getLong(UUIDConstants.SNOWFLAKE_TWITTER_EPOCH, UUIDConstants.DEFAULT_EPOCH_MILLIS)
        );
    }

    public TwitterSnowflakeIDGenerator(int timestampBits, int workerIdBits, int sequenceBits, long epoch) {
        super(timestampBits, workerIdBits, sequenceBits, epoch);
    }

    @Override
    protected long getTimestamp() {
        return SystemClock.currentTimeMillis();
    }
}