package com.github.bourbon.base.lang.statistic.metric;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 16:57
 */
public interface Metric extends DebugSupport {

    long success();

    long maxSuccess();

    long exception();

    long block();

    long pass();

    long rt();

    long minRt();
}