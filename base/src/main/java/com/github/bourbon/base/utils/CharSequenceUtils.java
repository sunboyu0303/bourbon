package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.ArrayList;
import java.util.Arrays;
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
        return ObjectUtils.defaultIfNull(cs, CharSequence::length, 0);
    }

    static boolean isEmpty(CharSequence cs) {
        return ObjectUtils.isNull(cs) || cs.length() == 0;
    }

    static String nullToDefault(CharSequence cs) {
        return nullToDefault(cs, StringConstants.EMPTY);
    }

    static String nullToDefault(CharSequence cs, String s) {
        return ObjectUtils.isNull(cs) ? s : cs.toString();
    }

    static String emptyToDefault(CharSequence cs, String s) {
        return isEmpty(cs) ? s : cs.toString();
    }

    static String blankToDefault(CharSequence cs, String s) {
        return isBlank(cs) ? s : cs.toString();
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

    static boolean isEquals(CharSequence s1, CharSequence s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }

    static boolean isWord(CharSequence cs) {
        return cs != null && cs.chars().allMatch(c -> CharUtils.isWord((char) c));
    }

    static String join(CharSequence[] sequences) {
        return BooleanUtils.defaultIfPredicate(sequences, s -> !ArrayUtils.isEmpty(s), s -> {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(s).forEach(sb::append);
            return sb.toString();
        }, StringConstants.EMPTY);
    }

    static String join(CharSequence[] c, CharSequence cs) {
        return ArrayUtils.isEmpty(c) ? StringConstants.EMPTY : String.join(cs, c);
    }

    static String join(Collection<CharSequence> c, CharSequence cs) {
        return CollectionUtils.isEmpty(c) ? StringConstants.EMPTY : String.join(cs, c);
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
        return ObjectUtils.defaultIfNull(cs, s -> trim(s, 0));
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
        return !isEmpty(cs) && !isEmpty(searchCs) && cs.toString().contains(searchCs);
    }

    static boolean containsAny(CharSequence cs, char[] chars) {
        if (!isEmpty(cs)) {
            for (int i = 0; i < cs.length(); ++i) {
                if (PrimitiveArrayUtils.contains(chars, cs.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean containsAll(CharSequence cs, char[] chars) {
        if (!isEmpty(cs)) {
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
        return ObjectUtils.defaultSupplierIfNull(cs, s -> StringSplitter.split(s.toString(), c, i, isTrim, ignoreEmpty), ListUtils::newArrayList);
    }

    static String[] splitToArray(CharSequence cs, CharSequence cs2) {
        return ObjectUtils.defaultIfNull(cs, cs1 -> StringSplitter.splitToArray(cs1.toString(), toString(cs2), 0, false, false), StringConstants.EMPTY_STRING_ARRAY);
    }

    static String toString(CharSequence cs) {
        return ObjectUtils.defaultIfNull(cs, CharSequence::toString);
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
        if (cs != null && !isEmpty(prefix) && !startWith(cs, prefix, ignoreCase)) {
            if (!ArrayUtils.isEmpty(prefixes)) {
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
            boolean isStartWith;
            if (ignoreCase) {
                isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
            } else {
                isStartWith = str.toString().startsWith(prefix.toString());
            }
            return isStartWith && (!ignoreEquals || !equals(str, prefix, ignoreCase));
        }
        return ignoreEquals && null == str && null == prefix;
    }

    static boolean equals(CharSequence cs1, CharSequence cs2) {
        return equals(cs1, cs2, false);
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
        if (str != null && !isEmpty(suffix) && !endWith(str, suffix, ignoreCase)) {
            if (!ArrayUtils.isEmpty(suffixes)) {
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
            return isIgnoreCase ? cs.toString().toLowerCase().endsWith(suffix.toString().toLowerCase()) : cs.toString().endsWith(suffix.toString());
        }
        return null == cs && null == suffix;
    }

    static String removePreAndLowerFirst(CharSequence cs, int preLength) {
        return ObjectUtils.defaultIfNull(cs, str -> {
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
        return ObjectUtils.defaultIfNull(cs, str -> {
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
        return ObjectUtils.defaultIfNull(cs, str -> {
            if (str.length() > 0) {
                char firstChar = str.charAt(0);
                if (Character.isLowerCase(firstChar)) {
                    return Character.toUpperCase(firstChar) + subSuf(str, 1);
                }
            }
            return str.toString();
        });
    }

    static String subPre(CharSequence cs, int toIndexExclude) {
        return sub(cs, 0, toIndexExclude);
    }

    static String subSuf(CharSequence cs, int fromIndex) {
        return BooleanUtils.defaultIfPredicate(cs, s -> !isEmpty(s), s -> sub(s, fromIndex, s.length()));
    }

    static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
        if (isEmpty(str)) {
            return toString(str);
        }

        int len = str.length();
        if (fromIndexInclude < 0) {
            fromIndexInclude += len;
            if (fromIndexInclude < 0) {
                fromIndexInclude = 0;
            }
        } else if (fromIndexInclude > len) {
            fromIndexInclude = len;
        }

        if (toIndexExclude < 0) {
            toIndexExclude += len;
            if (toIndexExclude < 0) {
                toIndexExclude = len;
            }
        } else if (toIndexExclude > len) {
            toIndexExclude = len;
        }

        if (toIndexExclude < fromIndexInclude) {
            int tmp = fromIndexInclude;
            fromIndexInclude = toIndexExclude;
            toIndexExclude = tmp;
        }

        return fromIndexInclude == toIndexExclude ? StringConstants.EMPTY : str.toString().substring(fromIndexInclude, toIndexExclude);
    }

    static String removePrefix(CharSequence str, CharSequence prefix) {
        if (!isEmpty(str) && !isEmpty(prefix)) {
            String str2 = str.toString();
            return str2.startsWith(prefix.toString()) ? subSuf(str2, prefix.length()) : str2;
        }
        return toString(str);
    }

    static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (!isEmpty(str) && !isEmpty(prefix)) {
            String str2 = str.toString();
            return str2.toLowerCase().startsWith(prefix.toString().toLowerCase()) ? subSuf(str2, prefix.length()) : str2;
        }
        return toString(str);
    }

    static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (!isEmpty(str) && !isEmpty(suffix)) {
            String str2 = str.toString();
            return str2.endsWith(suffix.toString()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        }
        return toString(str);
    }

    static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (!isEmpty(str) && !isEmpty(suffix)) {
            String str2 = str.toString();
            return str2.toLowerCase().endsWith(suffix.toString().toLowerCase()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        }
        return toString(str);
    }

    static String upperFirstAndAddPre(CharSequence str, String preString) {
        return str != null && preString != null ? preString + upperFirst(str) : null;
    }

    static String removeAll(CharSequence str, char... chars) {
        if (null != str && !ArrayUtils.isEmpty(chars)) {
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
        List<String> result = new ArrayList<>();
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
        if (!isEmpty(inString) && !isEmpty(charsToDelete)) {
            int lastCharIndex = 0;
            char[] result = new char[inString.length()];
            for (int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if (charsToDelete.indexOf(c) == -1) {
                    result[lastCharIndex++] = c;
                }
            }
            return lastCharIndex == inString.length() ? inString : new String(result, 0, lastCharIndex);
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
}