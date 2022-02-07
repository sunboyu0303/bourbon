package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.*;
import com.github.bourbon.base.lang.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 17:25
 */
public final class NumberUtils {

    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);

    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    public static double add(float v1, float v2) {
        return add(ArrayUtils.of(Float.toString(v1), Float.toString(v2))).doubleValue();
    }

    public static double add(float v1, double v2) {
        return add(ArrayUtils.of(Float.toString(v1), Double.toString(v2))).doubleValue();
    }

    public static double add(double v1, float v2) {
        return add(ArrayUtils.of(Double.toString(v1), Float.toString(v2))).doubleValue();
    }

    public static double add(double v1, double v2) {
        return add(ArrayUtils.of(Double.toString(v1), Double.toString(v2))).doubleValue();
    }

    public static BigDecimal add(Number[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }
        Number value = values[0];
        BigDecimal result = toBigDecimal(value);
        for (int i = 1; i < values.length; ++i) {
            value = values[i];
            if (null != value) {
                result = result.add(toBigDecimal(value));
            }
        }
        return result;
    }

    public static BigDecimal add(String[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }
        String value = values[0];
        BigDecimal result = toBigDecimal(value);

        for (int i = 1; i < values.length; ++i) {
            value = values[i];
            if (CharSequenceUtils.isNotBlank(value)) {
                result = result.add(toBigDecimal(value));
            }
        }

        return result;
    }

    public static BigDecimal add(BigDecimal[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }
        BigDecimal value = values[0];
        BigDecimal result = toBigDecimal(value);

        for (int i = 1; i < values.length; ++i) {
            value = values[i];
            if (null != value) {
                result = result.add(value);
            }
        }

        return result;
    }

    public static double sub(float v1, float v2) {
        return sub(ArrayUtils.of(Float.toString(v1), Float.toString(v2))).doubleValue();
    }

    public static double sub(float v1, double v2) {
        return sub(ArrayUtils.of(Float.toString(v1), Double.toString(v2))).doubleValue();
    }

    public static double sub(double v1, float v2) {
        return sub(ArrayUtils.of(Double.toString(v1), Float.toString(v2))).doubleValue();
    }

    public static double sub(double v1, double v2) {
        return sub(ArrayUtils.of(Double.toString(v1), Double.toString(v2))).doubleValue();
    }

    public static BigDecimal sub(Number[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }
        Number value = values[0];
        BigDecimal result = toBigDecimal(value);

        for (int i = 1; i < values.length; ++i) {
            value = values[i];
            if (null != value) {
                result = result.subtract(toBigDecimal(value));
            }
        }

        return result;
    }

    public static BigDecimal sub(String[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }
        String value = values[0];
        BigDecimal result = toBigDecimal(value);

        for (int i = 1; i < values.length; ++i) {
            value = values[i];
            if (CharSequenceUtils.isNotBlank(value)) {
                result = result.subtract(toBigDecimal(value));
            }
        }

        return result;
    }

    public static BigDecimal sub(BigDecimal[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }
        BigDecimal value = values[0];
        BigDecimal result = toBigDecimal(value);

        for (int i = 1; i < values.length; ++i) {
            value = values[i];
            if (null != value) {
                result = result.subtract(value);
            }
        }

        return result;
    }

    public static double mul(float v1, float v2) {
        return mul(Float.toString(v1), Float.toString(v2)).doubleValue();
    }

    public static double mul(float v1, double v2) {
        return mul(Float.toString(v1), Double.toString(v2)).doubleValue();
    }

    public static double mul(double v1, float v2) {
        return mul(Double.toString(v1), Float.toString(v2)).doubleValue();
    }

    public static double mul(double v1, double v2) {
        return mul(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    public static BigDecimal mul(Number[] values) {
        if (ArrayUtils.isNotEmpty(values) && !ArrayUtils.hasNull(values)) {
            Number value = values[0];
            BigDecimal result = new BigDecimal(value.toString());

            for (int i = 1; i < values.length; ++i) {
                value = values[i];
                result = result.multiply(new BigDecimal(value.toString()));
            }

            return result;
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal mul(String v1, String v2) {
        return mul(new BigDecimal[]{new BigDecimal(v1), new BigDecimal(v2)});
    }

    public static BigDecimal mul(String[] values) {
        if (ArrayUtils.isNotEmpty(values) && !ArrayUtils.hasNull(values)) {
            BigDecimal result = new BigDecimal(values[0]);

            for (int i = 1; i < values.length; ++i) {
                result = result.multiply(new BigDecimal(values[i]));
            }

            return result;
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal mul(BigDecimal[] values) {
        if (ArrayUtils.isNotEmpty(values) && !ArrayUtils.hasNull(values)) {
            BigDecimal result = values[0];

            for (int i = 1; i < values.length; ++i) {
                result = result.multiply(values[i]);
            }

            return result;
        }
        return BigDecimal.ZERO;
    }

    public static double div(float v1, float v2) {
        return div(v1, v2, 10);
    }

    public static double div(float v1, double v2) {
        return div(v1, v2, 10);
    }

    public static double div(double v1, float v2) {
        return div(v1, v2, 10);
    }

    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    public static double div(Double v1, Double v2) {
        return div(v1, v2, 10);
    }

    public static BigDecimal div(Number v1, Number v2) {
        return div(v1, v2, 10);
    }

    public static BigDecimal div(String v1, String v2) {
        return div(v1, v2, 10);
    }

    public static double div(float v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(float v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(double v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(double v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(Double v1, Double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal div(Number v1, Number v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal div(String v1, String v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    public static double div(float v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }

    public static double div(double v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }

    public static double div(Double v1, Double v2, int scale, RoundingMode roundingMode) {
        return div((Number) v1, (Number) v2, scale, roundingMode).doubleValue();
    }

    public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
        return div(v1.toString(), v2.toString(), scale, roundingMode);
    }

    public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
        return div(toBigDecimal(v1), toBigDecimal(v2), scale, roundingMode);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        Assert.notNull(v2, "Divisor must be not null !");
        if (null == v1) {
            return BigDecimal.ZERO;
        } else {
            if (scale < 0) {
                scale = -scale;
            }
            return v1.divide(v2, scale, roundingMode);
        }
    }

    public static BigDecimal toBigDecimal(Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        }
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        if (number instanceof Long) {
            return new BigDecimal((Long) number);
        }
        if (number instanceof Integer) {
            return new BigDecimal((Integer) number);
        }
        return BooleanUtils.defaultSupplierIfAssignableFrom(number, BigInteger.class, bi -> new BigDecimal((BigInteger) bi), () -> toBigDecimal(number.toString()));
    }

    public static BigDecimal toBigDecimal(String numberStr) {
        return BooleanUtils.defaultIfPredicate(numberStr, CharSequenceUtils::isNotBlank, s -> {
            try {
                Number n = parseNumber(s);
                return BooleanUtils.defaultSupplierIfAssignableFrom(n, BigDecimal.class, BigDecimal.class::cast, () -> new BigDecimal(n.toString()));
            } catch (Exception e) {
                return new BigDecimal(s);
            }
        }, BigDecimal.ZERO);
    }

    public static BigInteger toBigInteger(Number number) {
        return ObjectUtils.defaultIfNullElseFunction(number, n -> BooleanUtils.defaultSupplierIfAssignableFrom(
                n, BigInteger.class, BigInteger.class::cast, () -> BooleanUtils.defaultSupplierIfAssignableFrom(
                        n, Long.class, l -> BigInteger.valueOf((Long) l), () -> toBigInteger(n.longValue())
                )
        ), BigInteger.ZERO);
    }

    public static BigInteger toBigInteger(String number) {
        return BooleanUtils.defaultIfPredicate(number, CharSequenceUtils::isNotBlank, BigInteger::new, BigInteger.ZERO);
    }

    public static boolean isNumber(CharSequence str) {
        if (CharSequenceUtils.isBlank(str)) {
            return false;
        }
        char[] chars = str.toString().toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        int start = chars[0] != CharConstants.HYPHEN && chars[0] != CharConstants.PLUS ? 0 : 1;
        int i;
        if (sz > start + 1 && chars[start] == CharConstants.ZERO && (chars[start + 1] == CharConstants.SMALL_X || chars[start + 1] == CharConstants.X)) {
            i = start + 2;
            if (i == sz) {
                return false;
            }
            while (i < chars.length) {
                if ((chars[i] < CharConstants.ZERO || chars[i] > CharConstants.NINE) && (chars[i] < CharConstants.SMALL_A || chars[i] > CharConstants.SMALL_F) && (chars[i] < CharConstants.A || chars[i] > CharConstants.F)) {
                    return false;
                }
                ++i;
            }
            return true;
        } else {
            --sz;
            for (i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                if (chars[i] >= CharConstants.ZERO && chars[i] <= CharConstants.NINE) {
                    foundDigit = true;
                    allowSigns = false;
                } else if (chars[i] == CharConstants.DOT) {
                    if (hasDecPoint || hasExp) {
                        return false;
                    }
                    hasDecPoint = true;
                } else if (chars[i] != CharConstants.SMALL_E && chars[i] != CharConstants.E) {
                    if (chars[i] != CharConstants.PLUS && chars[i] != CharConstants.HYPHEN) {
                        return false;
                    }
                    if (!allowSigns) {
                        return false;
                    }
                    allowSigns = false;
                    foundDigit = false;
                } else {
                    if (hasExp) {
                        return false;
                    }
                    if (!foundDigit) {
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                }
            }

            if (i < chars.length) {
                if (chars[i] >= CharConstants.ZERO && chars[i] <= CharConstants.NINE) {
                    return true;
                }
                if (chars[i] != CharConstants.SMALL_E && chars[i] != CharConstants.E) {
                    if (chars[i] == CharConstants.DOT) {
                        return (!hasDecPoint && !hasExp) && foundDigit;
                    }
                    if (allowSigns || chars[i] != CharConstants.SMALL_D && chars[i] != CharConstants.D && chars[i] != CharConstants.SMALL_F && chars[i] != CharConstants.F) {
                        return (chars[i] == CharConstants.SMALL_L || chars[i] == CharConstants.L) && (foundDigit && !hasExp);
                    }
                    return foundDigit;
                }
                return false;
            } else {
                return !allowSigns && foundDigit;
            }
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return s.contains(StringConstants.DOT);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int parseInt(String number) {
        if (CharSequenceUtils.isBlank(number)) {
            return IntConstants.DEFAULT;
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).intValue();
        }
    }

    public static long parseLong(String number) {
        if (CharSequenceUtils.isBlank(number)) {
            return LongConstants.DEFAULT;
        }
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).longValue();
        }
    }

    public static float parseFloat(String number) {
        if (CharSequenceUtils.isBlank(number)) {
            return FloatConstants.DEFAULT;
        }
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).floatValue();
        }
    }

    public static double parseDouble(String number) {
        if (CharSequenceUtils.isBlank(number)) {
            return DoubleConstants.DEFAULT;
        }
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).doubleValue();
        }
    }

    public static Number parseNumber(String numberStr) throws NumberFormatException {
        try {
            NumberFormat format = NumberFormat.getInstance();
            if (format instanceof DecimalFormat) {
                ((DecimalFormat) format).setParseBigDecimal(true);
            }
            return format.parse(numberStr);
        } catch (ParseException e) {
            NumberFormatException nfe = new NumberFormatException(e.getMessage());
            nfe.initCause(e);
            throw nfe;
        }
    }

    public static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;

        if (value.startsWith(StringConstants.HYPHEN)) {
            negative = true;
            index++;
        }

        if (value.startsWith(HexUtils.ZERO_SMALL_X, index) || value.startsWith(HexUtils.ZERO_X, index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith(StringConstants.POUND, index)) {
            index++;
            radix = 16;
        } else if (value.startsWith(StringConstants.ZERO, index) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return (negative ? result.negate() : result);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T parseNumber(String s, Class<T> c) {
        Assert.notNull(s, "Text must not be null");
        Assert.notNull(c, "Target class must not be null");
        String trimmed = CharSequenceUtils.trimAllWhitespace(s);

        if (Byte.class == c) {
            return (T) (HexUtils.isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
        }
        if (Short.class == c) {
            return (T) (HexUtils.isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
        }
        if (Integer.class == c) {
            return (T) (HexUtils.isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
        }
        if (Long.class == c) {
            return (T) (HexUtils.isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
        }
        if (Float.class == c) {
            return (T) Float.valueOf(trimmed);
        }
        if (Double.class == c) {
            return (T) Double.valueOf(trimmed);
        }
        if (BigInteger.class == c) {
            return (T) (HexUtils.isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
        }
        if (BigDecimal.class == c || Number.class == c) {
            return (T) new BigDecimal(trimmed);
        }
        throw new IllegalArgumentException("Cannot convert String [" + s + "] to target class [" + c.getName() + "]");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number n, Class<T> c) throws IllegalArgumentException {

        Assert.notNull(n, "Number must not be null");
        Assert.notNull(c, "Target class must not be null");

        if (c.isInstance(n)) {
            return (T) n;
        }
        if (Byte.class == c) {
            long value = checkedLongValue(n, c);
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                raiseOverflowException(n, c);
            }
            return (T) Byte.valueOf(n.byteValue());
        }
        if (Short.class == c) {
            long value = checkedLongValue(n, c);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(n, c);
            }
            return (T) Short.valueOf(n.shortValue());
        }
        if (Integer.class == c) {
            long value = checkedLongValue(n, c);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(n, c);
            }
            return (T) Integer.valueOf(n.intValue());
        }
        if (Long.class == c) {
            return (T) Long.valueOf(checkedLongValue(n, c));
        }
        if (Float.class == c) {
            return (T) Float.valueOf(n.floatValue());
        }
        if (Double.class == c) {
            return (T) Double.valueOf(n.doubleValue());
        }
        if (BigInteger.class == c) {
            return BooleanUtils.defaultSupplierIfAssignableFrom(n, BigDecimal.class, bd -> (T) ((BigDecimal) bd).toBigInteger(), () -> (T) BigInteger.valueOf(n.longValue()));
        }
        if (BigDecimal.class == c) {
            return (T) new BigDecimal(n.toString());
        }
        throw new IllegalArgumentException("Could not convert number [" + n + "] of type [" + n.getClass().getName() + "] to unsupported target class [" + c.getName() + "]");
    }

    private static long checkedLongValue(Number n, Class<? extends Number> c) {
        BigInteger bigInt = null;
        if (n instanceof BigInteger) {
            bigInt = (BigInteger) n;
        } else if (n instanceof BigDecimal) {
            bigInt = ((BigDecimal) n).toBigInteger();
        }
        if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
            raiseOverflowException(n, c);
        }
        return n.longValue();
    }

    private static void raiseOverflowException(Number n, Class<?> c) {
        throw new IllegalArgumentException("Could not convert number [" + n + "] of type [" + n.getClass().getName() + "] to target class [" + c.getName() + "]: overflow");
    }

    private NumberUtils() {
    }
}