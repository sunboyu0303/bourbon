package com.github.bourbon.base.constant;

/**
 * byte 常量类
 *
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 09:51
 */
public final class ByteConstants {
    /**
     * 最小 byte 值
     */
    public static final byte MIN_VALUE = Byte.MIN_VALUE;
    /**
     * 最大 byte 值
     */
    public static final byte MAX_VALUE = Byte.MAX_VALUE;
    /**
     * 默认 byte 值
     */
    public static final byte DEFAULT = (byte) 0;
    /**
     * byte type
     */
    public static final Class<?> TYPE = Byte.TYPE;
    /**
     * byte 原始类
     */
    public static final Class<?> PRIMITIVE_CLASS = byte.class;
    /**
     * byte 包装类
     */
    public static final Class<?> BOXED_CLASS = Byte.class;
    /**
     * 空 byte 原始类数组
     */
    public static final byte[] EMPTY_BYTE_ARRAY = {};
    /**
     * 空 byte 包装类数组
     */
    public static final Byte[] EMPTY_BYTE_BOXED_ARRAY = {};

    private ByteConstants() {
        throw new UnsupportedOperationException();
    }
}