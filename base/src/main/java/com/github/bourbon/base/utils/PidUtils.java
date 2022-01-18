package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/2 14:27
 */
public interface PidUtils {

    static int getPid() {
        return Integer.parseInt(SystemUtils.runtimeMXBean().getName().split(StringConstants.AT)[0]);
    }
}