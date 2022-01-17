package com.github.bourbon.base.lang;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 15:01
 */
public class GroupedMap extends LinkedHashMap<String, LinkedHashMap<String, String>> {
    private static final long serialVersionUID = -7777365130776081931L;
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = cacheLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = cacheLock.writeLock();
    private int size = -1;

    public String get(String group, String key) {
        readLock.lock();
        try {
            return BooleanUtils.defaultIfPredicate(get(CharSequenceUtils.nullToDefault(group)), m -> !MapUtils.isEmpty(m), m -> m.get(key));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public LinkedHashMap<String, String> get(Object key) {
        readLock.lock();
        try {
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int size() {
        writeLock.lock();
        try {
            if (size < 0) {
                size = 0;
                LinkedHashMap<String, String> value;
                for (Iterator<LinkedHashMap<String, String>> iterator = values().iterator(); iterator.hasNext(); size += value.size()) {
                    value = iterator.next();
                }
            }
            return size;
        } finally {
            writeLock.unlock();
        }
    }

    public String put(String group, String key, String value) {
        writeLock.lock();
        try {
            return computeIfAbsent(CharSequenceUtils.nullToDefault(group), k -> new LinkedHashMap<>()).put(key, value);
        } finally {
            size = -1;
            writeLock.unlock();
        }
    }

    public GroupedMap putAll(String group, Map<? extends String, ? extends String> m) {
        writeLock.lock();
        try {
            final String g = CharSequenceUtils.nullToDefault(group);
            m.forEach((k, v) -> put(g, k, v));
            return this;
        } finally {
            size = -1;
            writeLock.unlock();
        }
    }

    public String remove(String group, String key) {
        writeLock.lock();
        try {
            return BooleanUtils.defaultIfPredicate(get(CharSequenceUtils.nullToDefault(group)), m -> !MapUtils.isEmpty(m), m -> m.remove(key));
        } finally {
            size = -1;
            writeLock.unlock();
        }
    }

    public boolean isEmpty(String group) {
        readLock.lock();
        try {
            return MapUtils.isEmpty(get(CharSequenceUtils.nullToDefault(group)));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(String group, String key) {
        readLock.lock();
        try {
            LinkedHashMap<String, String> valueMap = get(CharSequenceUtils.nullToDefault(group));
            return !MapUtils.isEmpty(valueMap) && valueMap.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    public boolean containsValue(String group, String value) {
        readLock.lock();
        try {
            LinkedHashMap<String, String> valueMap = get(CharSequenceUtils.nullToDefault(group));
            return !MapUtils.isEmpty(valueMap) && valueMap.containsValue(value);
        } finally {
            readLock.unlock();
        }
    }

    public GroupedMap clear(String group) {
        writeLock.lock();
        try {
            LinkedHashMap<String, String> valueMap = get(CharSequenceUtils.nullToDefault(group));
            if (!MapUtils.isEmpty(valueMap)) {
                valueMap.clear();
            }
            return this;
        } finally {
            size = -1;
            writeLock.unlock();
        }
    }

    @Override
    public Set<String> keySet() {
        readLock.lock();
        try {
            return super.keySet();
        } finally {
            readLock.unlock();
        }
    }

    public Set<String> keySet(String group) {
        readLock.lock();
        try {
            return BooleanUtils.defaultSupplierIfPredicate(get(CharSequenceUtils.nullToDefault(group)), m -> !MapUtils.isEmpty(m), LinkedHashMap::keySet, Collections::emptySet);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<LinkedHashMap<String, String>> values() {
        readLock.lock();
        try {
            return super.values();
        } finally {
            readLock.unlock();
        }
    }

    public Collection<String> values(String group) {
        readLock.lock();
        try {
            return BooleanUtils.defaultSupplierIfPredicate(get(CharSequenceUtils.nullToDefault(group)), m -> !MapUtils.isEmpty(m), LinkedHashMap::values, Collections::emptyList);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Set<Map.Entry<String, LinkedHashMap<String, String>>> entrySet() {
        readLock.lock();
        try {
            return super.entrySet();
        } finally {
            readLock.unlock();
        }
    }

    public Set<Map.Entry<String, String>> entrySet(String group) {
        readLock.lock();
        try {
            return BooleanUtils.defaultSupplierIfPredicate(get(CharSequenceUtils.nullToDefault(group)), m -> !MapUtils.isEmpty(m), LinkedHashMap::entrySet, Collections::emptySet);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            return super.toString();
        } finally {
            readLock.unlock();
        }
    }
}