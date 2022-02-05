package com.github.bourbon.tracer.core.appender.manager;

import com.github.bourbon.base.constant.StringConstants;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 14:24
 */
public class ConsumerThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private String workName;

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread worker = new Thread(runnable, "Tracer-AsyncConsumer-Thread-" + workName + StringConstants.HYPHEN + threadNumber.getAndIncrement());
        worker.setDaemon(true);
        return worker;
    }
}