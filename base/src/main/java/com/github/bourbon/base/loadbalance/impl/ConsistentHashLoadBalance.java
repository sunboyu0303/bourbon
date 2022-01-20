package com.github.bourbon.base.loadbalance.impl;

import com.github.bourbon.base.loadbalance.AbstractLoadBalance;
import com.github.bourbon.base.loadbalance.ILoadBalance;
import com.github.bourbon.base.loadbalance.Invocation;
import com.github.bourbon.base.loadbalance.Invoker;
import com.github.bourbon.base.utils.MD5Utils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/3 16:39
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance implements ILoadBalance {

    private final ConcurrentMap<String, ConsistentHashSelector<?>> selectors = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, Invocation invocation) {
        String key = invocation.getServiceKey();
        // using the hashcode of list to compute the hash only pay attention to the elements in the list
        int invokersHashCode = invokers.hashCode();
        ConsistentHashSelector<T> selector = (ConsistentHashSelector<T>) selectors.get(key);
        if (selector == null || selector.identityHashCode != invokersHashCode) {
            selectors.put(key, new ConsistentHashSelector<>(invokers, invokersHashCode));
            selector = (ConsistentHashSelector<T>) selectors.get(key);
        }
        return selector.select(invocation);
    }

    private static final class ConsistentHashSelector<T> {
        private final TreeMap<Long, Invoker<T>> virtualInvokers = new TreeMap<>();
        private final int identityHashCode;

        private final Map<String, AtomicLong> serverRequestCountMap = MapUtils.newConcurrentHashMap();
        private final AtomicLong totalRequestCount = new AtomicLong(0);
        private final int serverCount;

        private ConsistentHashSelector(List<Invoker<T>> invokers, int identityHashCode) {
            this.identityHashCode = identityHashCode;
            for (Invoker<T> invoker : invokers) {
                String address = invoker.get().toString();
                for (int i = 0; i < 64; i++) {
                    byte[] digest = MD5Utils.getBytes(address + i);
                    for (int h = 0; h < 4; h++) {
                        virtualInvokers.put(hash(digest, h), invoker);
                    }
                }
            }
            serverCount = invokers.size();
        }

        private Invoker<T> select(Invocation invocation) {
            return selectForKey(hash(MD5Utils.getBytes(toKey(invocation.getArguments())), 0));
        }

        private String toKey(Object[] args) {
            StringBuilder buf = new StringBuilder();
            for (Object object : args) {
                buf.append(object);
            }
            return buf.toString();
        }

        private Invoker<T> selectForKey(long hash) {
            Map.Entry<Long, Invoker<T>> entry = virtualInvokers.ceilingEntry(hash);
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            String serverAddress = entry.getValue().get().toString();
            double overloadThread = ((double) totalRequestCount.get() / (double) serverCount) * 1.5d;
            AtomicLong atomicLong;
            while ((atomicLong = serverRequestCountMap.get(serverAddress)) != null && atomicLong.get() >= overloadThread) {
                entry = getNextInvokerNode(virtualInvokers, entry);
                serverAddress = entry.getValue().get().toString();
            }

            serverRequestCountMap.computeIfAbsent(serverAddress, o -> new AtomicLong(0)).incrementAndGet();
            totalRequestCount.incrementAndGet();
            return entry.getValue();
        }

        private Map.Entry<Long, Invoker<T>> getNextInvokerNode(TreeMap<Long, Invoker<T>> virtualInvokers, Map.Entry<Long, Invoker<T>> entry) {
            return ObjectUtils.defaultSupplierIfNull(virtualInvokers.higherEntry(entry.getKey()), virtualInvokers::firstEntry);
        }

        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24) | ((long) (digest[2 + number * 4] & 0xFF) << 16) | ((long) (digest[1 + number * 4] & 0xFF) << 8) | (digest[number * 4] & 0xFF)) & 0xFFFFFFFFL;
        }
    }
}