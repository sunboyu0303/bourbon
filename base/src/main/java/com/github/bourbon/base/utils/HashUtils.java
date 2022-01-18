package com.github.bourbon.base.utils;

import java.util.zip.CRC32;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 17:19
 */
public interface HashUtils {

    static int getOneByOneHash(String s) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash += c;
            hash += hash << 10;
            hash ^= hash >> 6;
        }
        hash += hash << 3;
        hash ^= hash >> 11;
        hash += hash << 15;
        return hash;
    }

    static int getBernsteinHash(String s) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash = 33 * hash + c;
        }
        return hash;
    }

    static int getFNVHash(String s) {
        int hash = -2128831035;
        for (char c : s.toCharArray()) {
            hash = (hash ^ c) * 16777619;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }

    static int getRSHash(String s) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash = hash * a + c;
            a *= b;
        }
        return hash & Integer.MAX_VALUE;
    }

    static int getJSHash(String s) {
        int hash = 1315423911;
        for (char c : s.toCharArray()) {
            hash ^= (hash << 5) + c + (hash >> 2);
        }
        return hash & Integer.MAX_VALUE;
    }

    static int getPJWHash(String s) {
        int bitsInUnsignedInt = 32;
        int threeQuarters = bitsInUnsignedInt * 3 / 4;
        int oneEighth = bitsInUnsignedInt / 8;
        int highBits = -1 << bitsInUnsignedInt - oneEighth;
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash = (hash << oneEighth) + c;
            int test;
            if ((test = hash & highBits) != 0) {
                hash = (hash ^ test >> threeQuarters) & ~highBits;
            }
        }
        return hash & Integer.MAX_VALUE;
    }

    static int getELFHash(String s) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash = (hash << 4) + c;
            int x;
            if ((x = (int) ((long) hash & 4026531840L)) != 0) {
                hash ^= x >> 24;
                hash &= ~x;
            }
        }
        return hash & Integer.MAX_VALUE;
    }

    static long getCRC32Hash(String s) {
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        return crc32.getValue();
    }

    static int getBKDRHash(String s) {
        return getBKDRHash(s, BKDREnum.A);
    }

    static int getBKDRHash(String s, BKDREnum e) {
        int h = 0;
        for (char c : s.toCharArray()) {
            h = e.value * h + c;
        }
        return h;
    }

    static int getAbsBKDRHash(String s) {
        return getBKDRHash(s) & Integer.MAX_VALUE;
    }

    static int getAbsBKDRHash(String s, BKDREnum e) {
        return getBKDRHash(s, e) & Integer.MAX_VALUE;
    }

    static int getAPHash(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            hash ^= (i & 1) == 0 ? hash << 7 ^ c ^ hash >> 3 : ~(hash << 11 ^ c ^ hash >> 5);
        }
        return hash;
    }

    static int getSDBMHash(String s) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash = c + (hash << 6) + (hash << 16) - hash;
        }
        return hash & Integer.MAX_VALUE;
    }

    static int getDJBHash(String s) {
        int hash = 5381;
        for (char c : s.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & Integer.MAX_VALUE;
    }

    static int dekHash(String s) {
        int hash = s.length();
        for (char c : s.toCharArray()) {
            hash = hash << 5 ^ hash >> 27 ^ c;
        }
        return hash & Integer.MAX_VALUE;
    }

    enum BKDREnum {
        A(31),
        B(131),
        C(1313),
        D(13131),
        E(131313);

        private int value;

        BKDREnum(int value) {
            this.value = value;
        }
    }
}