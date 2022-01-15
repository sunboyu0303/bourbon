package com.github.bourbon.springframework.boot.context.properties.bind;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 21:50
 */
@FunctionalInterface
public interface PlaceholdersResolver {

    PlaceholdersResolver NONE = v -> v;

    Object resolvePlaceholders(Object value);
}