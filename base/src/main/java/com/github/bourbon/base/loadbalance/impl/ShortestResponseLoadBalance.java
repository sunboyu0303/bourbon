package com.github.bourbon.base.loadbalance.impl;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.loadbalance.*;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 15:49
 */
public class ShortestResponseLoadBalance extends AbstractLoadBalance implements ILoadBalance {

    private final ConcurrentMap<Status, SlideWindowData> methodMap = new ConcurrentHashMap<>();

    private final AtomicBoolean onResetSlideWindow = new AtomicBoolean(false);

    private volatile long lastUpdateTime = Clock.currentTimeMillis();

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, Invocation invocation) {
        int length = invokers.size();
        // Estimated shortest response time of all invokers
        long shortestResponse = Long.MAX_VALUE;
        // The number of invokers having the same estimated shortest response time
        int shortestCount = 0;
        // The index of invokers having the same estimated shortest response time
        int[] shortestIndexes = new int[length];
        // the weight of every invokers
        int[] weights = new int[length];
        // The sum of the warmup weights of all the shortest response  invokers
        int totalWeight = 0;
        // The weight of the first shortest response invokers
        int firstWeight = 0;
        // Every shortest response invoker has the same weight value?
        boolean sameWeight = true;

        // Filter out all the shortest response invokers
        for (int i = 0; i < length; i++) {
            Invoker<T> invoker = invokers.get(i);
            // Calculate the estimated response time from the product of active connections and succeeded average elapsed time.
            long estimateResponse = methodMap.computeIfAbsent(Status.getStatus(invoker), SlideWindowData::new).getEstimateResponse();
            int afterWarmUp = invoker.getWeight();
            weights[i] = afterWarmUp;
            // Same as LeastActiveLoadBalance
            if (estimateResponse < shortestResponse) {
                shortestResponse = estimateResponse;
                shortestCount = 1;
                shortestIndexes[0] = i;
                totalWeight = afterWarmUp;
                firstWeight = afterWarmUp;
                sameWeight = true;
            } else if (estimateResponse == shortestResponse) {
                shortestIndexes[shortestCount++] = i;
                totalWeight += afterWarmUp;
                if (sameWeight && i > 0 && afterWarmUp != firstWeight) {
                    sameWeight = false;
                }
            }
        }

        if (Clock.currentTimeMillis() - lastUpdateTime > 30_000L && onResetSlideWindow.compareAndSet(false, true)) {
            // reset slideWindowData in async way
            SlideWindowData.executor.execute(() -> {
                methodMap.values().forEach(SlideWindowData::reset);
                lastUpdateTime = Clock.currentTimeMillis();
                onResetSlideWindow.set(false);
            });
        }

        if (shortestCount == 1) {
            return invokers.get(shortestIndexes[0]);
        }

        if (!sameWeight && totalWeight > 0) {
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < shortestCount; i++) {
                int shortestIndex = shortestIndexes[i];
                offsetWeight -= weights[shortestIndex];
                if (offsetWeight < 0) {
                    return invokers.get(shortestIndex);
                }
            }
        }
        return invokers.get(shortestIndexes[ThreadLocalRandom.current().nextInt(shortestCount)]);
    }

    private static class SlideWindowData {
        private static final ExecutorService executor = ThreadPoolExecutorFactory.newFixedThreadPool("base", 1, new NamedThreadFactory("slidePeriod-reset"));
        private long succeededOffset = 0L;
        private long succeededElapsedOffset = 0L;
        private Status status;

        private SlideWindowData(Status status) {
            this.status = status;
        }

        private void reset() {
            this.succeededOffset = status.getSucceeded();
            this.succeededElapsedOffset = status.getSucceededElapsed();
        }

        private long getSucceededAverageElapsed() {
            return BooleanUtils.defaultIfPredicate(status.getSucceeded() - succeededOffset, s -> s != 0, s -> (status.getSucceededElapsed() - succeededElapsedOffset) / s, 0L);
        }

        private long getEstimateResponse() {
            return getSucceededAverageElapsed() * (status.getActive() + 1);
        }
    }
}