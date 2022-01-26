package com.github.bourbon.base.lang.cache;

import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 16:17
 */
public class LFUCache<K, V> {

    private final ReentrantLock lock = new ReentrantLock();

    private Map<K, CacheNode<K, V>> map = new HashMap<>();

    private CacheDeque<K, V>[] freqTable;

    private final int capacity;

    private final int evictionCount;

    private int curSize = 0;

    public LFUCache() {
        this(IntConstants.KB, 0.75f);
    }

    @SuppressWarnings("unchecked")
    public LFUCache(final int maxCapacity, final float evictionFactor) {
        boolean factorInRange = evictionFactor <= 1 && evictionFactor > 0;
        if (!factorInRange || Float.isNaN(evictionFactor)) {
            throw new IllegalArgumentException("Illegal eviction factor value:" + evictionFactor);
        }
        this.capacity = IntUtils.checkPositive(maxCapacity, () -> "Illegal initial capacity: " + maxCapacity);
        this.evictionCount = (int) (capacity * evictionFactor);

        freqTable = new CacheDeque[capacity + 1];
        for (int i = 0; i <= capacity; i++) {
            freqTable[i] = new CacheDeque<>();
        }
        for (int i = 0; i < capacity; i++) {
            freqTable[i].nextDeque = freqTable[i + 1];
        }
        freqTable[capacity].nextDeque = freqTable[capacity];
    }

    public V put(final K key, final V value) {
        CacheNode<K, V> node;
        lock.lock();
        try {
            node = map.get(key);
            if (node != null) {
                CacheNode.withDrawNode(node);
                node.v = value;
                freqTable[0].addLast(node);
                map.put(key, node);
            } else {
                node = freqTable[0].addLast(key, value);
                map.put(key, node);
                curSize++;
                if (curSize > capacity) {
                    proceedEviction();
                }
            }
        } finally {
            lock.unlock();
        }
        return node.v;
    }

    public V remove(final K key) {
        CacheNode<K, V> node = null;
        lock.lock();
        try {
            if (map.containsKey(key)) {
                node = map.remove(key);
                if (node != null) {
                    CacheNode.withDrawNode(node);
                }
                curSize--;
            }
        } finally {
            lock.unlock();
        }
        return ObjectUtils.defaultIfNull(node, n -> n.v);
    }

    public V get(final K key) {
        CacheNode<K, V> node = null;
        lock.lock();
        try {
            if (map.containsKey(key)) {
                node = map.get(key);
                CacheNode.withDrawNode(node);
                node.owner.nextDeque.addLast(node);
            }
        } finally {
            lock.unlock();
        }
        return ObjectUtils.defaultIfNull(node, n -> n.v);
    }

    private int proceedEviction() {
        int targetSize = capacity - evictionCount;
        int evictedElements = 0;
        for (CacheDeque<K, V> deque : freqTable) {
            CacheNode<K, V> node;
            while (!deque.isEmpty()) {
                node = deque.pollFirst();
                remove(node.k);
                if (targetSize >= curSize) {
                    return evictedElements;
                }
                evictedElements++;
            }
        }
        return evictedElements;
    }

    public void clear() {
        lock.lock();
        try {
            for (CacheDeque<K, V> deque : freqTable) {
                while (!deque.isEmpty()) {
                    deque.pollFirst();
                }
            }
            map.clear();
            curSize = 0;
        } finally {
            lock.unlock();
        }
    }

    private static class CacheNode<K, V> {

        private CacheNode<K, V> prev;

        private CacheNode<K, V> next;

        private K k;

        private V v;

        private CacheDeque<K, V> owner;

        private CacheNode() {
        }

        private CacheNode(K k, V v) {
            this.k = k;
            this.v = v;
        }

        private static <K, V> CacheNode<K, V> withDrawNode(final CacheNode<K, V> node) {
            if (node != null && node.prev != null) {
                node.prev.next = node.next;
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
            }
            return node;
        }
    }

    private static class CacheDeque<K, V> {

        private CacheNode<K, V> first;

        private CacheNode<K, V> last;

        private CacheDeque<K, V> nextDeque;

        private CacheDeque() {
            first = new CacheNode<>();
            last = new CacheNode<>();
            last.next = first;
            first.prev = last;
        }

        private CacheNode<K, V> addLast(final K key, final V value) {
            return addLast(new CacheNode<>(key, value));
        }

        private CacheNode<K, V> addLast(final CacheNode<K, V> node) {
            node.owner = this;
            node.next = last.next;
            node.prev = last;
            node.next.prev = node;
            last.next = node;
            return node;
        }

        private CacheNode<K, V> pollFirst() {
            CacheNode<K, V> node = null;
            if (first.prev != last) {
                node = first.prev;
                first.prev = node.prev;
                first.prev.next = first;
                node.prev = null;
                node.next = null;
            }
            return node;
        }

        private boolean isEmpty() {
            return last.next == first;
        }
    }
}