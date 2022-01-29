package com.github.bourbon.base.thread;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 14:35
 */
public class ThreadPoolConfig {

    private String threadPoolName;

    private String spaceName;

    private long taskTimeout;

    private long period;

    private TimeUnit timeUnit;

    public static SofaThreadConfigBuilder newBuilder() {
        return new SofaThreadConfigBuilder();
    }

    private ThreadPoolConfig(SofaThreadConfigBuilder builder) {
        this.threadPoolName = builder.threadPoolName;
        this.spaceName = builder.spaceName;
        this.taskTimeout = BooleanUtils.defaultIfPredicate(builder.taskTimeout, t -> t != 0, t -> t, SofaThreadPoolConstants.DEFAULT_TASK_TIMEOUT);
        this.period = BooleanUtils.defaultIfPredicate(builder.period, t -> t != 0, t -> t, SofaThreadPoolConstants.DEFAULT_PERIOD);
        this.timeUnit = ObjectUtils.defaultIfNull(builder.timeUnit, TimeUnit.MILLISECONDS);
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public ThreadPoolConfig setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
        return this;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public ThreadPoolConfig setSpaceName(String spaceName) {
        this.spaceName = spaceName;
        return this;
    }

    public long getTaskTimeout() {
        return taskTimeout;
    }

    public void setTaskTimeout(long taskTimeout) {
        this.taskTimeout = taskTimeout;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public long getTaskTimeoutMilli() {
        return this.timeUnit.toMillis(this.taskTimeout);
    }

    public String getIdentity() {
        return buildIdentity(this.threadPoolName, this.spaceName);
    }

    private static String buildIdentity(String threadPoolName, String spaceName) {
        return BooleanUtils.defaultIfPredicate(spaceName, n -> !CharSequenceUtils.isEmpty(n), n -> n + StringConstants.HYPHEN + threadPoolName, threadPoolName);
    }

    public static final class SofaThreadConfigBuilder {
        private String threadPoolName;
        private String spaceName;
        private long taskTimeout;
        private long period;
        private TimeUnit timeUnit;

        private SofaThreadConfigBuilder() {
        }

        public SofaThreadConfigBuilder threadPoolName(String threadPoolName) {
            this.threadPoolName = threadPoolName;
            return this;
        }

        public SofaThreadConfigBuilder spaceName(String spaceName) {
            this.spaceName = spaceName;
            return this;
        }

        public SofaThreadConfigBuilder taskTimeout(long taskTimeout) {
            this.taskTimeout = taskTimeout;
            return this;
        }

        public SofaThreadConfigBuilder period(long period) {
            this.period = period;
            return this;
        }

        public SofaThreadConfigBuilder timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public ThreadPoolConfig build() {
            return new ThreadPoolConfig(this);
        }
    }
}