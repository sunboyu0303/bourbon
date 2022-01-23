package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 13:13
 */
public class DEKHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = str.length();
        for (char c : str.toCharArray()) {
            hash = hash << 5 ^ hash >> 27 ^ c;
        }
        return hash & Integer.MAX_VALUE;
    }
}