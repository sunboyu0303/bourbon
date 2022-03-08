package com.github.bourbon.pfinder.profiler.sdk;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
public class PfinderContextRegister {

    private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

    public static boolean isRegistered() {
        return REGISTERED.get();
    }

    public static boolean register(PfinderContext pfinderContext) {
        if (REGISTERED.compareAndSet(false, true)) {
            forceRegister(pfinderContext);
            return true;
        }
        return false;
    }

    public static void forceRegister(PfinderContext pfinderContext) {
        PfinderContext.InstanceHolder.INSTANCE = Objects.requireNonNull(pfinderContext, "PfinderContext is required.");
    }
}