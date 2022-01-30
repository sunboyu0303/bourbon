package com.github.bourbon.base.logger;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 20:02
 */
public final class LoggerFactory {

    private static final ConcurrentMap<String, FailSafeLogger> LOGGERS = new ConcurrentHashMap<>();

    private static final LoggerAdapter LOGGER_ADAPTER;

    static {
        LoggerAdapter tmp = null;
        Set<LoggerAdapter> instances = ScopeModelUtils.getExtensionLoader(LoggerAdapter.class).getSupportedExtensionInstances();
        if (CollectionUtils.isNotEmpty(instances)) {
            for (LoggerAdapter adapter : instances) {
                if (adapter.initialize()) {
                    tmp = adapter;
                    break;
                }
            }
        }
        LOGGER_ADAPTER = ObjectUtils.defaultSupplierIfNull(tmp, SystemLoggerAdapter::new);
    }

    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LOGGERS.computeIfAbsent(clazz.getName(), o -> new FailSafeLogger(LOGGER_ADAPTER.getLogger(clazz)));
    }

    public static Logger getLogger(String name) {
        return LOGGERS.computeIfAbsent(name, o -> new FailSafeLogger(LOGGER_ADAPTER.getLogger(name)));
    }

    public static Logger getLogger(String name, LogParamInfo info) {
        return LOGGERS.computeIfAbsent(name, o -> new FailSafeLogger(LOGGER_ADAPTER.getLogger(name, info)));
    }
}