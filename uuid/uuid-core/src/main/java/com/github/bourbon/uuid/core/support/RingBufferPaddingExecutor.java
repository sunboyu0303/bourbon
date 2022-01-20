package com.github.bourbon.uuid.core.support;

import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;
import com.github.bourbon.uuid.core.lang.RingBuffer;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 16:41
 */
public class RingBufferPaddingExecutor {

    private final Lock lock = new ReentrantLock();
    
    private final RingBuffer ringBuffer;
    private final RingBufferUidProvider provider;
    private final ThreadPoolExecutor executor;

    public RingBufferPaddingExecutor(RingBuffer ringBuffer, RingBufferUidProvider provider, long scheduleInterval) {
        this.ringBuffer = ringBuffer;
        this.provider = provider;
        this.executor = ThreadPoolExecutorFactory.newFixedThreadPool("uuid", 10, new NamedThreadFactory("RingBuffer-Worker", true));
        this.doExec();
        if (scheduleInterval > 0) {
            ExecutorFactory.Managed.newSingleScheduledExecutorService("uuid", new NamedThreadFactory("RingBuffer-Schedule", true)).scheduleWithFixedDelay(this::doExec, scheduleInterval, scheduleInterval, TimeUnit.SECONDS);
        }
    }

    public void asyncPadding() {
        executor.submit(this::doExec);
    }

    private void doExec() {
        if (lock.tryLock()) {
            try {
                boolean isFullRingBuffer = false;
                while (!isFullRingBuffer) {
                    for (Long uid : provider.provide(TimeUnit.MILLISECONDS.toSeconds(SystemClock.currentTimeMillis()))) {
                        isFullRingBuffer = !ringBuffer.put(uid);
                        if (isFullRingBuffer) {
                            break;
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}