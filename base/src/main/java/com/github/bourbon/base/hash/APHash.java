package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 13:10
 */
public class APHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            hash ^= (i & 1) == 0 ? hash << 7 ^ c ^ hash >> 3 : ~(hash << 11 ^ c ^ hash >> 5);
        }
        return hash;
    }
}