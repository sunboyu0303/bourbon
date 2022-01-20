package com.github.bourbon.base.loadbalance.impl;

import com.github.bourbon.base.loadbalance.AbstractLoadBalance;
import com.github.bourbon.base.loadbalance.ILoadBalance;
import com.github.bourbon.base.loadbalance.Invocation;
import com.github.bourbon.base.loadbalance.Invoker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/2 22:06
 */
public class RandomLoadBalance extends AbstractLoadBalance implements ILoadBalance {

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, Invocation invocation) {
        int length = invokers.size();
        boolean sameWeight = true;
        int[] weights = new int[length];
        int totalWeight = 0;

        for (int i = 0; i < length; i++) {
            int weight = invokers.get(i).getWeight();
            totalWeight += weight;
            weights[i] = totalWeight;
            if (sameWeight && totalWeight != weight * (i + 1)) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // If (not every invoker has the same weight & at least one invoker's weight>0), select randomly based on totalWeight.
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < length; i++) {
                if (offset < weights[i]) {
                    return invokers.get(i);
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }
}