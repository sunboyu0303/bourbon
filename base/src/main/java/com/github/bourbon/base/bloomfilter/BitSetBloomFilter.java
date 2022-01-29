package com.github.bourbon.base.bloomfilter;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.lang.Hash;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:49
 */
public final class BitSetBloomFilter extends AbstractBloomFilter {

    private final BitSet bitSet = new BitSet(Integer.MAX_VALUE);

    public BitSetBloomFilter() {
        this(ScopeModelUtils.getExtensionLoader(Hash.class).getSupportedExtensionInstances());
    }

    public BitSetBloomFilter(Set<Hash> hashSet) {
        super(hashSet);
    }

    @Override
    public boolean add(String s) {
        return BooleanUtils.defaultIfPredicate(createHashes(s), h -> !contains(h, i -> !bitSet.get(i)), h -> {
            Arrays.stream(h).forEach(i -> bitSet.set(i, true));
            return true;
        }, false);
    }

    @Override
    public boolean contains(String s) {
        return contains(createHashes(s), i -> !bitSet.get(i));
    }
}