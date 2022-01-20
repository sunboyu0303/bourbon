package com.github.bourbon.uuid.core.support;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 16:31
 */
@FunctionalInterface
public interface RingBufferUidProvider {

    List<Long> provide(long second);
}