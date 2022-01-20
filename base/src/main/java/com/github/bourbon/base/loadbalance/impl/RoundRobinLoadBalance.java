package com.github.bourbon.base.loadbalance.impl;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.loadbalance.AbstractLoadBalance;
import com.github.bourbon.base.loadbalance.ILoadBalance;
import com.github.bourbon.base.loadbalance.Invocation;
import com.github.bourbon.base.loadbalance.Invoker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/3 15:04
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance implements ILoadBalance {

    private ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> methodWeightMap = new ConcurrentHashMap<>();

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, Invocation invocation) {
        ConcurrentMap<String, WeightedRoundRobin> map = methodWeightMap.computeIfAbsent(invocation.getServiceKey(), k -> new ConcurrentHashMap<>());

        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = Clock.currentTimeMillis();
        Invoker<T> selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;

        for (Invoker<T> invoker : invokers) {
            int weight = invoker.getWeight();
            WeightedRoundRobin weightedRoundRobin = map.computeIfAbsent(invoker.getIdentity(), k -> new WeightedRoundRobin().setWeight(weight));
            if (weight != weightedRoundRobin.getWeight()) {
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                selectedInvoker = invoker;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }

        if (invokers.size() != map.size()) {
            map.entrySet().removeIf(i -> now - i.getValue().getLastUpdate() > 60_000L);
        }

        if (selectedInvoker != null) {
            selectedWRR.sel(totalWeight);
            return selectedInvoker;
        }

        return invokers.get(0);
    }

    private static class WeightedRoundRobin {
        private final AtomicLong current = new AtomicLong(0);
        private volatile int weight;
        private volatile long lastUpdate;

        private int getWeight() {
            return weight;
        }

        private WeightedRoundRobin setWeight(int weight) {
            this.weight = weight;
            current.set(0);
            return this;
        }

        private long increaseCurrent() {
            return current.addAndGet(weight);
        }

        private void sel(int total) {
            current.addAndGet(-1L * total);
        }

        private long getLastUpdate() {
            return lastUpdate;
        }

        private void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }
}