package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.ttl.spi.TtlWrapper;

import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 15:34
 */
public interface DisableInheritableForkJoinWorkerThreadFactory extends ForkJoinWorkerThreadFactory, TtlWrapper<ForkJoinWorkerThreadFactory> {

    ForkJoinWorkerThreadFactory unwrap();
}