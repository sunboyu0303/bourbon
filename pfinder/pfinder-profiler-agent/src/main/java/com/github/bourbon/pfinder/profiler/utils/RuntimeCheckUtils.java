package com.github.bourbon.pfinder.profiler.utils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 00:23
 */
public class RuntimeCheckUtils {

    public static boolean supportEnhance() {
        String currentJavaVersionStr = System.getProperty("java.version");
        Version currentJavaVersion = Version.parse(currentJavaVersionStr);
        if (currentJavaVersion.lessThan("1.8")) {
            return true;
        }
        return currentJavaVersion.greaterOrEqualsThan("1.8.0_60");
    }

    private RuntimeCheckUtils() {
        throw new UnsupportedOperationException();
    }
}