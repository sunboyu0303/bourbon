package com.github.bourbon.common.bloomfilter;

import com.github.bourbon.base.bloomfilter.AbstractBloomFilter;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.lang.Hash;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.common.bitmap.BitMap;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 14:26
 */
public final class BitMapBloomFilter extends AbstractBloomFilter {

    private final BitMap bitMap = BitMap.of();

    public BitMapBloomFilter() {
        this(ScopeModelUtils.getExtensionLoader(Hash.class).getSupportedExtensionInstances());
    }

    public BitMapBloomFilter(Set<Hash> hashSet) {
        super(hashSet);
    }

    @Override
    public boolean add(String s) {
        return BooleanUtils.defaultIfPredicate(createHashes(s), h -> !contains(h, i -> !bitMap.contains(i)), h -> {
            bitMap.add(h);
            return true;
        }, false);
    }

    @Override
    public boolean contains(String s) {
        return contains(createHashes(s), i -> !bitMap.contains(i));
    }
}