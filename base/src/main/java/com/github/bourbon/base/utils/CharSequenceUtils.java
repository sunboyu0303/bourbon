package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 14:27
 */
public interface CharSequenceUtils {

    static int length(CharSequence cs) {
        return ObjectUtils.defaultIfNullElseFunction(cs, CharSequence::length, 0);
    }

    static boolean isEmpty(CharSequence cs) {
        return ObjectUtils.isNull(cs) || cs.length() == 0;
    }

    static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    static String defaultIfNull(String str) {
        return defaultIfNull(str, StringConstants.EMPTY);
    }

    static String defaultIfNull(String str, String def) {
        return ObjectUtils.defaultIfNull(str, def);
    }

    static String defaultIfNull(CharSequence cs) {
        return defaultIfNull(cs, StringConstants.EMPTY);
    }

    static String defaultIfNull(CharSequence cs, String def) {
        return ObjectUtils.defaultIfNullElseFunction(cs, CharSequence::toString, def);
    }

    static String defaultIfEmpty(String s) {
        return defaultIfEmpty(s, StringConstants.EMPTY);
    }

    static String defaultIfEmpty(String s, String def) {
        return isEmpty(s) ? def : s;
    }

    static String defaultIfEmpty(CharSequence cs) {
        return defaultIfEmpty(cs, StringConstants.EMPTY);
    }

    static String defaultIfEmpty(CharSequence cs, String def) {
        return isEmpty(cs) ? def : cs.toString();
    }

    static String defaultIfBlank(String s) {
        return defaultIfBlank(s, StringConstants.EMPTY);
    }

    static String defaultIfBlank(String s, String def) {
        return isBlank(s) ? def : s;
    }

    static String defaultIfBlank(CharSequence cs) {
        return defaultIfBlank(cs, StringConstants.EMPTY);
    }

    static String defaultIfBlank(CharSequence cs, String def) {
        return isBlank(cs) ? def : cs.toString();
    }

    static boolean isBlank(CharSequence cs) {
        int length;
        if (cs != null && (length = cs.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!CharUtils.isBlankChar(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    static boolean equals(String str1, String str2) {
        return equals(str1, str2, false);
    }

    static boolean equals(CharSequence cs1, CharSequence cs2) {
        return equals(cs1, cs2, false);
    }

    static boolean equals(String str1, String str2, boolean ignore) {
        if (null == str1) {
            return str2 == null;
        }
        if (null == str2) {
            return false;
        }
        return BooleanUtils.defaultSupplierIfFalse(ignore, () -> str1.equalsIgnoreCase(str2), () -> str1.equals(str2));
    }

    static boolean equalsIgnoreCase(String str1, String str2) {
        return equals(str1, str2, true);
    }

    static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return equals(cs1, cs2, true);
    }

    static boolean equals(CharSequence cs1, CharSequence cs2, boolean ignore) {
        if (null == cs1) {
            return cs2 == null;
        }
        if (null == cs2) {
            return false;
        }
        return BooleanUtils.defaultSupplierIfFalse(ignore, () -> cs1.toString().equalsIgnoreCase(cs2.toString()), () -> cs1.toString().contentEquals(cs2));
    }

    static boolean isWord(CharSequence cs) {
        return cs != null && cs.chars().allMatch(c -> CharUtils.isWord((char) c));
    }

    static String join(CharSequence[] c) {
        return join(c, StringConstants.EMPTY);
    }

    static String join(CharSequence[] c, CharSequence cs) {
        return ArrayUtils.isEmpty(c) ? StringConstants.EMPTY : String.join(cs, c);
    }

    static String join(Collection<CharSequence> c) {
        return join(c, StringConstants.EMPTY);
    }

    static String join(Collection<CharSequence> c, CharSequence cs) {
        return CollectionUtils.isEmpty(c) ? StringConstants.EMPTY : String.join(cs, c);
    }

    static String trimAllWhitespace(String cs) {
        if (isEmpty(cs)) {
            return StringConstants.EMPTY;
        }
        int len = cs.length();
        StringBuilder sb = new StringBuilder(len);
        for (char c : cs.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String trimAllWhitespace(CharSequence cs) {
        if (isEmpty(cs)) {
            return StringConstants.EMPTY;
        }
        int len = cs.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = cs.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String trim(CharSequence cs) {
        return ObjectUtils.defaultIfNullElseFunction(cs, s -> trim(s, 0));
    }

    static String trim(CharSequence cs, int i) {
        return trim(cs, i, CharUtils::isBlankChar);
    }

    static String trim(CharSequence cs, int i, Predicate<Character> p) {
        if (cs == null) {
            return null;
        }
        int length = cs.length();
        int start = 0;
        int end = length;
        if (i <= 0) {
            while (start < end && p.test(cs.charAt(start))) {
                ++start;
            }
        }
        if (i >= 0) {
            while (start < end && p.test(cs.charAt(end - 1))) {
                --end;
            }
        }
        return start <= 0 && end >= length ? cs.toString() : cs.toString().substring(start, end);
    }

    static String trim(String str) {
        return trim(str, null);
    }

    static String trim(String str, String stripChars) {
        return trim(str, stripChars, 0);
    }

    static String trimStart(String str) {
        return trimStart(str, null);
    }

    static String trimStart(String str, String stripChars) {
        return trim(str, stripChars, -1);
    }

    static String trimEnd(String str) {
        return trimEnd(str, null);
    }

    static String trimEnd(String str, String stripChars) {
        return trim(str, stripChars, 1);
    }

    static String trimToNull(String str) {
        return trimToNull(str, null);
    }

    static String trimToNull(String str, String stripChars) {
        String result = trim(str, stripChars);
        return result != null && result.length() != 0 ? result : null;
    }

    static String trimToEmpty(String str) {
        return trimToEmpty(str, null);
    }

    static String trimToEmpty(String str, String stripChars) {
        return ObjectUtils.defaultIfNull(trim(str, stripChars), StringConstants.EMPTY);
    }

    static String trim(String str, String stripChars, int mode) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        int start = 0;
        int end = length;
        if (mode <= 0) {
            if (stripChars == null) {
                while (start < end && Character.isWhitespace(str.charAt(start))) {
                    ++start;
                }
            } else {
                if (stripChars.length() == 0) {
                    return str;
                }
                while (start < end && stripChars.indexOf(str.charAt(start)) != -1) {
                    ++start;
                }
            }
        }
        if (mode >= 0) {
            if (stripChars == null) {
                while (start < end && Character.isWhitespace(str.charAt(end - 1))) {
                    --end;
                }
            } else {
                if (stripChars.length() == 0) {
                    return str;
                }
                while (start < end && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                    --end;
                }
            }
        }
        return start <= 0 && end >= length ? str : str.substring(start, end);
    }

    static boolean contains(CharSequence cs, char c) {
        return indexOf(cs, c) > -1;
    }

    static int indexOf(CharSequence cs, char c) {
        return indexOf(cs, c, 0);
    }

    static int indexOf(CharSequence cs, char c, int i) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(cs, String.class, s -> ((String) s).indexOf(c, i), () -> indexOf(cs, c, i, -1));
    }

    static int indexOf(CharSequence cs, char c, int start, int end) {
        if (isEmpty(cs)) {
            return -1;
        }
        int len = cs.length();
        if (start < 0 || start > len) {
            start = 0;
        }
        if (end > len || end < 0) {
            end = len;
        }
        for (int i = start; i < end; ++i) {
            if (cs.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    static int indexOf(CharSequence cs1, CharSequence cs2, int fromIndex, boolean ignoreCase) {
        if (cs1 != null && cs2 != null) {
            if (fromIndex < 0) {
                fromIndex = 0;
            }
            int endLimit = cs1.length() - cs2.length() + 1;
            if (fromIndex > endLimit) {
                return -1;
            }
            if (cs2.length() == 0) {
                return fromIndex;
            }
            if (!ignoreCase) {
                return cs1.toString().indexOf(cs2.toString(), fromIndex);
            }
            for (int i = fromIndex; i < endLimit; ++i) {
                if (isSubEquals(cs1, i, cs2, 0, cs2.length(), true)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static boolean contains(CharSequence cs, CharSequence searchCs) {
        return isNotEmpty(cs) && isNotEmpty(searchCs) && cs.toString().contains(searchCs);
    }

    static boolean containsAny(CharSequence cs, char[] chars) {
        if (isNotEmpty(cs)) {
            for (int i = 0; i < cs.length(); ++i) {
                if (PrimitiveArrayUtils.contains(chars, cs.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean containsAll(CharSequence cs, char[] chars) {
        if (isNotEmpty(cs)) {
            for (int i = 0; i < cs.length(); ++i) {
                if (!PrimitiveArrayUtils.contains(chars, cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    static List<String> split(CharSequence cs, char c) {
        return split(cs, c, 0);
    }

    static List<String> split(CharSequence cs, char c, int i) {
        return split(cs, c, i, false, false);
    }

    static List<String> split(CharSequence cs, char c, int i, boolean isTrim, boolean ignoreEmpty) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(cs, s -> StringSplitter.split(s.toString(), c, i, isTrim, ignoreEmpty), ListUtils::newArrayList);
    }

    static String[] splitToArray(CharSequence cs, CharSequence cs2) {
        return ObjectUtils.defaultIfNullElseFunction(cs, cs1 -> StringSplitter.splitToArray(cs1.toString(), toString(cs2), 0, false, false), StringConstants.EMPTY_STRING_ARRAY);
    }

    static String toString(CharSequence cs) {
        return ObjectUtils.defaultIfNullElseFunction(cs, CharSequence::toString);
    }

    static boolean isSubEquals(CharSequence cs1, int i1, CharSequence cs2, int i2, int length, boolean ignoreCase) {
        return (null != cs1 && null != cs2) && cs1.toString().regionMatches(ignoreCase, i1, cs2.toString(), i2, length);
    }

    static String addPrefixIfNot(CharSequence cs, CharSequence prefix) {
        return prependIfMissing(cs, prefix, prefix);
    }

    static String prependIfMissing(CharSequence str, CharSequence prefix, CharSequence... prefixes) {
        return prependIfMissing(str, prefix, false, prefixes);
    }

    static String prependIfMissingIgnoreCase(CharSequence str, CharSequence prefix, CharSequence... prefixes) {
        return prependIfMissing(str, prefix, true, prefixes);
    }

    static String prependIfMissing(CharSequence cs, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
        if (cs != null && isNotEmpty(prefix) && !startWith(cs, prefix, ignoreCase)) {
            if (ArrayUtils.isNotEmpty(prefixes)) {
                for (CharSequence s : prefixes) {
                    if (startWith(cs, s, ignoreCase)) {
                        return cs.toString();
                    }
                }
            }
            return prefix.toString().concat(cs.toString());
        }
        return toString(cs);
    }

    static boolean startWith(CharSequence cs, CharSequence prefix, boolean ignore) {
        return startWith(cs, prefix, ignore, false);
    }

    static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
        if (null != str && null != prefix) {
            return BooleanUtils.defaultSupplierIfFalse(ignoreCase, () -> str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase()), () -> str.toString().startsWith(prefix.toString())) && (!ignoreEquals || !equals(str, prefix, ignoreCase));
        }
        return ignoreEquals && null == str && null == prefix;
    }

    static String addSuffixIfNot(CharSequence cs, CharSequence suffix) {
        return appendIfMissing(cs, suffix, suffix);
    }

    static String appendIfMissing(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
        return appendIfMissing(str, suffix, false, suffixes);
    }

    static String appendIfMissingIgnoreCase(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
        return appendIfMissing(str, suffix, true, suffixes);
    }

    static String appendIfMissing(CharSequence str, CharSequence suffix, boolean ignoreCase, CharSequence... suffixes) {
        if (str != null && isNotEmpty(suffix) && !endWith(str, suffix, ignoreCase)) {
            if (ArrayUtils.isNotEmpty(suffixes)) {
                for (CharSequence testSuffix : suffixes) {
                    if (endWith(str, testSuffix, ignoreCase)) {
                        return str.toString();
                    }
                }
            }
            return str.toString().concat(suffix.toString());
        }
        return toString(str);
    }

    static boolean endWith(CharSequence cs, CharSequence suffix) {
        return endWith(cs, suffix, false);
    }

    static boolean endWithIgnoreCase(CharSequence cs, CharSequence suffix) {
        return endWith(cs, suffix, true);
    }

    static boolean endWith(CharSequence cs, CharSequence suffix, boolean isIgnoreCase) {
        if (null != cs && null != suffix) {
            return BooleanUtils.defaultSupplierIfFalse(isIgnoreCase, () -> cs.toString().toLowerCase().endsWith(suffix.toString().toLowerCase()), () -> cs.toString().endsWith(suffix.toString()));
        }
        return null == cs && null == suffix;
    }

    static String removePreAndLowerFirst(CharSequence cs, int preLength) {
        return ObjectUtils.defaultIfNullElseFunction(cs, str -> {
            if (str.length() > preLength) {
                char first = Character.toLowerCase(str.charAt(preLength));
                return str.length() > preLength + 1 ? first + str.toString().substring(preLength + 1) : String.valueOf(first);
            }
            return str.toString();
        });
    }

    static String toUnderlineCase(CharSequence str) {
        return NamingCaseUtils.toUnderlineCase(str);
    }

    static String lowerFirst(CharSequence cs) {
        return ObjectUtils.defaultIfNullElseFunction(cs, str -> {
            if (str.length() > 0) {
                char firstChar = str.charAt(0);
                if (Character.isUpperCase(firstChar)) {
                    return Character.toLowerCase(firstChar) + subSuf(str, 1);
                }
            }
            return str.toString();
        });
    }

    static String upperFirst(CharSequence cs) {
        return ObjectUtils.defaultIfNullElseFunction(cs, str -> {
            if (str.length() > 0) {
                char firstChar = str.charAt(0);
                if (Character.isLowerCase(firstChar)) {
                    return Character.toUpperCase(firstChar) + subSuf(str, 1);
                }
            }
            return str.toString();
        });
    }

    static String subPre(CharSequence cs, int toIndex) {
        return sub(cs, 0, toIndex);
    }

    static String subSuf(CharSequence cs, int fromIndex) {
        return BooleanUtils.defaultIfPredicate(cs, CharSequenceUtils::isNotEmpty, s -> sub(s, fromIndex, s.length()));
    }

    static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return toString(str);
        }
        int len = str.length();
        if (fromIndex < 0) {
            fromIndex += len;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }
        if (toIndex < 0) {
            toIndex += len;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }
        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        return fromIndex == toIndex ? StringConstants.EMPTY : str.subSequence(fromIndex, toIndex).toString();
    }

    static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (start < 0) {
            start += len;
        }
        if (start < 0) {
            start = 0;
        }
        return start > len ? StringConstants.EMPTY : str.substring(start);
    }

    static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (end < 0) {
            end += len;
        }
        if (start < 0) {
            start += len;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            return StringConstants.EMPTY;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    static String left(String str, int len) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> BooleanUtils.defaultIfFalse(len > 0, () -> s.length() <= len ? s : s.substring(0, len), StringConstants.EMPTY));
    }

    static String right(String str, int len) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> BooleanUtils.defaultIfFalse(len > 0, () -> s.length() <= len ? s : s.substring(s.length() - len), StringConstants.EMPTY));
    }

    static String substringBefore(String str, String separator) {
        if (str != null && separator != null && str.length() != 0) {
            return BooleanUtils.defaultIfFalse(separator.length() != 0, () -> BooleanUtils.defaultIfPredicate(str.indexOf(separator), pos -> pos != -1, pos -> str.substring(0, pos), str), StringConstants.EMPTY);
        }
        return str;
    }

    static String substringAfter(String str, String separator) {
        if (isNotEmpty(str)) {
            return ObjectUtils.defaultIfNullElseFunction(separator, s -> BooleanUtils.defaultIfPredicate(str.indexOf(s), pos -> pos != -1, pos -> str.substring(pos + s.length()), StringConstants.EMPTY), StringConstants.EMPTY);
        }
        return str;
    }

    static String substringBeforeLast(String str, String separator) {
        if (isNotEmpty(str) && isNotEmpty(separator)) {
            return BooleanUtils.defaultIfPredicate(str.lastIndexOf(separator), pos -> pos != -1, pos -> str.substring(0, pos), str);
        }
        return str;
    }

    static String substringAfterLast(String str, String separator) {
        if (isNotEmpty(str)) {
            if (isNotEmpty(separator)) {
                int pos = str.lastIndexOf(separator);
                return pos != -1 && pos != str.length() - separator.length() ? str.substring(pos + separator.length()) : StringConstants.EMPTY;
            }
            return StringConstants.EMPTY;
        }
        return str;
    }

    static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag, 0);
    }

    static String substringBetween(String str, String open, String close) {
        return substringBetween(str, open, close, 0);
    }

    static String substringBetween(String str, String open, String close, int fromIndex) {
        if (str != null && open != null && close != null) {
            int start = str.indexOf(open, fromIndex);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }
            return null;
        }
        return null;
    }

    static String removePrefix(CharSequence str, CharSequence prefix) {
        if (isNotEmpty(str) && isNotEmpty(prefix)) {
            String str2 = str.toString();
            return str2.startsWith(prefix.toString()) ? subSuf(str2, prefix.length()) : str2;
        }
        return toString(str);
    }

    static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isNotEmpty(str) && isNotEmpty(prefix)) {
            String str2 = str.toString();
            return str2.toLowerCase().startsWith(prefix.toString().toLowerCase()) ? subSuf(str2, prefix.length()) : str2;
        }
        return toString(str);
    }

    static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (isNotEmpty(str) && isNotEmpty(suffix)) {
            String str2 = str.toString();
            return str2.endsWith(suffix.toString()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        }
        return toString(str);
    }

    static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (isNotEmpty(str) && isNotEmpty(suffix)) {
            String str2 = str.toString();
            return str2.toLowerCase().endsWith(suffix.toString().toLowerCase()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        }
        return toString(str);
    }

    static String upperFirstAndAddPre(CharSequence str, String preString) {
        return str != null && preString != null ? preString + upperFirst(str) : null;
    }

    static String removeAll(CharSequence str, char... chars) {
        if (null != str && PrimitiveArrayUtils.isNotEmpty(chars)) {
            int len = str.length();
            if (0 == len) {
                return toString(str);
            }
            StringBuilder builder = new StringBuilder(len);
            for (int i = 0; i < len; ++i) {
                char c = str.charAt(i);
                if (!PrimitiveArrayUtils.contains(chars, c)) {
                    builder.append(c);
                }
            }
            return builder.toString();
        }
        return toString(str);
    }

    static String[] commaDelimitedListToStringArray(String s) {
        return delimitedListToStringArray(s, StringConstants.COMMA);
    }

    static String[] delimitedListToStringArray(String s, String delimiter) {
        return delimitedListToStringArray(s, delimiter, null);
    }

    static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return StringConstants.EMPTY_STRING_ARRAY;
        }
        if (delimiter == null) {
            return ArrayUtils.of(str);
        }
        List<String> result = ListUtils.newArrayList();
        int pos;
        if (delimiter.isEmpty()) {
            for (pos = 0; pos < str.length(); ++pos) {
                result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
            }
        } else {
            int delPos;
            for (pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
            }
            if (str.length() > 0 && pos <= str.length()) {
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return CollectionUtils.toStringArray(result);
    }

    static String deleteAny(String inString, String charsToDelete) {
        if (isNotEmpty(inString) && isNotEmpty(charsToDelete)) {
            StringBuilder sb = new StringBuilder(inString.length());
            for (char c : inString.toCharArray()) {
                if (charsToDelete.indexOf(c) == -1) {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return inString;
    }

    static String requireNotEmpty(CharSequence cs) {
        if (isEmpty(cs)) {
            throw new NullPointerException();
        }
        return cs.toString();
    }

    static String requireNotEmpty(CharSequence cs, String s) {
        if (isEmpty(cs)) {
            throw new NullPointerException(s);
        }
        return cs.toString();
    }

    static String requireNotEmpty(CharSequence cs, Supplier<String> s) {
        if (isEmpty(cs)) {
            throw new NullPointerException(s.get());
        }
        return cs.toString();
    }

    static <X extends Throwable> String requireNotEmpty(CharSequence cs, ThrowableSupplier<X> s) throws X {
        if (isEmpty(cs)) {
            throw s.get();
        }
        return cs.toString();
    }

    static String requireNotBlank(CharSequence cs) {
        if (isBlank(cs)) {
            throw new NullPointerException();
        }
        return cs.toString();
    }

    static String requireNotBlank(CharSequence cs, String s) {
        if (isBlank(cs)) {
            throw new NullPointerException(s);
        }
        return cs.toString();
    }

    static String requireNotBlank(CharSequence cs, Supplier<String> s) {
        if (isBlank(cs)) {
            throw new NullPointerException(s.get());
        }
        return cs.toString();
    }

    static <X extends Throwable> String requireNotBlank(CharSequence cs, ThrowableSupplier<X> s) throws X {
        if (isBlank(cs)) {
            throw s.get();
        }
        return cs.toString();
    }

    static int countMatches(String str, char c) {
        int count = 0;
        if (isNotEmpty(str)) {
            for (char ch : str.toCharArray()) {
                if (ch == c) {
                    count++;
                }
            }
        }
        return count;
    }
}