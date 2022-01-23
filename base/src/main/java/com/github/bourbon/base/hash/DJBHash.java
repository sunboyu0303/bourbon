package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 13:12
 */
public class DJBHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 5381;
        for (char c : str.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & Integer.MAX_VALUE;
    }
}