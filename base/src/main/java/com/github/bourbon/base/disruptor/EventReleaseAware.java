package com.github.bourbon.base.disruptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 10:28
 */
public interface EventReleaseAware {
    
    void setEventReleaser(EventReleaser eventReleaser);
}