package com.github.bourbon.base.disruptor.translator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:16
 */
public interface EventTranslator<T> {

    void translateTo(T event, long sequence);
}