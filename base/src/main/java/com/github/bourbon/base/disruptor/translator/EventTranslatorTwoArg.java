package com.github.bourbon.base.disruptor.translator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:18
 */
public interface EventTranslatorTwoArg<T, A, B> {

    void translateTo(T event, long sequence, A arg0, B arg1);
}