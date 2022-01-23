package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:16
 */
public class PJWHash implements Hash {

    @Override
    public int getHash(String str) {
        int bitsInUnsignedInt = 32;
        int threeQuarters = bitsInUnsignedInt * 3 / 4;
        int oneEighth = bitsInUnsignedInt / 8;
        int highBits = -1 << bitsInUnsignedInt - oneEighth;
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = (hash << oneEighth) + c;
            int test;
            if ((test = hash & highBits) != 0) {
                hash = (hash ^ test >> threeQuarters) & ~highBits;
            }
        }
        return hash & Integer.MAX_VALUE;
    }
}