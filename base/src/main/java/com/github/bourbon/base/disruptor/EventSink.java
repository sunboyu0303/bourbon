package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.translator.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 10:56
 */
public interface EventSink<E> {

    void publishEvent(EventTranslator<E> translator);

    <A> void publishEvent(EventTranslatorOneArg<E, A> translator, A arg0);

    <A, B> void publishEvent(EventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1);

    <A, B, C> void publishEvent(EventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2);

    void publishEvent(EventTranslatorVararg<E> translator, Object... args);

    boolean tryPublishEvent(EventTranslator<E> translator);

    <A> boolean tryPublishEvent(EventTranslatorOneArg<E, A> translator, A arg0);

    <A, B> boolean tryPublishEvent(EventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1);

    <A, B, C> boolean tryPublishEvent(EventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2);

    boolean tryPublishEvent(EventTranslatorVararg<E> translator, Object... args);

    void publishEvents(EventTranslator<E>[] translators);

    <A> void publishEvents(EventTranslatorOneArg<E, A> translator, A[] arg0);

    <A, B> void publishEvents(EventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1);

    <A, B, C> void publishEvents(EventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2);

    void publishEvents(EventTranslatorVararg<E> translator, Object[]... args);

    void publishEvents(EventTranslator<E>[] translators, int batchStartsAt, int batchSize);

    <A> void publishEvents(EventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0);

    <A, B> void publishEvents(EventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1);

    <A, B, C> void publishEvents(EventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1, C[] arg2);

    void publishEvents(EventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args);

    boolean tryPublishEvents(EventTranslator<E>[] translators);

    <A> boolean tryPublishEvents(EventTranslatorOneArg<E, A> translator, A[] arg0);

    <A, B> boolean tryPublishEvents(EventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1);

    <A, B, C> boolean tryPublishEvents(EventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2);

    boolean tryPublishEvents(EventTranslatorVararg<E> translator, Object[]... args);

    boolean tryPublishEvents(EventTranslator<E>[] translators, int batchStartsAt, int batchSize);

    <A> boolean tryPublishEvents(EventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0);

    <A, B> boolean tryPublishEvents(EventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1);

    <A, B, C> boolean tryPublishEvents(EventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1, C[] arg2);

    boolean tryPublishEvents(EventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args);
}