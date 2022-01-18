package com.github.bourbon.base.threadpool.event;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 16:34
 */
public interface ThreadPoolExhaustedListener {
    
    void onEvent(ThreadPoolExhaustedEvent event);
}