package com.github.bourbon.base.convert.impl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 10:12
 */
public class ObjectToAtomicIntegerConverter extends ObjectToNumberConverter {

    public ObjectToAtomicIntegerConverter() {
        super(AtomicInteger.class);
    }
}