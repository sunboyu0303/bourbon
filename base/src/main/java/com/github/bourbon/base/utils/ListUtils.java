package com.github.bourbon.base.utils;

import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 11:15
 */
public interface ListUtils {

    static <T> List<T> newList(boolean isLinked) {
        return BooleanUtils.defaultSupplierIfFalse(isLinked, ListUtils::newLinkedList, ListUtils::newArrayList);
    }

    static <T> List<T> newList(boolean isLinked, T... values) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(values, v -> BooleanUtils.defaultSupplierIfFalse(isLinked, ListUtils::newLinkedList, ListUtils::newArrayList), () -> newList(isLinked));
    }

    static <T> List<T> newList(boolean isLinked, Collection<T> collection) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(collection, c -> BooleanUtils.defaultSupplierIfFalse(isLinked, ListUtils::newLinkedList, ListUtils::newArrayList), () -> newList(isLinked));
    }

    static <T> List<T> newList(boolean isLinked, Iterable<T> iterable) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(iterable, i -> BooleanUtils.defaultSupplierIfFalse(isLinked, ListUtils::newLinkedList, ListUtils::newArrayList), () -> newList(isLinked));
    }

    static <T> List<T> newList(boolean isLinked, Iterator<T> iterator) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(iterator, i -> BooleanUtils.defaultSupplierIfFalse(isLinked, ListUtils::newLinkedList, ListUtils::newArrayList), () -> newList(isLinked));
    }

    static <T> List<T> of(T... elements) {
        return BooleanUtils.defaultSupplierIfPredicate(elements, ArrayUtils::isNotEmpty, e -> Collections.unmodifiableList(newArrayList(e)), Collections::emptyList);
    }

    static <E> List<E> newArrayList() {
        return new ArrayList<>();
    }

    static <E> List<E> newArrayList(int i) {
        return new ArrayList<>(i);
    }

    static <E> List<E> newArrayList(E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    static <E> List<E> newArrayList(Collection<E> c) {
        return new ArrayList<>(c);
    }

    static <E> List<E> newArrayList(Iterable<E> iterable) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(iterable, Collection.class, i -> newArrayList((Collection<E>) i), () -> newArrayList(iterable.iterator()));
    }

    static <E> List<E> newArrayList(Iterator<E> i) {
        List<E> list = newArrayList();
        CollectionUtils.addAll(list, i);
        return list;
    }

    static <E> List<E> newLinkedList() {
        return new LinkedList<>();
    }

    static <E> List<E> newLinkedList(E... elements) {
        List<E> list = newLinkedList();
        Collections.addAll(list, elements);
        return list;
    }

    static <E> List<E> newLinkedList(Collection<E> c) {
        return new LinkedList<>(c);
    }

    static <E> List<E> newLinkedList(Iterable<E> iterable) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(iterable, Collection.class, i -> newLinkedList((Collection<E>) i), () -> newLinkedList(iterable.iterator()));
    }

    static <E> List<E> newLinkedList(Iterator<E> i) {
        List<E> list = newLinkedList();
        CollectionUtils.addAll(list, i);
        return list;
    }

    static <E> List<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<>();
    }

    static <E> List<E> newCopyOnWriteArrayList(E... elements) {
        return new CopyOnWriteArrayList<>(elements);
    }

    static <E> List<E> newCopyOnWriteArrayList(Collection<E> c) {
        return new CopyOnWriteArrayList<>(c);
    }

    @SuppressWarnings("unchecked")
    static <E> List<E> newCopyOnWriteArrayList(Iterable<E> i) {
        return new CopyOnWriteArrayList<>(BooleanUtils.defaultSupplierIfAssignableFrom(i, Collection.class, Collection.class::cast, () -> newArrayList(i)));
    }

    static <T> List<T> unmodifiableList(List<T> list) {
        return Collections.unmodifiableList(list);
    }

    static <T> List<T> synchronizedList(List<T> list) {
        return Collections.synchronizedList(list);
    }

    static <T> List<T> merge(List<T>... items) {
        return BooleanUtils.defaultSupplierIfPredicate(items, ArrayUtils::isNotEmpty, i -> BooleanUtils.defaultSupplierIfFalse(
                i.length != 1, () -> Arrays.stream(i).filter(ObjectUtils::nonNull).flatMap(Collection::stream).collect(Collectors.toList()), () -> i[0]
        ), Collections::emptyList);
    }

    static <T> List<T> requireNonEmpty(List<T> l) {
        CollectionUtils.requireNonEmpty(l);
        return l;
    }

    static <T> List<T> requireNonEmpty(List<T> l, String m) {
        CollectionUtils.requireNonEmpty(l, m);
        return l;
    }

    static <T> List<T> requireNonEmpty(List<T> l, Supplier<String> s) {
        CollectionUtils.requireNonEmpty(l, s);
        return l;
    }

    static <T, X extends Throwable> List<T> requireNonEmpty(List<T> l, ThrowableSupplier<X> s) throws X {
        CollectionUtils.requireNonEmpty(l, s);
        return l;
    }

    static <T> List<T> requireNonNull(List<T> l) {
        CollectionUtils.requireNonNull(l);
        return l;
    }

    static <T> List<T> requireNonNull(List<T> l, String m) {
        CollectionUtils.requireNonNull(l, m);
        return l;
    }

    static <T> List<T> requireNonNull(List<T> l, Supplier<String> s) {
        CollectionUtils.requireNonNull(l, s);
        return l;
    }

    static <T, X extends Throwable> List<T> requireNonNull(List<T> l, ThrowableSupplier<X> s) throws X {
        CollectionUtils.requireNonNull(l, s);
        return l;
    }
}