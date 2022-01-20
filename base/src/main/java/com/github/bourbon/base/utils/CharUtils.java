package com.github.bourbon.base.utils;

import java.util.Set;

import static com.github.bourbon.base.constant.CharConstants.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 14:44
 */
public final class CharUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(PRIMITIVE_CLASS, BOXED_CLASS);

    public static boolean isLetter(char c) {
        return isLetterUpper(c) || isLetterLower(c);
    }

    public static boolean isLetterUpper(char c) {
        return c >= A && c <= Z;
    }

    public static boolean isLetterLower(char c) {
        return c >= SMALL_A && c <= SMALL_Z;
    }

    public static boolean isNumber(char c) {
        return c >= ZERO && c <= NINE;
    }

    public static boolean isHexChar(char c) {
        return isNumber(c) || c >= SMALL_A && c <= SMALL_F || c >= A && c <= F;
    }

    public static boolean isWord(char c) {
        return isLetter(c) || isNumber(c);
    }

    public static boolean isChar(Object o) {
        return o instanceof Character || o.getClass() == TYPE;
    }

    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234 || c == 0;
    }

    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }

    public static boolean isCharacter(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static char charValue(Character w, char defaultValue) {
        return ObjectUtils.defaultIfNull(w, defaultValue);
    }

    public static boolean equals(char c1, char c2) {
        return equals(c1, c2, false);
    }

    public static boolean equals(char c1, char c2, boolean ignore) {
        return BooleanUtils.defaultSupplierIfFalse(ignore, () -> Character.toLowerCase(c1) == Character.toLowerCase(c2), () -> c1 == c2);
    }

    public static String toString(char c) {
        return ASCIIStrCache.toString(c);
    }

    private static class ASCIIStrCache {
        private static final int ASCII_LENGTH = 128;
        private static final String[] CACHE = new String[ASCII_LENGTH];

        static {
            for (char c = 0; c < ASCII_LENGTH; ++c) {
                CACHE[c] = String.valueOf(c);
            }
        }

        public static String toString(char c) {
            return BooleanUtils.defaultSupplierIfFalse(c < ASCII_LENGTH, () -> CACHE[c], () -> String.valueOf(c));
        }
    }

    private CharUtils() {
    }
}