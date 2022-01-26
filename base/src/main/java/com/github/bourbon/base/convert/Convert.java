package com.github.bourbon.base.convert;

import com.github.bourbon.base.constant.ByteConstants;
import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.constant.LongConstants;
import com.github.bourbon.base.constant.ShortConstants;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 23:04
 */
@SuppressWarnings("unchecked")
public final class Convert {

    private static final ExtensionLoader<Converter> loader = ScopeModelUtils.getExtensionLoader(Converter.class);

    public static Boolean toBoolean(String s) {
        return toBoolean(s, null);
    }

    private static final Converter<String, Boolean> STRING_TO_BOOLEAN = (Converter<String, Boolean>) loader.getExtension("string-to-boolean");

    public static Boolean toBoolean(String s, Boolean b) {
        return STRING_TO_BOOLEAN.convert(s, b);
    }

    public static Boolean toBoolean(Object o) {
        return toBoolean(o, null);
    }

    private static final Converter<Object, Boolean> OBJECT_TO_BOOLEAN = (Converter<Object, Boolean>) loader.getExtension("object-to-boolean");

    public static Boolean toBoolean(Object o, Boolean b) {
        return OBJECT_TO_BOOLEAN.convert(o, b);
    }

    public static Character toCharacter(String s) {
        return toCharacter(s, null);
    }

    private static final Converter<String, Character> STRING_TO_CHARACTER = (Converter<String, Character>) loader.getExtension("string-to-character");

    public static Character toCharacter(String s, Character c) {
        return STRING_TO_CHARACTER.convert(s, c);
    }

    public static Character toCharacter(Object o) {
        return toCharacter(o, null);
    }

    private static final Converter<Object, Character> OBJECT_TO_CHARACTER = (Converter<Object, Character>) loader.getExtension("object-to-character");

    public static Character toCharacter(Object o, Character c) {
        return OBJECT_TO_CHARACTER.convert(o, c);
    }

    public static Float toFloat(String s) {
        return toFloat(s, null);
    }

    private static final Converter<String, Float> STRING_TO_FLOAT = (Converter<String, Float>) loader.getExtension("string-to-float");

    public static Float toFloat(String s, Float f) {
        return STRING_TO_FLOAT.convert(s, f);
    }

    public static Float toFloat(Object o) {
        return toFloat(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_FLOAT = (Converter<Object, Number>) loader.getExtension("object-to-float");

    public static Float toFloat(Object o, Float f) {
        return (Float) OBJECT_TO_FLOAT.convert(o, f);
    }

    public static Double toDouble(String s) {
        return toDouble(s, null);
    }

    private static final Converter<String, Double> STRING_TO_DOUBLE = (Converter<String, Double>) loader.getExtension("string-to-double");

    public static Double toDouble(String s, Double d) {
        return STRING_TO_DOUBLE.convert(s, d);
    }

    public static Double toDouble(Object o) {
        return toDouble(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_DOUBLE = (Converter<Object, Number>) loader.getExtension("object-to-double");

    public static Double toDouble(Object o, Double d) {
        return (Double) OBJECT_TO_DOUBLE.convert(o, d);
    }

    public static Byte toByte(String s) {
        return toByte(s, null);
    }

    private static final Converter<String, Byte> STRING_TO_BYTE = (Converter<String, Byte>) loader.getExtension("string-to-byte");

    public static Byte toByte(String s, Byte b) {
        return STRING_TO_BYTE.convert(s, b);
    }

    public static Byte toByte(Object o) {
        return toByte(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_BYTE = (Converter<Object, Number>) loader.getExtension("object-to-byte");

    public static Byte toByte(Object o, Byte b) {
        return (Byte) OBJECT_TO_BYTE.convert(o, b);
    }

    public static Byte toByteNatural(String s, Byte def) {
        return BooleanUtils.defaultIfPredicate(toByte(s), r -> r != null && r >= ByteConstants.DEFAULT, t -> t, def);
    }

    public static Byte toByteNatural(Object o, Byte def) {
        return BooleanUtils.defaultIfPredicate(toByte(o), r -> r != null && r >= ByteConstants.DEFAULT, t -> t, def);
    }

    public static Byte toBytePositive(String s, Byte def) {
        return BooleanUtils.defaultIfPredicate(toByte(s), r -> r != null && r > ByteConstants.DEFAULT, t -> t, def);
    }

    public static Byte toBytePositive(Object o, Byte def) {
        return BooleanUtils.defaultIfPredicate(toByte(o), r -> r != null && r > ByteConstants.DEFAULT, t -> t, def);
    }

    public static Short toShort(String s) {
        return toShort(s, null);
    }

    private static final Converter<String, Short> STRING_TO_SHORT = (Converter<String, Short>) loader.getExtension("string-to-short");

    public static Short toShort(String s, Short def) {
        return STRING_TO_SHORT.convert(s, def);
    }

    public static Short toShort(Object o) {
        return toShort(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_SHORT = (Converter<Object, Number>) loader.getExtension("object-to-short");

    public static Short toShort(Object o, Short def) {
        return (Short) OBJECT_TO_SHORT.convert(o, def);
    }

    public static Short toShortNatural(String s, Short def) {
        return BooleanUtils.defaultIfPredicate(toShort(s), r -> r != null && r >= ShortConstants.DEFAULT, t -> t, def);
    }

    public static Short toShortNatural(Object o, Short def) {
        return BooleanUtils.defaultIfPredicate(toShort(o), r -> r != null && r >= ShortConstants.DEFAULT, t -> t, def);
    }

    public static Short toShortPositive(String s, Short def) {
        return BooleanUtils.defaultIfPredicate(toShort(s), r -> r != null && r > ShortConstants.DEFAULT, t -> t, def);
    }

    public static Short toShortPositive(Object o, Short def) {
        return BooleanUtils.defaultIfPredicate(toShort(o), r -> r != null && r > ShortConstants.DEFAULT, t -> t, def);
    }

    public static Integer toInteger(String s) {
        return toInteger(s, null);
    }

    private static final Converter<String, Integer> STRING_TO_INTEGER = (Converter<String, Integer>) loader.getExtension("string-to-integer");

    public static Integer toInteger(String s, Integer i) {
        return STRING_TO_INTEGER.convert(s, i);
    }

    public static Integer toInteger(Object o) {
        return toInteger(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_INTEGER = (Converter<Object, Number>) loader.getExtension("object-to-integer");

    public static Integer toInteger(Object o, Integer i) {
        return (Integer) OBJECT_TO_INTEGER.convert(o, i);
    }

    public static Integer toIntegerNatural(String s, Integer def) {
        return BooleanUtils.defaultIfPredicate(toInteger(s), r -> r != null && r >= IntConstants.DEFAULT, t -> t, def);
    }

    public static Integer toIntegerNatural(Object o, Integer def) {
        return BooleanUtils.defaultIfPredicate(toInteger(o), r -> r != null && r >= IntConstants.DEFAULT, t -> t, def);
    }

    public static Integer toIntegerPositive(String s, Integer def) {
        return BooleanUtils.defaultIfPredicate(toInteger(s), r -> r != null && r > IntConstants.DEFAULT, t -> t, def);
    }

    public static Integer toIntegerPositive(Object o, Integer def) {
        return BooleanUtils.defaultIfPredicate(toInteger(o), r -> r != null && r > IntConstants.DEFAULT, t -> t, def);
    }

    public static Long toLong(String s) {
        return toLong(s, null);
    }

    private static final Converter<String, Long> STRING_TO_LONG = (Converter<String, Long>) loader.getExtension("string-to-long");

    public static Long toLong(String s, Long l) {
        return STRING_TO_LONG.convert(s, l);
    }

    public static Long toLong(Object o) {
        return toLong(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_LONG = (Converter<Object, Number>) loader.getExtension("object-to-long");

    public static Long toLong(Object o, Long l) {
        return (Long) OBJECT_TO_LONG.convert(o, l);
    }

    public static Long toLongNatural(String s, Long def) {
        return BooleanUtils.defaultIfPredicate(toLong(s), r -> r != null && r >= LongConstants.DEFAULT, t -> t, def);
    }

    public static Long toLongNatural(Object o, Long def) {
        return BooleanUtils.defaultIfPredicate(toLong(o), r -> r != null && r >= LongConstants.DEFAULT, t -> t, def);
    }

    public static Long toLongPositive(String s, Long def) {
        return BooleanUtils.defaultIfPredicate(toLong(s), r -> r != null && r > LongConstants.DEFAULT, t -> t, def);
    }

    public static Long toLongPositive(Object o, Long def) {
        return BooleanUtils.defaultIfPredicate(toLong(o), r -> r != null && r > LongConstants.DEFAULT, t -> t, def);
    }

    public static AtomicInteger toAtomicInteger(String s) {
        return toAtomicInteger(s, null);
    }

    private static final Converter<String, AtomicInteger> STRING_TO_ATOMIC_INTEGER = (Converter<String, AtomicInteger>) loader.getExtension("string-to-atomic-integer");

    public static AtomicInteger toAtomicInteger(String s, AtomicInteger i) {
        return STRING_TO_ATOMIC_INTEGER.convert(s, i);
    }

    public static AtomicInteger toAtomicInteger(Object o) {
        return toAtomicInteger(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_ATOMIC_INTEGER = (Converter<Object, Number>) loader.getExtension("object-to-atomic-integer");

    public static AtomicInteger toAtomicInteger(Object o, AtomicInteger i) {
        return (AtomicInteger) OBJECT_TO_ATOMIC_INTEGER.convert(o, i);
    }

    public static AtomicLong toAtomicLong(String s) {
        return toAtomicLong(s, null);
    }

    private static final Converter<String, AtomicLong> STRING_TO_ATOMIC_LONG = (Converter<String, AtomicLong>) loader.getExtension("string-to-atomic-long");

    public static AtomicLong toAtomicLong(String s, AtomicLong l) {
        return STRING_TO_ATOMIC_LONG.convert(s, l);
    }

    public static AtomicLong toAtomicLong(Object o) {
        return toAtomicLong(o, null);
    }

    private static final Converter<Object, Number> OBJECT_TO_ATOMIC_LONG = (Converter<Object, Number>) loader.getExtension("object-to-atomic-long");

    public static AtomicLong toAtomicLong(Object o, AtomicLong l) {
        return (AtomicLong) OBJECT_TO_ATOMIC_LONG.convert(o, l);
    }

    public static String toString(Object o) {
        return toString(o, null);
    }

    private static final Converter<Object, String> OBJECT_TO_STRING = (Converter<Object, String>) loader.getExtension("object-to-string");

    public static String toString(Object o, String s) {
        return OBJECT_TO_STRING.convert(o, s);
    }

    public static Date toDate(Object o) {
        return toDate(o, null);
    }

    private static final Converter<Object, Date> OBJECT_TO_DATE = (Converter<Object, Date>) loader.getExtension("object-to-date");

    public static Date toDate(Object o, Date d) {
        return OBJECT_TO_DATE.convert(o, d);
    }

    private Convert() {
    }
}