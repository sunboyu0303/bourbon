package com.github.bourbon.base.logger;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/9 15:37
 */
public final class MessageFormatter {

    private static final char DELIMIT_START = CharConstants.LEFT_BRACES;
    private static final String DELIMIT_STR = StringConstants.LEFT_RIGHT_BRACES;
    private static final char ESCAPE_CHAR = CharConstants.BACKSLASH;

    public static FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        Throwable throwableCandidate = getThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = trimmedCopy(argArray);
        }
        return arrayFormat(messagePattern, args, throwableCandidate);
    }

    private static Throwable getThrowableCandidate(Object[] argArray) {
        return BooleanUtils.defaultIfFalse(!ArrayUtils.isEmpty(argArray),
                () -> BooleanUtils.defaultIfAssignableFrom(argArray[argArray.length - 1], Throwable.class, Throwable.class::cast)
        );
    }

    private static Object[] trimmedCopy(Object[] argArray) {
        Assert.notEmpty(argArray, "empty or null argument array");
        int len = argArray.length - 1;
        Object[] trimmed = new Object[len];
        System.arraycopy(argArray, 0, trimmed, 0, len);
        return trimmed;
    }

    private static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray, Throwable throwable) {

        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }

        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }

        int i = 0;
        int j;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int L;
        for (L = 0; L < argArray.length; L++) {
            j = messagePattern.indexOf(DELIMIT_STR, i);
            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                }
                sbuf.append(messagePattern, i, messagePattern.length());
                return new FormattingTuple(sbuf.toString(), argArray, throwable);
            } else {
                if (isEscapedDelimit(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L--;
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append(DELIMIT_START);
                        i = j + 1;
                    } else {
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, argArray[L], MapUtils.newHashMap());
                        i = j + 2;
                    }
                } else {
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, argArray[L], MapUtils.newHashMap());
                    i = j + 2;
                }
            }
        }
        sbuf.append(messagePattern, i, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    private static boolean isEscapedDelimit(String messagePattern, int delimitStartIndex) {
        if (delimitStartIndex == 0) {
            return false;
        }
        return messagePattern.charAt(delimitStartIndex - 1) == ESCAPE_CHAR;
    }

    private static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR;
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append(StringConstants.NULL);
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        } else {
            if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) o);
            } else {
                objectArrayAppend(sbuf, (Object[]) o, seenMap);
            }
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            sbuf.append(o.toString());
        } catch (Exception t) {
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(StringConstants.COMMA_SPACE);
            }
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append(CharConstants.LEFT_BRACKETS);
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(StringConstants.COMMA_SPACE);
                }
            }
            seenMap.remove(a);
        } else {
            sbuf.append(StringConstants.DOTS);
        }
        sbuf.append(CharConstants.RIGHT_BRACKETS);
    }

    public static final class FormattingTuple {

        public static final FormattingTuple NULL = new FormattingTuple(null);

        private final String message;
        private final Throwable throwable;
        private final Object[] argArray;

        private FormattingTuple(String message) {
            this(message, null, null);
        }

        private FormattingTuple(String message, Object[] argArray, Throwable throwable) {
            this.message = message;
            this.throwable = throwable;
            this.argArray = argArray;
        }

        public String getMessage() {
            return message;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public Object[] getArgArray() {
            return argArray;
        }
    }

    private MessageFormatter() {
    }
}