package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/2 14:27
 */
public final class PidUtils {

    private static final int pid = Integer.parseInt(SystemUtils.runtimeMXBean().getName().split(StringConstants.AT)[0]);

    public static int pid() {
        return pid;
    }

    private PidUtils() {
    }
}