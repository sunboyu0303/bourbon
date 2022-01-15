package com.github.bourbon.base.convert.impl;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 10:12
 */
public class ObjectToAtomicLongConverter extends ObjectToNumberConverter {

    public ObjectToAtomicLongConverter() {
        super(AtomicLong.class);
    }
}