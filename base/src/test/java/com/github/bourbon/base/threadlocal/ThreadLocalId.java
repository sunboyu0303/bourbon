package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/26 23:47
 */
public class ThreadLocalId {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalId.class);

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(nextId::getAndIncrement);

    public static int get() {
        return threadId.get();
    }

    public static void remove() {
        threadId.remove();
    }

    private static void incrementSameThreadId() {
        try {
            for (int i = 0; i < 5; i++) {
                LOGGER.info(Thread.currentThread() + StringConstants.UNDERLINE + i + ",threadId:" + get());
            }
        } finally {
            remove();
        }
    }

    public static void main(String[] args) {
        incrementSameThreadId();
        new Thread(ThreadLocalId::incrementSameThreadId).start();
        new Thread(ThreadLocalId::incrementSameThreadId).start();
    }
}