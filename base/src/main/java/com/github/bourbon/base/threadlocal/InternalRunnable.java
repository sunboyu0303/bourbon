package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 10:50
 */
public class InternalRunnable implements Runnable {

    private final Runnable runnable;

    private InternalRunnable(Runnable r) {
        runnable = r;
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } finally {
            InternalThreadLocal.removeAll();
        }
    }

    public static Runnable wrap(Runnable r) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(r, InternalRunnable.class, t -> t, () -> new InternalRunnable(r));
    }
}