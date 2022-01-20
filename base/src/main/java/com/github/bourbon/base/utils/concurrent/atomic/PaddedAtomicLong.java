package com.github.bourbon.base.utils.concurrent.atomic;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 14:54
 */
public class PaddedAtomicLong extends AtomicLong {
    private static final long serialVersionUID = -3817316181931790046L;

    private volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public PaddedAtomicLong(long initialValue) {
        super(initialValue);
    }

    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}