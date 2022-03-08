package com.github.bourbon.base.disruptor.translator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:17
 */
public interface EventTranslatorOneArg<T, A> {

    void translateTo(T event, long sequence, A arg0);
}