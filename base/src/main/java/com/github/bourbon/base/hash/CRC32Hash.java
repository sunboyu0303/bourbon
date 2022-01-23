package com.github.bourbon.base.hash;

import com.github.bourbon.base.lang.Hash;

import java.util.zip.CRC32;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:18
 */
public class CRC32Hash implements Hash {

    @Override
    public int getHash(String str) {
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        return (int) crc32.getValue();
    }
}