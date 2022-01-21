package com.github.bourbon.ump.constant;

import com.github.bourbon.base.system.Version;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 17:00
 */
public final class UMPConstants {

    public static final String PROFILER_VERSION = Version.getVersion(UMPConstants.class);

    public static final int DEFAULT_TP_METRIC_PERIOD = 5_000;

    public static final long DEFAULT_ALIVE_METRIC_PERIOD = 20_000L;

    public static final long DEFAULT_JVM_METRIC_DELAY = 10_000L;

    public static final long DEFAULT_JVM_METRIC_PERIOD = 10_000L;

    public static final long ENVIRONMENT_INFO_REPORT_PERIOD = 14400_000L;

    private UMPConstants() {
    }
}