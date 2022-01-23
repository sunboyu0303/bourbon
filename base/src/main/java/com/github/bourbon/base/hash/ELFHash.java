package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:17
 */
public class ELFHash implements Hash {

    @Override
    public int getHash(String str) {
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = (hash << 4) + c;
            int x;
            if ((x = (int) ((long) hash & 4026531840L)) != 0) {
                hash ^= x >> 24;
                hash &= ~x;
            }
        }
        return hash & Integer.MAX_VALUE;
    }
}