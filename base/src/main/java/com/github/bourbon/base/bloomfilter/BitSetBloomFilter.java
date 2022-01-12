package com.github.bourbon.base.bloomfilter;

import com.github.bourbon.base.utils.BooleanUtils;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:49
 */
public class BitSetBloomFilter implements BloomFilter {

    private final BitSet bitSet = new BitSet(Integer.MAX_VALUE);

    @Override
    public boolean add(String s) {
        return BooleanUtils.defaultIfPredicate(BloomFilter.createHashes(s), h -> !contains(h), h -> {
            Arrays.stream(h).forEach(i -> bitSet.set(i, true));
            return true;
        }, false);
    }

    @Override
    public boolean contains(String s) {
        return contains(BloomFilter.createHashes(s));
    }

    private boolean contains(int[] h) {
        for (int i : h) {
            if (!bitSet.get(i)) {
                return false;
            }
        }
        return true;
    }
}