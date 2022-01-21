package com.github.bourbon.ump;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.lang.Holder;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.ump.constant.UMPConstants;
import com.github.bourbon.ump.domain.CallerInfo;
import com.github.bourbon.ump.domain.LogType;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/24 18:17
 */
public final class Profiler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Profiler.class);
    private static final Reporter REPORTER = ScopeModelUtils.getExtensionLoader(Reporter.class).getDefaultExtension();
    private static final TpMonitorService TP_MONITOR_SERVICE = ScopeModelUtils.getExtensionLoader(TpMonitorService.class).getDefaultExtension();
    private static final Map<String, Holder<Long>> FUNC_HB = MapUtils.newConcurrentHashMap();

    private Profiler() {
    }

    public static CallerInfo registerInfo(String key, boolean enableHeart, boolean enableTP) {
        if (key == null || StringConstants.EMPTY.equals(key.trim())) {
            return null;
        }
        try {
            CallerInfo callerInfo = null;
            String k = key.trim();
            if (enableTP) {
                callerInfo = new CallerInfo(k);
            }

            if (enableHeart) {
                Holder<Long> holder = FUNC_HB.computeIfAbsent(k, o -> {
                    REPORTER.report(LogType.ALIVE, "{\"k\":\"" + k + "\"}");
                    return Holder.of(SystemClock.currentTimeMillis());
                });

                if (SystemClock.currentTimeMillis() - holder.get() >= UMPConstants.DEFAULT_ALIVE_METRIC_PERIOD) {
                    if (holder.tryLock()) {
                        try {
                            REPORTER.report(LogType.ALIVE, "{\"k\":\"" + k + "\"}");
                            holder.set(SystemClock.currentTimeMillis());
                        } finally {
                            holder.unlock();
                        }
                    }
                }
            }
            return callerInfo;
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    public static void functionError(CallerInfo info) {
        try {
            if (info != null) {
                info.error();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void registerInfoEnd(CallerInfo info) {
        try {
            if (info != null) {
                if (info.getProcessState() == CallerInfo.STATE_FALSE) {
                    TP_MONITOR_SERVICE.handle(info, -1L);
                } else {
                    TP_MONITOR_SERVICE.handle(info, info.getElapsedTime());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static void registerJVMInfo(String key) {
        if (key != null && !StringConstants.EMPTY.equals(key.trim())) {
            ScopeModelUtils.getExtensionLoader(JvmMonitorService.class).getDefaultExtension().initJvm(key.trim());
        }
    }

    public static void initHeartBeats(String key) {
        if (key != null && !StringConstants.EMPTY.equals(key.trim())) {
            ScopeModelUtils.getExtensionLoader(AliveMonitorService.class).getDefaultExtension().initSystemAlive(key.trim());
        }
    }
}