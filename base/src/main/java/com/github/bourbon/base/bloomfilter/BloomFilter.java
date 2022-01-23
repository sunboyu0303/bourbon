package com.github.bourbon.base.bloomfilter;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.lang.Hash;
import com.github.bourbon.base.utils.PrimitiveArrayUtils;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:48
 */
public interface BloomFilter {

    Set<Hash> HASH_SET = ScopeModelUtils.getExtensionLoader(Hash.class).getSupportedExtensionInstances();

    boolean contains(String key);

    boolean add(String key);

    static int[] createHashes(String s) {
        return PrimitiveArrayUtils.unWrap(HASH_SET.stream().map(f -> Math.abs(f.getHash(s))).toArray(Integer[]::new));
    }
}