package com.github.bourbon.ump.domain;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;
import com.github.bourbon.ump.LogHandler;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 17:31
 */
public class MessagesStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Lock lock = new ReentrantLock();
    private final Condition readSignal = lock.newCondition();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Queue<Object> messages = new ConcurrentLinkedQueue<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    private final LogHandler handler;
    private final int capacity;
    private final ThreadPoolExecutor executor;

    public MessagesStore(LogHandler handler, int capacity, String name) {
        this.handler = handler;
        this.capacity = capacity;
        this.executor = ThreadPoolExecutorFactory.newFixedThreadPool("ump", 1, new NamedThreadFactory("MessagesStore" + name, true));
        this.executor.submit(new LogHandle());
    }

    public boolean storeMsg(Object message) {
        if (counter.get() < capacity) {
            messages.offer(message);
            counter.incrementAndGet();
            if (lock.tryLock()) {
                readSignal.signalAll();
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public void close() {
        if (closed.compareAndSet(false, true)) {
            executor.shutdown();
        }
    }

    private class LogHandle implements Runnable {
        private int pollCount = 0;
        private List<String> buffer = ListUtils.newArrayList();

        @Override
        public void run() {
            while (!closed.get()) {
                try {
                    ++pollCount;
                    Object msg = messages.poll();
                    if (msg != null) {
                        counter.decrementAndGet();
                        buffer.add((String) msg);
                    } else if (buffer.isEmpty()) {
                        readSignal.await();
                    } else {
                        TimeUnitUtils.sleepMilliSeconds(1L);
                    }

                    if (pollCount >= 64) {
                        pollCount = 0;
                        try {
                            handler.handle(buffer);
                        } catch (Exception t) {
                            if (log.isDebugEnabled()) {
                                log.debug("Ump monitor data process error!", t);
                            }
                        } finally {
                            buffer.clear();
                        }
                    }
                } catch (Exception t) {
                    if (log.isDebugEnabled()) {
                        log.debug("Ump monitor data process error!", t);
                    }
                    TimeUnitUtils.sleepMilliSeconds(1L);
                }
            }
        }
    }
}