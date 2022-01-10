package com.github.bourbon.cache.core.utils;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 18:09
 */
public enum MemoryUnit {
    B, KB, MB, GB, TB, PB;

    private static final Map<String, MemoryUnit> map = Arrays.stream(values()).collect(Collectors.toMap(Enum::name, Function.identity()));

    public static MemoryUnit getMemoryUnit(String key) {
        return getMemoryUnit(key, null);
    }

    public static MemoryUnit getMemoryUnit(String key, MemoryUnit defaultValue) {
        return ObjectUtils.defaultIfNull(map.get(key), defaultValue);
    }
}