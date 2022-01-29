package com.github.bourbon.base.thread.space;

import com.github.bourbon.base.utils.SetUtils;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 16:12
 */
public final class ThreadPoolSpace {

    private final Set<String> threadPoolIdentities = SetUtils.synchronizedSet(SetUtils.newHashSet());

    private final AtomicInteger threadPoolNumber = new AtomicInteger(0);

    public void addThreadPool(String identify) {
        this.threadPoolIdentities.add(identify);
    }

    public void removeThreadPool(String identify) {
        this.threadPoolIdentities.remove(identify);
    }

    public Set<String> getThreadPoolIdentities() {
        return threadPoolIdentities;
    }

    public int getThreadPoolNumber() {
        return threadPoolNumber.getAndIncrement();
    }
}