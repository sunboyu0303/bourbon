package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 13:11
 */
public class SDBMHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = c + (hash << 6) + (hash << 16) - hash;
        }
        return hash & Integer.MAX_VALUE;
    }
}