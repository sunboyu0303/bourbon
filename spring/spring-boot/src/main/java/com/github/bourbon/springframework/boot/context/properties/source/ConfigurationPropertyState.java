package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.lang.Assert;

import java.util.function.Predicate;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:37
 */
public enum ConfigurationPropertyState {
    PRESENT, ABSENT, UNKNOWN;

    static <T> ConfigurationPropertyState search(Iterable<T> source, Predicate<T> predicate) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(predicate, "Predicate must not be null");
        for (T item : source) {
            if (predicate.test(item)) {
                return PRESENT;
            }
        }
        return ABSENT;
    }
}