package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:10
 */
public class FNVHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = -2128831035;
        for (char c : str.toCharArray()) {
            hash = (hash ^ c) * 16777619;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }
}