package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:48
 */
public class AbsBKDRHash extends BKDRHash implements Hash {

    @Override
    public int getHash(String str) {
        return super.getHash(str) & Integer.MAX_VALUE;
    }

    @Override
    public int getHash(String str, BKDR bkdr) {
        return super.getHash(str, bkdr) & Integer.MAX_VALUE;
    }
}