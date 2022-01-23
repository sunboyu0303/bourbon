package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:12
 */
public class RSHash implements Hash {

    @Override
    public int getHash(String str) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = hash * a + c;
            a *= b;
        }
        return hash & Integer.MAX_VALUE;
    }
}