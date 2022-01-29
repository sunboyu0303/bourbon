package com.github.bourbon.base.bloomfilter;

import com.github.bourbon.base.lang.Hash;
import com.github.bourbon.base.utils.PrimitiveArrayUtils;
import com.github.bourbon.base.utils.function.IntPredicate;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 09:08
 */
public abstract class AbstractBloomFilter implements BloomFilter {

    private final Set<Hash> hashSet;

    protected AbstractBloomFilter(Set<Hash> hashSet) {
        this.hashSet = hashSet;
    }

    protected final int[] createHashes(String s) {
        return PrimitiveArrayUtils.unWrap(hashSet.stream().map(f -> Math.abs(f.getHash(s))).toArray(Integer[]::new));
    }

    protected final boolean contains(int[] hashes, IntPredicate predicate) {
        for (int i : hashes) {
            if (predicate.test(i)) {
                return false;
            }
        }
        return true;
    }
}