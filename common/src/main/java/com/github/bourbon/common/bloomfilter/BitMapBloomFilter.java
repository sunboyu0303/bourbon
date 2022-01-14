package com.github.bourbon.common.bloomfilter;

import com.github.bourbon.base.bloomfilter.BloomFilter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.common.bitmap.BitMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 14:26
 */
public class BitMapBloomFilter implements BloomFilter {

    private final BitMap bitMap = BitMap.of();

    @Override
    public boolean add(String s) {
        return BooleanUtils.defaultIfPredicate(BloomFilter.createHashes(s), h -> !contains(h), h -> {
            bitMap.add(h);
            return true;
        }, false);
    }

    @Override
    public boolean contains(String s) {
        return contains(BloomFilter.createHashes(s));
    }

    private boolean contains(int[] hashes) {
        for (int i : hashes) {
            if (!bitMap.contains(i)) {
                return false;
            }
        }
        return true;
    }
}