package com.github.bourbon.base.disruptor.translator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:20
 */
public interface EventTranslatorVararg<T> {

    void translateTo(T event, long sequence, final Object... args);
}