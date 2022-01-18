package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.ttl.spi.TtlWrapper;

import java.util.concurrent.ThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 11:42
 */
public interface DisableInheritableThreadFactory extends ThreadFactory, TtlWrapper<ThreadFactory> {
    
    ThreadFactory unwrap();
}