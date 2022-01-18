package com.github.bourbon.base.utils.function;

import java.util.function.Predicate;

import static java.util.stream.Stream.of;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 16:31
 */
public interface Predicates {

    Predicate[] EMPTY_ARRAY = new Predicate[0];

    static <T> Predicate<T> alwaysTrue() {
        return e -> true;
    }

    static <T> Predicate<T> alwaysFalse() {
        return e -> false;
    }

    static <T> Predicate<T> and(Predicate<T>[] predicates) {
        return of(predicates).reduce(Predicate::and).orElseGet(Predicates::alwaysTrue);
    }

    static <T> Predicate<T> or(Predicate<T>[] predicates) {
        return of(predicates).reduce(Predicate::or).orElse(alwaysTrue());
    }
}