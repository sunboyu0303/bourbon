package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 11:14
 */
public interface CollectionUtils {

    static <T> Collection<T> unmodifiableCollection(Collection<T> c) {
        return Collections.unmodifiableCollection(c);
    }

    static <T> Collection<T> synchronizedCollection(Collection<T> c) {
        return Collections.synchronizedCollection(c);
    }

    static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    static boolean contains(Collection<?> c, Object o) {
        return isNotEmpty(c) && c.contains(o);
    }

    static <E> void addAll(Collection<E> c, Iterator<E> i) {
        while (i.hasNext()) {
            c.add(i.next());
        }
    }

    static String toString(Collection<?> c) {
        return toString(c, StringConstants.COMMA);
    }

    static String toString(Collection<?> c, CharSequence s) {
        return toString(c, s, StringConstants.EMPTY, StringConstants.EMPTY);
    }

    static String toString(Collection<?> c, CharSequence s, String prefix, String suffix) {
        if (isEmpty(c)) {
            return StringConstants.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    static String toCommaDelimitedString(Collection<?> c) {
        return toDelimitedString(c, StringConstants.COMMA);
    }

    static String toDelimitedString(Collection<?> c, String delimit) {
        return toDelimitedString(c, delimit, StringConstants.EMPTY, StringConstants.EMPTY);
    }

    static String toDelimitedString(Collection<?> c, String delimit, String prefix, String suffix) {
        if (isEmpty(c)) {
            return StringConstants.EMPTY;
        }
        int totalLength = c.size() * (prefix.length() + suffix.length()) + (c.size() - 1) * delimit.length();
        Object element;
        for (Iterator<?> iterator = c.iterator(); iterator.hasNext(); totalLength += String.valueOf(element).length()) {
            element = iterator.next();
        }
        StringBuilder sb = new StringBuilder(totalLength);
        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext()) {
            sb.append(prefix).append(iterator.next()).append(suffix);
            if (iterator.hasNext()) {
                sb.append(delimit);
            }
        }
        return sb.toString();
    }

    static String[] toStringArray(Collection<String> collection) {
        return BooleanUtils.defaultIfPredicate(collection, CollectionUtils::isNotEmpty, c -> c.toArray(StringConstants.EMPTY_STRING_ARRAY), StringConstants.EMPTY_STRING_ARRAY);
    }

    static <T> Collection<T> requireNonEmpty(Collection<T> c) {
        if (isEmpty(c)) {
            throw new NullPointerException();
        }
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw new NullPointerException();
            }
        }
        return c;
    }

    static <T> Collection<T> requireNonEmpty(Collection<T> c, String m) {
        if (isEmpty(c)) {
            throw new NullPointerException(m);
        }
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw new NullPointerException(m);
            }
        }
        return c;
    }

    static <T> Collection<T> requireNonEmpty(Collection<T> c, Supplier<String> s) {
        if (isEmpty(c)) {
            throw new NullPointerException(s.get());
        }
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw new NullPointerException(s.get());
            }
        }
        return c;
    }

    static <T, X extends Throwable> Collection<T> requireNonEmpty(Collection<T> c, ThrowableSupplier<X> s) throws X {
        if (isEmpty(c)) {
            throw s.get();
        }
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw s.get();
            }
        }
        return c;
    }

    static <T> Collection<T> requireNonNull(Collection<T> c) {
        ObjectUtils.requireNonNull(c);
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw new NullPointerException();
            }
        }
        return c;
    }

    static <T> Collection<T> requireNonNull(Collection<T> c, String m) {
        ObjectUtils.requireNonNull(c, m);
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw new NullPointerException(m);
            }
        }
        return c;
    }

    static <T> Collection<T> requireNonNull(Collection<T> c, Supplier<String> s) {
        ObjectUtils.requireNonNull(c, s);
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw new NullPointerException(s.get());
            }
        }
        return c;
    }

    static <T, X extends Throwable> Collection<T> requireNonNull(Collection<T> c, ThrowableSupplier<X> s) throws X {
        ObjectUtils.requireNonNull(c, s);
        for (T t : c) {
            if (ObjectUtils.isNull(t)) {
                throw s.get();
            }
        }
        return c;
    }

    static List<?> merge(List<?>... items) {
        return BooleanUtils.defaultSupplierIfPredicate(items, ArrayUtils::isNotEmpty, i -> BooleanUtils.defaultSupplierIfFalse(
                i.length != 1, () -> Arrays.stream(i).filter(ObjectUtils::nonNull).flatMap(Collection::stream).collect(Collectors.toList()), () -> i[0]
        ), Collections::emptyList);
    }

    static Set<?> merge(Set<?>... items) {
        return BooleanUtils.defaultSupplierIfPredicate(items, ArrayUtils::isNotEmpty, i -> BooleanUtils.defaultSupplierIfFalse(
                i.length != 1, () -> Arrays.stream(i).filter(ObjectUtils::nonNull).flatMap(Collection::stream).collect(Collectors.toSet()), () -> i[0]
        ), Collections::emptySet);
    }
}