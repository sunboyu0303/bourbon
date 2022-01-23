package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:14
 */
public class JSHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 1315423911;
        for (char c : str.toCharArray()) {
            hash ^= (hash << 5) + c + (hash >> 2);
        }
        return hash & Integer.MAX_VALUE;
    }
}