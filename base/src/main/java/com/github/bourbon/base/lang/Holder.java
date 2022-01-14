package com.github.bourbon.base.lang;

import com.github.bourbon.base.lang.lock.Lockable;
import com.github.bourbon.base.lang.mutable.MutableObject;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 23:23
 */
public final class Holder<T> extends MutableObject<T> implements Lockable {
    private static final long serialVersionUID = -3744805478026364060L;
    private final Lock lock = new ReentrantLock();

    public static <T> Holder<T> of(T v) throws NullPointerException {
        return new Holder<>(ObjectUtils.requireNonNull(v, "Holder can not hold a null value!"));
    }

    public static <T> Holder<T> nullOf() {
        return new Holder<>(null);
    }

    private Holder(T value) {
        super(value);
    }

    @Override
    public Lock getLock() {
        return lock;
    }
}