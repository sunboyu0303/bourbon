package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.convert.Convert;
import com.github.bourbon.base.lang.Pair;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 11:16
 */
public interface MapUtils {

    static Map<String, Map<String, String>> splitAll(Map<String, List<String>> map, String separator) {
        return BooleanUtils.defaultSupplierIfPredicate(map, MapUtils::isNotEmpty, m -> m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> split(e.getValue(), separator))), HashMap::new);
    }

    static Map<String, String> split(List<String> list, String separator) {
        Map<String, String> map = newHashMap();
        if (CollectionUtils.isNotEmpty(list)) {
            for (String item : list) {
                int index = item.indexOf(separator);
                if (index == -1) {
                    map.put(item, StringConstants.EMPTY);
                } else {
                    map.put(item.substring(0, index), item.substring(index + 1));
                }
            }
        }
        return map;
    }

    static Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator) {
        return BooleanUtils.defaultSupplierIfPredicate(map, MapUtils::isNotEmpty, m -> m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> join(e.getValue(), separator))), HashMap::new);
    }

    static List<String> join(Map<String, String> map, String separator) {
        List<String> list = ListUtils.newArrayList();
        if (isNotEmpty(map)) {
            map.forEach((k, v) -> {
                if (CharSequenceUtils.isEmpty(v)) {
                    list.add(k);
                } else {
                    list.add(k + separator + v);
                }
            });
        }
        return list;
    }

    static Map<String, String> toMap(String... pairs) {
        Map<String, String> parameters = newHashMap();
        if (ArrayUtils.isNotEmpty(pairs)) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

    static Map<Object, Object> toMap(Object... pairs) {
        Map<Object, Object> ret = newHashMap();
        if (ArrayUtils.isNotEmpty(pairs)) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("Map pairs can not be odd number.");
            }
            int len = pairs.length / 2;
            for (int i = 0; i < len; i++) {
                ret.put(pairs[2 * i], pairs[2 * i + 1]);
            }
        }
        return ret;
    }

    static Map<?, ?> merge(Map<?, ?>... items) {
        return BooleanUtils.defaultSupplierIfPredicate(items, ArrayUtils::isNotEmpty, i -> BooleanUtils.defaultIfFalse(
                i.length != 1, () -> Arrays.stream(i).filter(ObjectUtils::nonNull).flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), i[0]
        ), Collections::emptyMap);
    }

    static <K, V> Map<K, V> of(K key, V value) {
        return of(key, value, false);
    }

    static <K, V> Map<K, V> of(K key, V value, boolean isOrder) {
        Map<K, V> map = newMap(isOrder);
        map.put(key, value);
        return map;
    }

    static <K, V> Map<K, V> ofPair(Pair<K, V>... pairs) {
        return Arrays.stream(pairs).collect(Collectors.toMap(p -> p.r1, p -> p.r2));
    }

    static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    static <K, V> Map<K, V> emptyIfNull(Map<K, V> m) {
        return ObjectUtils.defaultSupplierIfNull(m, Collections::emptyMap);
    }

    static <T extends Map<K, V>, K, V> T defaultIfEmpty(T map, T defaultMap) {
        return BooleanUtils.defaultIfPredicate(map, MapUtils::isNotEmpty, m -> m, defaultMap);
    }

    static <K, V> Map<K, V> newMap(boolean isOrder) {
        return isOrder ? newLinkedHashMap() : newHashMap();
    }

    static <K, V> Map<K, V> newMap(boolean isOrder, int c) {
        return isOrder ? newLinkedHashMap(c) : newHashMap(c);
    }

    static <K, V> Map<K, V> newMap(boolean isOrder, Map<K, V> map) {
        return BooleanUtils.defaultSupplierIfPredicate(map, MapUtils::isNotEmpty, m -> BooleanUtils.defaultSupplierIfFalse(isOrder, MapUtils::newLinkedHashMap, MapUtils::newHashMap), () -> newMap(isOrder));
    }

    static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    static <K, V> Map<K, V> newHashMap(int c) {
        return new HashMap<>(expectedSize(c));
    }

    static <K, V> Map<K, V> newHashMap(Map<K, V> map) {
        return new HashMap<>(map);
    }

    static <K, V> Map<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    static <K, V> Map<K, V> newLinkedHashMap(int c) {
        return new LinkedHashMap<>(expectedSize(c));
    }

    static <K, V> Map<K, V> newLinkedHashMap(Map<K, V> map) {
        return new LinkedHashMap<>(map);
    }

    static <K, V> Map<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    static <K, V> Map<K, V> newConcurrentHashMap(int c) {
        return new ConcurrentHashMap<>(expectedSize(c));
    }

    static <K, V> Map<K, V> newConcurrentHashMap(Map<K, V> map) {
        return new ConcurrentHashMap<>(map);
    }

    static <K extends Comparable, V> Map<K, V> newTreeMap() {
        return new TreeMap<>();
    }

    static <K extends Comparable, V> Map<K, V> newTreeMap(Comparator<K> c) {
        return new TreeMap<>(c);
    }

    static <K extends Comparable, V> Map<K, V> newTreeMap(SortedMap<K, V> m) {
        return new TreeMap<>(m);
    }

    static <K extends Comparable, V> Map<K, V> newTreeMap(Map<K, V> map) {
        return new TreeMap<>(map);
    }

    static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(map, SortedMap.class, m -> unmodifiableSortedMap((SortedMap<K, V>) m), () -> Collections.unmodifiableMap(map));
    }

    static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> m) {
        return Collections.unmodifiableSortedMap(m);
    }

    static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(map, SortedMap.class, m -> synchronizedSortedMap((SortedMap<K, V>) m), () -> Collections.synchronizedMap(map));
    }

    static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> m) {
        return Collections.synchronizedSortedMap(m);
    }

    static int expectedSize(int c) {
        if (c < 16) {
            return 16;
        }
        return c < 1073741824 ? (int) ((float) c / 0.75F + 1.0F) : Integer.MAX_VALUE;
    }

    static String getString(Map<?, ?> m, Object k) {
        return getString(m, k, null);
    }

    static String getString(Map<?, ?> m, Object k, String s) {
        return Convert.toString(m.get(k), s);
    }

    static Byte getByte(Map<?, ?> m, Object k) {
        return getByte(m, k, null);
    }

    static Byte getByte(Map<?, ?> m, Object k, Byte b) {
        return Convert.toByte(m.get(k), b);
    }

    static Short getShort(Map<?, ?> m, Object k) {
        return getShort(m, k, null);
    }

    static Short getShort(Map<?, ?> m, Object k, Short s) {
        return Convert.toShort(m.get(k), s);
    }

    static Integer getInteger(Map<?, ?> m, Object k) {
        return getInteger(m, k, null);
    }

    static Integer getInteger(Map<?, ?> m, Object k, Integer i) {
        return Convert.toInteger(m.get(k), i);
    }

    static Long getLong(Map<?, ?> m, Object k) {
        return getLong(m, k, null);
    }

    static Long getLong(Map<?, ?> m, Object k, Long l) {
        return Convert.toLong(m.get(k), l);
    }

    static Boolean getBoolean(Map<?, ?> m, Object k) {
        return getBoolean(m, k, null);
    }

    static Boolean getBoolean(Map<?, ?> m, Object k, Boolean b) {
        return Convert.toBoolean(m.get(k), b);
    }

    static Character getCharacter(Map<?, ?> m, Object k) {
        return getCharacter(m, k, null);
    }

    static Character getCharacter(Map<?, ?> m, Object k, Character c) {
        return Convert.toCharacter(m.get(k), c);
    }

    static Float getFloat(Map<?, ?> m, Object k) {
        return getFloat(m, k, null);
    }

    static Float getFloat(Map<?, ?> m, Object k, Float f) {
        return Convert.toFloat(m.get(k), f);
    }

    static Double getDouble(Map<?, ?> m, Object k) {
        return getDouble(m, k, null);
    }

    static Double getDouble(Map<?, ?> m, Object k, Double d) {
        return Convert.toDouble(m.get(k), d);
    }

    static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map) {
        if (isEmpty(map)) {
            throw new NullPointerException();
        }
        return map;
    }

    static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, String m) {
        if (isEmpty(map)) {
            throw new NullPointerException(m);
        }
        return map;
    }

    static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, Supplier<String> s) {
        if (isEmpty(map)) {
            throw new NullPointerException(s.get());
        }
        return map;
    }

    static <K, V, X extends Throwable> Map<K, V> requireNonEmpty(Map<K, V> map, ThrowableSupplier<X> s) throws X {
        if (isEmpty(map)) {
            throw s.get();
        }
        return map;
    }

    static <K, V> Map<K, V> requireNonNull(Map<K, V> map) {
        return ObjectUtils.requireNonNull(map);
    }

    static <K, V> Map<K, V> requireNonNull(Map<K, V> map, String m) {
        return ObjectUtils.requireNonNull(map, m);
    }

    static <K, V> Map<K, V> requireNonNull(Map<K, V> map, Supplier<String> s) {
        return ObjectUtils.requireNonNull(map, s);
    }

    static <K, V, X extends Throwable> Map<K, V> requireNonNull(Map<K, V> map, ThrowableSupplier<X> s) throws X {
        return ObjectUtils.requireNonNull(map, s);
    }

    static String mapToString(Map<String, String> map) {
        return BooleanUtils.defaultIfPredicate(map, MapUtils::isNotEmpty, m -> {
            StringJoiner sj = new StringJoiner(StringConstants.AND);
            m.forEach((k, v) -> sj.add(k + StringConstants.EQUAL + v));
            return sj.toString();
        }, StringConstants.EMPTY);
    }

    static void stringToMap(String str, Map<String, String> map) {
        if (CharSequenceUtils.isBlank(str)) {
            return;
        }
        String key = null;
        String value;
        int mark = -1;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case CharConstants.AND:
                    value = str.substring(mark + 1, i);
                    if (key != null) {
                        map.put(key, value);
                    }
                    key = null;
                    mark = i;
                    break;
                case CharConstants.EQUAL:
                    key = str.substring(mark + 1, i);
                    mark = i;
                    break;
                default:
                    break;
            }
        }
        if (key != null) {
            map.put(key, (str.length() - mark - 1) == 0 ? StringConstants.EMPTY : str.substring(mark + 1));
        }
    }
}