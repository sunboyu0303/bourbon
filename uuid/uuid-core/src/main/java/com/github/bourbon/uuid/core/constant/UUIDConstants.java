package com.github.bourbon.uuid.core.constant;

import com.github.bourbon.base.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 16:05
 */
public final class UUIDConstants {

    public static final LocalDateTime DEFAULT_LOCAL_DATE_TIME = LocalDateTimeUtils.localDateTime(2020, 11, 1, 0, 0);
    public static final long DEFAULT_EPOCH_SECONDS = LocalDateTimeUtils.toSeconds(DEFAULT_LOCAL_DATE_TIME);
    public static final long DEFAULT_EPOCH_MILLIS = LocalDateTimeUtils.toMillis(DEFAULT_LOCAL_DATE_TIME);

    public static final String UUID_PROPERTIES_PATH = "META-INF/uuid.properties";

    public static final String SNOWFLAKE_TIMESTAMP_BITS = "snowflake.timestamp.bits";
    public static final String SNOWFLAKE_WORKER_BITS = "snowflake.worker.bits";
    public static final String SNOWFLAKE_SEQUENCE_BITS = "snowflake.sequence.bits";
    public static final String SNOWFLAKE_EPOCH = "snowflake.epoch";

    public static final String SNOWFLAKE_TWITTER_TIMESTAMP_BITS = "snowflake.twitter.timestamp.bits";
    public static final String SNOWFLAKE_TWITTER_WORKER_BITS = "snowflake.twitter.worker.bits";
    public static final String SNOWFLAKE_TWITTER_SEQUENCE_BITS = "snowflake.twitter.sequence.bits";
    public static final String SNOWFLAKE_TWITTER_EPOCH = "snowflake.twitter.epoch";

    public static final String SNOWFLAKE_RING_BUFFER_TIMESTAMP_BITS = "snowflake.ring.buffer.timestamp.bits";
    public static final String SNOWFLAKE_RING_BUFFER_WORKER_BITS = "snowflake.ring.buffer.worker.bits";
    public static final String SNOWFLAKE_RING_BUFFER_SEQUENCE_BITS = "snowflake.ring.buffer.sequence.bits";
    public static final String SNOWFLAKE_RING_BUFFER_EPOCH = "snowflake.ring.buffer.epoch";
    public static final String SNOWFLAKE_RING_BUFFER_BOOST_POWER = "snowflake.ring.buffer.boost.power";
    public static final String SNOWFLAKE_RING_BUFFER_SCHEDULE_INTERVAL = "snowflake.ring.buffer.schedule.interval";
    public static final String SNOWFLAKE_RING_BUFFER_PADDING_FACTOR = "snowflake.ring.buffer.padding.factor";

    private UUIDConstants() {
    }
}