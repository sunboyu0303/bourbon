package com.github.bourbon.pfinder.profiler.sdk;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
public class PfinderContextPlugins {

    public static class UnwrapPlugin {

        static final class InstanceHolder {
            static UnwrapPlugin INSTANCE = new UnwrapPlugin();
        }

        public static void register(UnwrapPlugin unwrapPlugin) {
            InstanceHolder.INSTANCE = Objects.requireNonNull(unwrapPlugin, "UnwrapPlugin can't be null.");
        }

        public Runnable unwrapRunnable(Runnable runnable) {
            return runnable;
        }

        public <T> Callable<T> unwrapCallable(Callable<T> callable) {
            return callable;
        }
    }

    public static class ForceSamplePlugin {

        static final class InstanceHolder {
            static ForceSamplePlugin INSTANCE = new ForceSamplePlugin();
        }

        public static void register(ForceSamplePlugin unSamplePlugin) {
            InstanceHolder.INSTANCE = Objects.requireNonNull(unSamplePlugin, "UnSamplePlugin can't be null.");
        }

        public boolean forceSample() {
            return false;
        }
    }
}