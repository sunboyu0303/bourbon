package com.github.bourbon.springframework.boot.origin;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:34
 */
@FunctionalInterface
public interface OriginLookup<K> {

    Origin getOrigin(K key);

    default boolean isImmutable() {
        return false;
    }

    default String getPrefix() {
        return null;
    }

    @SuppressWarnings("unchecked")
    static <K> Origin getOrigin(Object source, K key) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(source, OriginLookup.class, o -> {
            try {
                return ((OriginLookup<K>) o).getOrigin(key);
            } catch (Exception e) {
                return null;
            }
        });
    }
}