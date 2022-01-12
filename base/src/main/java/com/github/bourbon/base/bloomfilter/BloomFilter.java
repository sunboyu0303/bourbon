package com.github.bourbon.base.bloomfilter;

import com.github.bourbon.base.utils.HashUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.PrimitiveArrayUtils;
import com.github.bourbon.base.utils.function.IntFunc;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 19:48
 */
public interface BloomFilter {

    List<IntFunc<String>> INT_FUNC_LIST = ListUtils.unmodifiableList(ListUtils.newArrayList(
            HashUtils::getRSHash, HashUtils::getJSHash, HashUtils::getELFHash, s -> HashUtils.getBKDRHash(s, HashUtils.BKDREnum.B),
            HashUtils::getAPHash, HashUtils::getDJBHash, HashUtils::getSDBMHash, HashUtils::getPJWHash
    ));

    boolean contains(String key);

    boolean add(String key);

    static int[] createHashes(String s) {
        return PrimitiveArrayUtils.unWrap(INT_FUNC_LIST.stream().map(f -> Math.abs(f.apply(s))).toArray(Integer[]::new));
    }
}