package com.github.bourbon.base.disruptor.translator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:19
 */
public interface EventTranslatorThreeArg<T, A, B, C> {

    void translateTo(T event, long sequence, A arg0, B arg1, C arg2);
}