package com.github.bourbon.cache.ehcache.utils;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.ehcache.config.units.MemoryUnit;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/14 11:33
 */
public final class MemoryUnitUtils {

    private static final Map<String, MemoryUnit> MAP = Arrays.stream(MemoryUnit.values()).collect(Collectors.toMap(MemoryUnit::name, Function.identity()));

    public static MemoryUnit getMemoryUnit(String name) {
        return getMemoryUnit(name, MemoryUnit.MB);
    }

    public static MemoryUnit getMemoryUnit(String name, MemoryUnit memoryUnit) {
        return BooleanUtils.defaultIfPredicate(name, n -> !CharSequenceUtils.isEmpty(n), n -> ObjectUtils.defaultIfNull(MAP.get(n), memoryUnit), memoryUnit);
    }

    private MemoryUnitUtils() {
    }
}