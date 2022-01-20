package com.github.bourbon.base.utils;

import com.github.bourbon.base.utils.concurrent.ConcurrentHashSet;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 11:15
 */
public interface SetUtils {

    static <E> Set<E> newSet(boolean isOrder) {
        return isOrder ? newLinkedHashSet() : newHashSet();
    }

    static <E> Set<E> newSet(boolean isOrder, E... e) {
        return isOrder ? newLinkedHashSet(e) : newHashSet(e);
    }

    static <E> Set<E> newSet(boolean isOrder, int c) {
        return isOrder ? newLinkedHashSet(c) : newHashSet(c);
    }

    static <E> Set<E> newSet(boolean isOrder, Collection<E> c) {
        return isOrder ? newLinkedHashSet(c) : newHashSet(c);
    }

    @SuppressWarnings("unchecked")
    static <E> Set<E> newSet(boolean isOrder, Iterable<E> i) {
        return isOrder ? newLinkedHashSet(i) : newHashSet(i);
    }

    static <E> Set<E> newSet(boolean isOrder, Iterator<E> i) {
        return isOrder ? newLinkedHashSet(i) : newHashSet(i);
    }

    static <E> Set<E> newHashSet() {
        return new HashSet<>();
    }

    static <E> Set<E> newHashSet(E... e) {
        Set<E> set = newHashSet(e.length);
        Collections.addAll(set, e);
        return set;
    }

    static <E> Set<E> newHashSet(int c) {
        return new HashSet<>(MapUtils.expectedSize(c));
    }

    static <E> Set<E> newHashSet(Collection<E> c) {
        return new HashSet<>(c);
    }

    static <E> Set<E> newHashSet(Iterable<E> iterable) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(iterable, Collection.class, i -> newHashSet((Collection<E>) i), () -> newHashSet(iterable.iterator()));
    }

    static <E> Set<E> newHashSet(Iterator<E> i) {
        Set<E> set = newHashSet();
        CollectionUtils.addAll(set, i);
        return set;
    }

    static <E> Set<E> newConcurrentHashSet() {
        return new ConcurrentHashSet<>();
    }

    static <E> Set<E> newConcurrentHashSet(int c) {
        return new ConcurrentHashSet<>(MapUtils.expectedSize(c));
    }

    static <E> Set<E> newConcurrentHashSet(Collection<E> c) {
        return new ConcurrentHashSet<>(c);
    }

    static <E> Set<E> newConcurrentHashSet(Iterable<E> i) {
        return new ConcurrentHashSet<>(i);
    }

    static <E> Set<E> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    static <E> Set<E> newLinkedHashSet(E... e) {
        Set<E> set = newLinkedHashSet(e.length);
        Collections.addAll(set, e);
        return set;
    }

    static <E> Set<E> newLinkedHashSet(int c) {
        return new LinkedHashSet<>(MapUtils.expectedSize(c));
    }

    static <E> Set<E> newLinkedHashSet(Collection<E> c) {
        return new LinkedHashSet<>(c);
    }

    static <E> Set<E> newLinkedHashSet(Iterable<E> iterable) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(iterable, Collection.class, i -> newLinkedHashSet((Collection<E>) i), () -> newLinkedHashSet(iterable.iterator()));
    }

    static <E> Set<E> newLinkedHashSet(Iterator<E> i) {
        Set<E> set = newLinkedHashSet();
        CollectionUtils.addAll(set, i);
        return set;
    }

    static <E extends Comparable> Set<E> newTreeSet() {
        return new TreeSet<>();
    }

    static <E extends Comparable> Set<E> newTreeSet(Collection<E> c) {
        return new TreeSet<>(c);
    }

    static <E extends Comparable> Set<E> newTreeSet(Iterable<E> iterable) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(iterable, Collection.class, i -> newTreeSet((Collection<E>) i), () -> newTreeSet(iterable.iterator()));
    }

    static <E extends Comparable> Set<E> newTreeSet(Iterator<E> i) {
        Set<E> set = newTreeSet();
        CollectionUtils.addAll(set, i);
        return set;
    }

    static <E extends Comparable> Set<E> newTreeSet(Comparator<E> c) {
        return new TreeSet<>(c);
    }

    static <E extends Comparable> Set<E> newTreeSet(SortedSet<E> s) {
        return new TreeSet<>(s);
    }

    static <E> Set<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<>();
    }

    static <E> Set<E> newCopyOnWriteArraySet(Collection<E> c) {
        return new CopyOnWriteArraySet<>(c);
    }

    @SuppressWarnings("unchecked")
    static <E> Set<E> newCopyOnWriteArraySet(Iterable<? extends E> i) {
        return new CopyOnWriteArraySet<>(BooleanUtils.defaultSupplierIfAssignableFrom(i, Collection.class, Collection.class::cast, () -> ListUtils.newArrayList(i)));
    }

    static <E> Set<E> unmodifiableSet(Set<E> set) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(set, SortedSet.class, s -> Collections.unmodifiableSortedSet((SortedSet<E>) s), () -> Collections.unmodifiableSet(set));
    }

    static <E> Set<E> synchronizedSet(Set<E> set) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(set, SortedSet.class, s -> Collections.synchronizedSortedSet((SortedSet<E>) s), () -> Collections.synchronizedSet(set));
    }

    static <E> Set<E> merge(Set<E>... items) {
        return BooleanUtils.defaultSupplierIfPredicate(items, i -> !ArrayUtils.isEmpty(i), i -> BooleanUtils.defaultIfFalse(
                i.length != 1, () -> Arrays.stream(i).filter(ObjectUtils::nonNull).flatMap(Collection::stream).collect(Collectors.toSet()), i[0]
        ), Collections::emptySet);
    }

    static <T> Set<T> requireNonEmpty(Set<T> l) {
        CollectionUtils.requireNonEmpty(l);
        return l;
    }

    static <T> Set<T> requireNonEmpty(Set<T> l, String m) {
        CollectionUtils.requireNonEmpty(l, m);
        return l;
    }

    static <T> Set<T> requireNonEmpty(Set<T> l, Supplier<String> s) {
        CollectionUtils.requireNonEmpty(l, s);
        return l;
    }

    static <T, X extends Throwable> Set<T> requireNonEmpty(Set<T> l, ThrowableSupplier<X> s) throws X {
        CollectionUtils.requireNonEmpty(l, s);
        return l;
    }

    static <T> Set<T> requireNonNull(Set<T> l) {
        CollectionUtils.requireNonNull(l);
        return l;
    }

    static <T> Set<T> requireNonNull(Set<T> l, String m) {
        CollectionUtils.requireNonNull(l, m);
        return l;
    }

    static <T> Set<T> requireNonNull(Set<T> l, Supplier<String> s) {
        CollectionUtils.requireNonNull(l, s);
        return l;
    }

    static <T, X extends Throwable> Set<T> requireNonNull(Set<T> l, ThrowableSupplier<X> s) throws X {
        CollectionUtils.requireNonNull(l, s);
        return l;
    }
}