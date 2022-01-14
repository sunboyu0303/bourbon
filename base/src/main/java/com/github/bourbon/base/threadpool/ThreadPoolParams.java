package com.github.bourbon.base.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 18:03
 */
public class ThreadPoolParams {

    private final Integer corePoolSize;
    private final Integer maximumPoolSize;
    private final Long keepAliveTime;
    private final TimeUnit unit;
    private final Integer queueSize;
    private final String name;

    public ThreadPoolParams(Integer corePoolSize,
                            Integer maximumPoolSize,
                            Long keepAliveTime,
                            TimeUnit unit,
                            Integer queueSize,
                            String name) {
        
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.queueSize = queueSize;
        this.name = name;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public String getName() {
        return name;
    }
}