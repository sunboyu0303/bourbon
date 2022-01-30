package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 11:17
 */
public interface ArrayUtils extends PrimitiveArrayUtils {

    static int length(Object o) {
        return ObjectUtils.defaultIfNull(o, Array::getLength, 0);
    }

    static <T> boolean isEmpty(T[] arr) {
        return ObjectUtils.isNull(arr) || arr.length == 0;
    }

    static <T> boolean isNotEmpty(T[] arr) {
        return !isEmpty(arr);
    }

    static boolean isEmpty(Object o) {
        return ObjectUtils.isNull(o) || (o.getClass().isArray() && 0 == Array.getLength(o));
    }

    static boolean isArray(Object o) {
        return ObjectUtils.nonNull(o) && o.getClass().isArray();
    }

    @SuppressWarnings("unchecked")
    static <T> T get(Object o, int index) {
        if (ObjectUtils.isNull(o)) {
            return null;
        }
        if (index < 0) {
            index += Array.getLength(o);
        }
        try {
            return (T) Array.get(o, index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    static <T> T[] getAny(Object o, int[] indexes) {
        if (ObjectUtils.isNull(o)) {
            return null;
        }
        T[] result = newArray(o.getClass().getComponentType(), indexes.length);
        for (int i : indexes) {
            result[i] = get(o, i);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }

    static Object[] newArray(int newSize) {
        return new Object[newSize];
    }

    static Class<?> getComponentType(Object object) {
        return ObjectUtils.defaultIfNull(object, o -> o.getClass().getComponentType());
    }

    static Class<?> getComponentType(Class<?> clazz) {
        return ObjectUtils.defaultIfNull(clazz, Class::getComponentType);
    }

    static Class<?> getArrayType(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }

    static Class<?> getArrayType(Object o) {
        return getArrayType(o.getClass());
    }

    static <T> boolean hasNull(T[] array) {
        if (!isEmpty(array)) {
            for (T e : array) {
                if (ObjectUtils.isNull(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    static <T> boolean isAllNull(T[] array) {
        return ObjectUtils.isNull(firstNonNull(array));
    }

    static <T> T firstNonNull(T[] array) {
        return firstMatch(ObjectUtils::nonNull, array);
    }

    static <T> T firstMatch(Predicate<T> p, T[] array) {
        return BooleanUtils.defaultIfPredicate(matchIndex(p, array), i -> i >= 0, i -> array[i]);
    }

    static <T> boolean contains(T[] array, T t) {
        return indexOf(array, t, 0) != -1;
    }

    static <T> int indexOf(T[] array, T t) {
        return matchIndex(o -> ObjectUtils.equals(t, o), array);
    }

    static <T> int indexOf(T[] array, T t, int i) {
        return matchIndex(o -> ObjectUtils.equals(t, o), i, array);
    }

    static <T> int matchIndex(Predicate<T> p, T[] array) {
        return matchIndex(p, 0, array);
    }

    static <T> int matchIndex(Predicate<T> p, int idx, T[] array) {
        ObjectUtils.requireNonNull(p, "Predicate must be not null !");
        if (!isEmpty(array)) {
            for (int i = idx; i < array.length; ++i) {
                if (p.test(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    static <T> T[] requireNonEmpty(T[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        Arrays.stream(array).forEach(ObjectUtils::requireNonNull);
        return array;
    }

    static <T> T[] requireNonEmpty(T[] array, String m) {
        if (isEmpty(array)) {
            throw new NullPointerException(m);
        }
        Arrays.stream(array).forEach(t -> ObjectUtils.requireNonNull(t, m));
        return array;
    }

    static <T> T[] requireNonEmpty(T[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        Arrays.stream(array).forEach(t -> ObjectUtils.requireNonNull(t, s));
        return array;
    }

    static <T, X extends Throwable> T[] requireNonEmpty(T[] array, ThrowableSupplier<X> s) throws X {
        if (isEmpty(array)) {
            throw s.get();
        }
        for (T t : array) {
            ObjectUtils.requireNonNull(t, s);
        }
        return array;
    }

    static <T> T[] requireNonNull(T[] array) {
        ObjectUtils.requireNonNull(array);
        Arrays.stream(array).forEach(ObjectUtils::requireNonNull);
        return array;
    }

    static <T> T[] requireNonNull(T[] array, String m) {
        ObjectUtils.requireNonNull(array, m);
        Arrays.stream(array).forEach(t -> ObjectUtils.requireNonNull(t, m));
        return array;
    }

    static <T> T[] requireNonNull(T[] array, Supplier<String> s) {
        ObjectUtils.requireNonNull(array, s);
        Arrays.stream(array).forEach(t -> ObjectUtils.requireNonNull(t, s));
        return array;
    }

    static <T, X extends Throwable> T[] requireNonNull(T[] array, ThrowableSupplier<X> s) throws X {
        ObjectUtils.requireNonNull(array, s);
        for (T t : array) {
            ObjectUtils.requireNonNull(t, s);
        }
        return array;
    }

    static <T> T[] append(T[] arr, T[] newArr) {
        return BooleanUtils.defaultIfPredicate(arr, a -> !isEmpty(a), a -> insert(a, a.length, newArr), newArr);
    }

    @SuppressWarnings("unchecked")
    static <T> T[] insert(T[] arr, int index, T[] newArr) {
        return (T[]) insert((Object) arr, index, newArr);
    }

    static <T> Object insert(Object o, int index, T[] newElements) {
        if (isEmpty(newElements)) {
            return o;
        }
        if (isEmpty(o)) {
            return newElements;
        }
        int len = length(o);
        if (index < 0) {
            index = index % len + len;
        }
        T[] result = newArray(o.getClass().getComponentType(), Math.max(len, index) + newElements.length);
        System.arraycopy(o, 0, result, 0, Math.min(len, index));
        System.arraycopy(newElements, 0, result, index, newElements.length);
        if (index < len) {
            System.arraycopy(o, index, result, index + newElements.length, len - index);
        }
        return result;
    }

    static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        if (isArray(obj)) {
            try {
                return Arrays.deepToString(((Object[]) obj));
            } catch (Exception e) {
                // ignore
            }
        }
        return obj.toString();
    }

    static Object[] merge(Object[][] items) {
        if (ArrayUtils.isEmpty(items)) {
            return new Object[0];
        }
        int len = items.length;
        int i = 0;
        Object[] item = null;
        while (i < len && ObjectUtils.isNull(item = items[i])) {
            i++;
        }
        if (i == len) {
            return new Object[0];
        }
        Class<?> type = getComponentType(item);
        int totalLen = 0;
        for (; i < len; i++) {
            item = items[i];
            if (ArrayUtils.isEmpty(item)) {
                continue;
            }
            if (getComponentType(item) != type) {
                throw new IllegalArgumentException("Arguments' types are different");
            }
            totalLen += item.length;
        }

        if (totalLen == 0) {
            return new Object[0];
        }

        Object result = Array.newInstance(type, totalLen);
        int index = 0;
        for (Object[] array : items) {
            if (ArrayUtils.isNotEmpty(array)) {
                for (Object o : array) {
                    Array.set(result, index++, o);
                }
            }
        }
        return (Object[]) result;
    }

    static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals(((Object[]) o1), ((Object[]) o2));
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return PrimitiveArrayUtils.equals(((boolean[]) o1), ((boolean[]) o2));
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return PrimitiveArrayUtils.equals(((char[]) o1), ((char[]) o2));
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return PrimitiveArrayUtils.equals(((float[]) o1), ((float[]) o2));
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return PrimitiveArrayUtils.equals(((double[]) o1), ((double[]) o2));
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return PrimitiveArrayUtils.equals(((byte[]) o1), ((byte[]) o2));
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return PrimitiveArrayUtils.equals(((short[]) o1), ((short[]) o2));
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return PrimitiveArrayUtils.equals(((int[]) o1), ((int[]) o2));
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return PrimitiveArrayUtils.equals(((long[]) o1), ((long[]) o2));
        }
        return false;
    }

    static int nullSafeHashCode(Object o) {
        if (o instanceof Object[]) {
            return nullSafeHashCode(((Object[]) o));
        }
        if (o instanceof boolean[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((boolean[]) o));
        }
        if (o instanceof char[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((char[]) o));
        }
        if (o instanceof float[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((float[]) o));
        }
        if (o instanceof double[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((double[]) o));
        }
        if (o instanceof byte[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((byte[]) o));
        }
        if (o instanceof short[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((short[]) o));
        }
        if (o instanceof int[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((int[]) o));
        }
        if (o instanceof long[]) {
            return PrimitiveArrayUtils.nullSafeHashCode(((long[]) o));
        }
        return o.hashCode();
    }

    static int nullSafeHashCode(Object[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (Object element : array) {
            hash = 31 * hash + ObjectUtils.nullSafeHashCode(element);
        }
        return hash;
    }

    static String nullSafeToString(Object o) {
        if (o instanceof Object[]) {
            return nullSafeToString(((Object[]) o));
        }
        if (o instanceof boolean[]) {
            return PrimitiveArrayUtils.nullSafeToString(((boolean[]) o));
        }
        if (o instanceof char[]) {
            return PrimitiveArrayUtils.nullSafeToString(((char[]) o));
        }
        if (o instanceof float[]) {
            return PrimitiveArrayUtils.nullSafeToString(((float[]) o));
        }
        if (o instanceof double[]) {
            return PrimitiveArrayUtils.nullSafeToString(((double[]) o));
        }
        if (o instanceof byte[]) {
            return PrimitiveArrayUtils.nullSafeToString(((byte[]) o));
        }
        if (o instanceof short[]) {
            return PrimitiveArrayUtils.nullSafeToString(((short[]) o));
        }
        if (o instanceof int[]) {
            return PrimitiveArrayUtils.nullSafeToString(((int[]) o));
        }
        if (o instanceof long[]) {
            return PrimitiveArrayUtils.nullSafeToString(((long[]) o));
        }
        return ObjectUtils.defaultIfNull(o.toString(), StringConstants.EMPTY);
    }

    static String nullSafeToString(Object[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner sj = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        Arrays.stream(array).forEach(o -> sj.add(String.valueOf(o)));
        return sj.toString();
    }

    static String toCommaDelimitedString(Object[] arr) {
        return toDelimitedString(arr, StringConstants.COMMA);
    }

    static String toDelimitedString(Object[] arr, String delimit) {
        if (ObjectUtils.isEmpty(arr)) {
            return StringConstants.EMPTY;
        }
        if (arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        }
        StringJoiner sj = new StringJoiner(delimit);
        Arrays.stream(arr).forEach(o -> sj.add(String.valueOf(o)));
        return sj.toString();
    }

    static <T> T[] of(T... values) {
        return values;
    }

    @SuppressWarnings("unchecked")
    static <T> T[] expand(T[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        Class<T> clazz = (Class<T>) getComponentType(src);
        T[] tmp = (T[]) Array.newInstance(clazz, src.length + capacity);
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static Object[] copyOf(Object[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        Object[] tmp = new Object[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    Object[] EMPTY_OBJECT_ARRAY = {};

    static Object[] defaultIfNull(Object[] array) {
        return defaultIfNull(array, EMPTY_OBJECT_ARRAY);
    }

    static Object[] defaultIfNull(Object[] array, Object[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static Object[] defaultIfEmpty(Object[] array) {
        return defaultIfEmpty(array, EMPTY_OBJECT_ARRAY);
    }

    static Object[] defaultIfEmpty(Object[] array, Object[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }
}