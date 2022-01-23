package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:09
 */
public class BernsteinHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = 33 * hash + c;
        }
        return hash;
    }
}