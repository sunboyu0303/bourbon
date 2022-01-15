package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 15:58
 */
public final class DataObjectPropertyName {

    private DataObjectPropertyName() {
    }

    public static String toDashedForm(String name) {
        StringBuilder result = new StringBuilder(name.length());
        boolean inIndex = false;
        for (char ch : name.toCharArray()) {
            if (inIndex) {
                result.append(ch);
                if (ch == CharConstants.RIGHT_BRACKETS) {
                    inIndex = false;
                }
            } else {
                if (ch == CharConstants.LEFT_BRACKETS) {
                    inIndex = true;
                    result.append(ch);
                } else {
                    ch = BooleanUtils.defaultIfFalse(ch != CharConstants.UNDERLINE, ch, CharConstants.HYPHEN);
                    if (Character.isUpperCase(ch) && result.length() > 0 && result.charAt(result.length() - 1) != CharConstants.HYPHEN) {
                        result.append(CharConstants.HYPHEN);
                    }
                    result.append(Character.toLowerCase(ch));
                }
            }
        }
        return result.toString();
    }
}