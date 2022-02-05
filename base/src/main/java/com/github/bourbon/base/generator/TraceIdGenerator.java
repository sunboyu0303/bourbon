package com.github.bourbon.base.generator;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.LocalHostUtils;
import com.github.bourbon.base.utils.PidUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/6 16:06
 */
public class TraceIdGenerator {

    private static AtomicInteger count = new AtomicInteger(1000);

    public static String getTraceId() {
        int nextId = getNextId();
        return LocalHostUtils.ip16() + Clock.currentTimeMillis() + nextId + PidUtils.pid();
    }

    private static int getNextId() {
        for (; ; ) {
            int current = count.get();
            int next = (current > 9000) ? 1000 : current + 1;
            if (count.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}