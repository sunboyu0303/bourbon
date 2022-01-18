package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.CharConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 00:06
 */
public final class MD5Utils {

    private static MessageDigest mdInst;

    static {
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] getBytes(String value) {
        return getBytes(value.getBytes(UTF_8));
    }

    public static byte[] getBytes(byte[] value) {
        return mdInst.digest(value);
    }

    public static String getString(String value) {
        byte[] md5 = getBytes(value);
        int j = md5.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : md5) {
            str[k++] = CharConstants.BASE16[byte0 >>> 4 & 0xf];
            str[k++] = CharConstants.BASE16[byte0 & 0xf];
        }
        return new String(str);
    }

    private MD5Utils() {
    }
}