package com.github.bourbon.springframework.boot.context.properties.source;

import java.lang.ref.SoftReference;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 22:34
 */
class SoftReferenceConfigurationPropertyCache<T> implements ConfigurationPropertyCaching {

    private static final Duration UNLIMITED = Duration.ZERO;

    private final boolean neverExpire;

    private volatile Duration timeToLive;

    private volatile SoftReference<T> value = new SoftReference<>(null);

    private volatile Instant lastAccessed = now();

    SoftReferenceConfigurationPropertyCache(boolean neverExpire) {
        this.neverExpire = neverExpire;
    }

    @Override
    public void enable() {
        timeToLive = UNLIMITED;
    }

    @Override
    public void disable() {
        timeToLive = null;
    }

    @Override
    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = (timeToLive == null || timeToLive.isZero()) ? null : timeToLive;
    }

    @Override
    public void clear() {
        lastAccessed = null;
    }

    T get(Supplier<T> factory, UnaryOperator<T> refreshAction) {
        T value = getValue();
        if (value == null) {
            value = refreshAction.apply(factory.get());
            setValue(value);
        } else if (hasExpired()) {
            value = refreshAction.apply(value);
            setValue(value);
        }
        if (!neverExpire) {
            lastAccessed = now();
        }
        return value;
    }

    private boolean hasExpired() {
        if (neverExpire) {
            return false;
        }
        Duration timeToLive = this.timeToLive;
        Instant lastAccessed = this.lastAccessed;
        if (timeToLive == null || lastAccessed == null) {
            return true;
        }
        return !UNLIMITED.equals(timeToLive) && now().isAfter(lastAccessed.plus(timeToLive));
    }

    protected Instant now() {
        return Instant.now();
    }

    protected T getValue() {
        return value.get();
    }

    protected void setValue(T value) {
        this.value = new SoftReference<>(value);
    }
}