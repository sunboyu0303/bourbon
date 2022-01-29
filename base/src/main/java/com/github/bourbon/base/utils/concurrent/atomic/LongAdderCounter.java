package com.github.bourbon.base.utils.concurrent.atomic;

import com.github.bourbon.base.lang.counter.LongCounter;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 11:13
 */
public class LongAdderCounter extends LongAdder implements LongCounter {

    private static final long serialVersionUID = 2831533224131362405L;

    @Override
    public long value() {
        return longValue();
    }
}