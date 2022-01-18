package com.github.bourbon.base.ttl;

import com.github.bourbon.base.ttl.spi.TtlWrapper;
import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 22:55
 */
public final class TtlUnwrap {

    @SuppressWarnings("unchecked")
    public static <T> T unwrap(T obj) {
        return BooleanUtils.defaultIfFalse(isWrapper(obj), () -> (T) ((TtlWrapper) obj).unwrap(), obj);
    }

    public static <T> boolean isWrapper(T obj) {
        return obj instanceof TtlWrapper;
    }

    private TtlUnwrap() {
    }
}