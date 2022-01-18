package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.CharConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/16 18:53
 */
public class NamingCaseUtils {

    static String toUnderlineCase(CharSequence cs) {
        return toSymbolCase(cs, CharConstants.UNDERLINE);
    }

    static String toKebabCase(CharSequence cs) {
        return toSymbolCase(cs, CharConstants.HYPHEN);
    }

    static String toSymbolCase(CharSequence cs, char symbol) {
        if (cs == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cs.length(); ++i) {
            char c = cs.charAt(i);
            if (Character.isUpperCase(c)) {
                Character preChar = i > 0 ? cs.charAt(i - 1) : null;
                Character nextChar = i < cs.length() - 1 ? cs.charAt(i + 1) : null;
                if (null != preChar) {
                    if (symbol == preChar) {
                        if (null == nextChar || Character.isLowerCase(nextChar)) {
                            c = Character.toLowerCase(c);
                        }
                    } else if (Character.isLowerCase(preChar)) {
                        sb.append(symbol);
                        if (null == nextChar || Character.isLowerCase(nextChar)) {
                            c = Character.toLowerCase(c);
                        }
                    } else if (null == nextChar || Character.isLowerCase(nextChar)) {
                        sb.append(symbol);
                        c = Character.toLowerCase(c);
                    }
                } else if (null == nextChar || Character.isLowerCase(nextChar)) {
                    c = Character.toLowerCase(c);
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
}