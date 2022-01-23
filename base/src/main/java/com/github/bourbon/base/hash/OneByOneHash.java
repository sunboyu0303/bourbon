package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:08
 */
public class OneByOneHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash += c;
            hash += hash << 10;
            hash ^= hash >> 6;
        }
        hash += hash << 3;
        hash ^= hash >> 11;
        hash += hash << 15;
        return hash;
    }
}