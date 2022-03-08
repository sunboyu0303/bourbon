package com.github.bourbon.pfinder.profiler.common.protocol;

import com.github.bourbon.base.utils.ObjectUtils;

import java.io.Serializable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:33
 */
public class Pair<K, V> implements Serializable {

    private static final Pair[] EMPTY_ARRAY = new Pair[0];

    @Tag(1)
    private K key;
    @Tag(2)
    private V value;

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public static <K, V> Pair<K, V>[] asArray(Pair<K, V>... pairs) {
        return pairs;
    }

    public static <K, V> Pair<K, V>[] singletonArray(Pair<K, V> pair) {
        return new Pair[]{pair};
    }

    public static <K, V> Pair<K, V>[] emptyArray() {
        return EMPTY_ARRAY;
    }

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public Pair<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    public V getValue() {
        return this.value;
    }

    public Pair<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

    public void peek(Pair.Peeker<K, V> peeker) {
        peeker.peek(this.key, this.value);
    }

    public <R> R map(Pair.Mapper<K, V, R> mapper) {
        return mapper.map(this.key, this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            Pair<?, ?> pair = (Pair) o;
            if (this.key != null) {
                if (this.key.equals(pair.key)) {
                    return ObjectUtils.equals(this.value, pair.value);
                }
            } else if (pair.key == null) {
                return ObjectUtils.equals(this.value, pair.value);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = this.key != null ? this.key.hashCode() : 0;
        result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" + this.key + ": " + this.value + '}';
    }

    public interface Mapper<K, V, R> {
        R map(K var1, V var2);
    }

    public interface Peeker<K, V> {
        void peek(K var1, V var2);
    }
}