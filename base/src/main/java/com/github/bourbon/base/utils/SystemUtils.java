package com.github.bourbon.base.utils;

import com.github.bourbon.base.convert.Convert;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.*;
import java.util.List;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 09:00
 */
public final class SystemUtils {

    public static Properties props() {
        return System.getProperties();
    }

    public static String get(String name, String defaultValue) {
        return CharSequenceUtils.defaultIfNull(get(name), defaultValue);
    }

    public static String get(String name) {
        String value = null;
        try {
            value = System.getProperty(name);
        } catch (Exception e) {
            // ignore
        }
        if (ObjectUtils.isNull(value)) {
            try {
                value = System.getenv(name);
            } catch (Exception e) {
                // ignore
            }
        }
        return value;
    }

    public static boolean get(String key, boolean defaultValue) {
        return Convert.toBoolean(get(key), defaultValue);
    }

    public static char get(String key, char defaultValue) {
        return Convert.toCharacter(get(key), defaultValue);
    }

    public static float get(String key, float defaultValue) {
        return Convert.toFloat(get(key), defaultValue);
    }

    public static double get(String key, double defaultValue) {
        return Convert.toDouble(get(key), defaultValue);
    }

    public static byte get(String key, byte defaultValue) {
        return Convert.toByte(get(key), defaultValue);
    }

    public static short get(String key, short defaultValue) {
        return Convert.toShort(get(key), defaultValue);
    }

    public static int get(String key, int defaultValue) {
        return Convert.toInteger(get(key), defaultValue);
    }

    public static long get(String key, long defaultValue) {
        return Convert.toLong(get(key), defaultValue);
    }

    public static RuntimeMXBean runtimeMXBean() {
        return ManagementFactory.getRuntimeMXBean();
    }

    public static ThreadMXBean threadMXBean() {
        return ManagementFactory.getThreadMXBean();
    }

    public static OperatingSystemMXBean operatingSystemMXBean() {
        return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public static ClassLoadingMXBean classLoadingMXBean() {
        return ManagementFactory.getClassLoadingMXBean();
    }

    public static MemoryMXBean memoryMXBean() {
        return ManagementFactory.getMemoryMXBean();
    }

    public static MemoryUsage heapMemoryUsage() {
        return memoryMXBean().getHeapMemoryUsage();
    }

    public static MemoryUsage nonHeapMemoryUsage() {
        return memoryMXBean().getNonHeapMemoryUsage();
    }

    public static List<GarbageCollectorMXBean> garbageCollectorMXBeans() {
        return ManagementFactory.getGarbageCollectorMXBeans();
    }

    public static CompilationMXBean compilationMXBean() {
        return ManagementFactory.getCompilationMXBean();
    }

    public static List<MemoryPoolMXBean> memoryPoolMXBeans() {
        return ManagementFactory.getMemoryPoolMXBeans();
    }

    public static List<MemoryManagerMXBean> memoryManagerMXBeans() {
        return ManagementFactory.getMemoryManagerMXBeans();
    }

    private SystemUtils() {
    }
}