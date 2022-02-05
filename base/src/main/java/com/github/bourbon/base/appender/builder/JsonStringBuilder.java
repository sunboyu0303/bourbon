package com.github.bourbon.base.appender.builder;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 15:01
 */
public class JsonStringBuilder {

    private final StringBuilder sb;

    private final boolean isValueNullCheck;

    public JsonStringBuilder() {
        this(false);
    }

    public JsonStringBuilder(boolean isValueNullCheck) {
        this(isValueNullCheck, 256);
    }

    public JsonStringBuilder(boolean isValueNullCheck, int size) {
        this.isValueNullCheck = isValueNullCheck;
        this.sb = new StringBuilder(size);
    }

    public JsonStringBuilder appendBegin() {
        sb.append(CharConstants.LEFT_BRACES);
        return this;
    }

    public JsonStringBuilder append(String key, Object value) {
        if (value == null) {
            if (isValueNullCheck) {
                return this;
            }
        }
        append(key, value, CharConstants.COMMA);
        return this;
    }

    public JsonStringBuilder appendEnd() {
        return appendEnd(true);
    }

    public JsonStringBuilder appendEnd(boolean isNewLine) {
        if (sb.charAt(sb.length() - 1) == CharConstants.COMMA) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(CharConstants.RIGHT_BRACES);
        if (isNewLine) {
            sb.append(StringConstants.NEWLINE);
        }
        return this;
    }

    private JsonStringBuilder append(String key, Object value, char endChar) {
        if (value == null) {
            sb.append(CharConstants.DOUBLE_QUOTES).append(key).append(CharConstants.DOUBLE_QUOTES).append(CharConstants.COLON).append(CharConstants.DOUBLE_QUOTES).append(CharConstants.DOUBLE_QUOTES).append(endChar);
            return this;
        }
        if (value instanceof String) {
            String valueStr = (String) value;
            if (valueStr.length() <= 0 || (valueStr.charAt(0) != CharConstants.LEFT_BRACES && valueStr.charAt(0) != CharConstants.LEFT_BRACKETS)) {
                sb.append(CharConstants.DOUBLE_QUOTES).append(key).append(CharConstants.DOUBLE_QUOTES).append(CharConstants.COLON).append(CharConstants.DOUBLE_QUOTES).append(value).append(CharConstants.DOUBLE_QUOTES).append(endChar);
                return this;
            }
        }
        sb.append(CharConstants.DOUBLE_QUOTES).append(key).append(CharConstants.DOUBLE_QUOTES).append(CharConstants.COLON).append(value).append(endChar);
        return this;
    }

    public JsonStringBuilder reset() {
        sb.delete(0, sb.length());
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}