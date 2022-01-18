package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;

import java.util.Set;

import static com.github.bourbon.base.constant.CharConstants.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/4 08:12
 */
public final class HexUtils {

    private static final Set<Character> DIGITS_LOWER = SetUtils.newHashSet(ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SMALL_A, SMALL_B, SMALL_C, SMALL_D, SMALL_E, SMALL_F);
    private static final Set<Character> DIGITS_UPPER = SetUtils.newHashSet(ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, A, B, C, D, E, F);
    private static final Set<Character> DIGITS_LOWER_UPPER = SetUtils.newHashSet(ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SMALL_A, SMALL_B, SMALL_C, SMALL_D, SMALL_E, SMALL_F, A, B, C, D, E, F);
    /**
     * {@code "0x"}
     */
    public static final String ZERO_SMALL_X = StringConstants.ZERO + StringConstants.SMALL_X;
    /**
     * {@code "0X"}
     */
    public static final String ZERO_X = StringConstants.ZERO + StringConstants.X;

    public static boolean isHexChar(char c) {
        return DIGITS_LOWER_UPPER.contains(c);
    }

    public static String toHex(int value) {
        return Integer.toHexString(value);
    }

    public static int hexToInt(String value) {
        return Integer.parseInt(value, 16);
    }

    public static String toHex(long value) {
        return Long.toHexString(value);
    }

    public static long hexToLong(String value) {
        return Long.parseLong(value, 16);
    }

    public static boolean isHexNumber(String value) {
        int index = value.startsWith(StringConstants.HYPHEN) ? 1 : 0;
        return value.startsWith(ZERO_SMALL_X, index) || value.startsWith(ZERO_X, index) || value.startsWith(StringConstants.POUND, index);
    }

    private HexUtils() {
    }
}