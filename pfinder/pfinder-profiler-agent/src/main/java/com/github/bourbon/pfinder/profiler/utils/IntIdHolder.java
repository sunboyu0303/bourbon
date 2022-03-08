package com.github.bourbon.pfinder.profiler.utils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 15:14
 */
public interface IntIdHolder extends IdHolder {
    
    IntIdHolder NOOP = new IntIdHolder() {
        @Override
        public int getId() {
            return -1;
        }

        @Override
        public String getName() {
            return "NOOP";
        }

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public String toString() {
            return "NOOP";
        }
    };

    int getId();
}