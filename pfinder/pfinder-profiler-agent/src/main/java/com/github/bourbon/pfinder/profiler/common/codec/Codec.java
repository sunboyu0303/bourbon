package com.github.bourbon.pfinder.profiler.common.codec;

/**
 * 编解码器接口类
 *
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:43
 */
public interface Codec<O, T> {

    /**
     * 编码
     */
    O encode(T t);

    /**
     * 解码
     */
    T decode(O o);
}