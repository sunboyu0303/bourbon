package com.github.bourbon.base.appender.builder;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 17:02
 */
public class XStringBuilder {

    private final StringBuilder sb;
    private final char separator;

    public XStringBuilder() {
        this(256);
    }

    public XStringBuilder(int size) {
        this(size, CharConstants.COMMA);
    }

    public XStringBuilder(int size, char separator) {
        this.separator = separator;
        this.sb = new StringBuilder(size);
    }

    public XStringBuilder append(String str) {
        sb.append(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY)).append(separator);
        return this;
    }

    public XStringBuilder append(String str, String separator) {
        sb.append(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY)).append(separator);
        return this;
    }

    public XStringBuilder append(String str, char separator) {
        sb.append(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY)).append(separator);
        return this;
    }

    public XStringBuilder append(long str) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(long str, String separator) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(long str, char separator) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(int str) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(int str, String separator) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(int str, char separator) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(char str) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(char str, String separator) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(char str, char separator) {
        sb.append(str).append(separator);
        return this;
    }

    public XStringBuilder append(Map<String, String> map) {
        this.appendEscape(MapUtils.mapToString(map));
        return this;
    }

    public XStringBuilder appendEnd(int str) {
        sb.append(str).append(StringConstants.NEWLINE);
        return this;
    }

    public XStringBuilder appendEnd(String str) {
        sb.append(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY)).append(StringConstants.NEWLINE);
        return this;
    }

    public XStringBuilder appendEnd(long str) {
        sb.append(str).append(StringConstants.NEWLINE);
        return this;
    }

    public XStringBuilder appendEnd(char c) {
        sb.append(c).append(StringConstants.NEWLINE);
        return this;
    }

    public XStringBuilder appendEnd(Map<String, String> map) {
        this.appendEscapeEnd(MapUtils.mapToString(map));
        return this;
    }

    public XStringBuilder appendRaw(String str) {
        sb.append(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY));
        return this;
    }

    public XStringBuilder appendEscape(String str) {
        return append(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY));
    }

    public XStringBuilder appendEscapeRaw(String str) {
        return appendRaw(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY));
    }

    public XStringBuilder appendEscapeEnd(String str) {
        return appendEnd(ObjectUtils.defaultIfNull(str, StringConstants.EMPTY));
    }

    public XStringBuilder reset() {
        sb.delete(0, sb.length());
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}