package com.github.bourbon.base.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 18:09
 */
public interface ThreadPool {

    ThreadPoolExecutor getThreadPoolExecutor(ThreadPoolParams params);
}