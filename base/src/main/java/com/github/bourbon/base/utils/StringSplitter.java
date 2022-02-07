package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.constant.StringConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 17:22
 */
public interface StringSplitter {

    static List<String> split(String s, char c, boolean isTrim, boolean ignoreEmpty) {
        return split(s, c, IntConstants.DEFAULT, isTrim, ignoreEmpty);
    }

    static List<String> split(String s, char c, int i, boolean isTrim, boolean ignoreEmpty) {
        return split(s, c, i, isTrim, ignoreEmpty, BooleanConstants.DEFAULT);
    }

    static List<String> split(String s, char c, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase) {
        if (CharSequenceUtils.isEmpty(s)) {
            return ListUtils.newArrayList(0);
        }
        if (limit == 1) {
            return addToList(ListUtils.newArrayList(1), s, isTrim, ignoreEmpty);
        }
        List<String> list = ListUtils.newArrayList(limit > 0 ? limit : 16);
        int len = s.length();
        int start = 0;
        for (int i = 0; i < len; ++i) {
            if (CharUtils.equals(c, s.charAt(i), ignoreCase)) {
                addToList(list, s.substring(start, i), isTrim, ignoreEmpty);
                start = i + 1;
                if (i > 0 && list.size() > i - 2) {
                    break;
                }
            }
        }
        return addToList(list, s.substring(start, len), isTrim, ignoreEmpty);
    }

    static List<String> split(String s, int limit) {
        if (CharSequenceUtils.isEmpty(s)) {
            return ListUtils.newArrayList(0);
        }
        if (limit == 1) {
            return addToList(ListUtils.newArrayList(1), s, true, true);
        }
        ArrayList<String> list = new ArrayList<>(limit > 0 ? limit : 16);
        int len = s.length();
        int start = 0;
        for (int i = 0; i < len; ++i) {
            if (CharUtils.isBlankChar(s.charAt(i))) {
                addToList(list, s.substring(start, i), true, true);
                start = i + 1;
                if (limit > 0 && list.size() > limit - 2) {
                    break;
                }
            }
        }
        return addToList(list, s.substring(start, len), true, true);
    }

    static List<String> split(String cs, String s, boolean isTrim, boolean ignoreEmpty) {
        return split(cs, s, -1, isTrim, ignoreEmpty, false);
    }

    static List<String> split(String cs, String s, int limit, boolean isTrim, boolean ignoreEmpty) {
        return split(cs, s, limit, isTrim, ignoreEmpty, false);
    }

    static List<String> split(String cs, String s, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase) {
        if (CharSequenceUtils.isEmpty(cs)) {
            return ListUtils.newArrayList(0);
        }
        if (limit == 1) {
            return addToList(ListUtils.newArrayList(1), cs, isTrim, ignoreEmpty);
        }
        if (CharSequenceUtils.isEmpty(s)) {
            return split(cs, limit);
        }
        if (s.length() == 1) {
            return split(cs, s.charAt(0), limit, isTrim, ignoreEmpty, ignoreCase);
        }
        List<String> list = ListUtils.newArrayList();
        int len = cs.length();
        int separatorLen = s.length();
        int start = 0;
        int i = 0;

        while (i < len) {
            i = CharSequenceUtils.indexOf(cs, s, start, ignoreCase);
            if (i <= -1) {
                break;
            }
            addToList(list, cs.substring(start, i), isTrim, ignoreEmpty);
            start = i + separatorLen;
            if (limit > 0 && list.size() > limit - 2) {
                break;
            }
        }

        return addToList(list, cs.substring(start, len), isTrim, ignoreEmpty);
    }

    static List<String> addToList(List<String> list, String part, boolean isTrim, boolean ignoreEmpty) {
        if (isTrim) {
            part = CharSequenceUtils.trim(part);
        }
        if (ObjectUtils.nonNull(part) && (!ignoreEmpty || !part.isEmpty())) {
            list.add(part);
        }
        return list;
    }

    static String[] splitToArray(CharSequence cs, char c) {
        return splitToArray(cs, c, 0);
    }

    static String[] splitToArray(CharSequence s, char c, int i) {
        return ObjectUtils.defaultIfNullElseFunction(s, cs -> splitToArray(cs.toString(), c, i, false, false), StringConstants.EMPTY_STRING_ARRAY);
    }

    static String[] splitToArray(String cs, char c, int i, boolean isTrim, boolean ignoreEmpty) {
        return split(cs, c, i, isTrim, ignoreEmpty).toArray(StringConstants.EMPTY_STRING_ARRAY);
    }

    static String[] splitToArray(String s, int i) {
        return split(s, i).toArray(StringConstants.EMPTY_STRING_ARRAY);
    }

    static String[] splitToArray(String cs, String s, int i, boolean isTrim, boolean ignoreEmpty) {
        return split(cs, s, i, isTrim, ignoreEmpty).toArray(StringConstants.EMPTY_STRING_ARRAY);
    }
}