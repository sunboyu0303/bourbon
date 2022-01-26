package com.github.bourbon.base.tracer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/26 11:16
 */
public interface TracerIdRetriever {

    String getTracerId(Thread thread);
}