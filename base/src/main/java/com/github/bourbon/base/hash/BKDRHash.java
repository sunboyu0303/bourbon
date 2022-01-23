package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:21
 */
public class BKDRHash implements Hash {

    @Override
    public int getHash(String str) {
        return getHash(str, BKDR.A);
    }

    public int getHash(String str, BKDR bkdr) {
        int h = 0;
        for (char c : str.toCharArray()) {
            h = bkdr.value * h + c;
        }
        return h;
    }

    public enum BKDR {
        A(31),
        B(131),
        C(1313),
        D(13131),
        E(131313);

        private int value;

        BKDR(int value) {
            this.value = value;
        }
    }
}