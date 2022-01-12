package com.github.bourbon.base.constant;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 10:01
 */
public final class IntConstants {

    public static final int MIN_VALUE = Integer.MIN_VALUE;

    public static final int MAX_VALUE = Integer.MAX_VALUE;

    public static final int DEFAULT = 0;

    public static final Class<?> TYPE = Integer.TYPE;

    public static final Class<?> PRIMITIVE_CLASS = int.class;

    public static final Class<?> BOXED_CLASS = Integer.class;
    /**
     * {@code "00000100"}
     */
    public static final int I_4 = 0x04;
    /**
     * {@code "00001000"}
     */
    public static final int I_8 = 0x08;
    /**
     * {@code "00010000"}
     */
    public static final int I_16 = 0x10;
    /**
     * {@code "00011000"}
     */
    public static final int I_24 = 0x18;
    /**
     * {@code "11111111"}
     */
    public static final int I_255 = 0xff;
    /**
     * {@code "00000001"}
     */
    public static final int MASK1 = 0x01;
    /**
     * {@code "00000011"}
     */
    public static final int MASK2 = 0x03;
    /**
     * {@code "00000111"}
     */
    public static final int MASK3 = 0x07;
    /**
     * {@code "00001111"}
     */
    public static final int MASK4 = 0x0f;
    /**
     * {@code "00011111"}
     */
    public static final int MASK5 = 0x1f;
    /**
     * {@code "00111111"}
     */
    public static final int MASK6 = 0x3f;
    /**
     * {@code "01111111"}
     */
    public static final int MASK7 = 0x7f;
    /**
     * {@code "11111111"}
     */
    public static final int MASK8 = 0xff;

    public static final int[] EMPTY_INT_ARRAY = {};

    public static final Integer[] EMPTY_INT_BOXED_ARRAY = {};

    public static final int B = 1;

    public static final int KB = 1024;

    public static final int KB8 = 8 * KB;

    public static final int KB32 = 32 * KB;

    public static final int MB = 1048576;

    public static final int MB8 = 8 * MB;

    public static final int GB = 1073741824;

    private IntConstants() {
    }
}