package com.github.bourbon.tracer.core.configuration;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.PropertiesUtils;
import com.github.bourbon.tracer.core.appender.info.StaticInfoLog;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 14:10
 */
public final class SofaTracerConfiguration {

    public static final String DISABLE_MIDDLEWARE_DIGEST_LOG_KEY = "disable_middleware_digest_log";

    public static final String DISABLE_DIGEST_LOG_KEY = "disable_digest_log";

    public static final String TRACER_GLOBAL_ROLLING_KEY = "tracer_global_rolling_policy";

    public static final String TRACER_GLOBAL_LOG_RESERVE_DAY = "tracer_global_log_reserve_day";

    public static final int DEFAULT_LOG_RESERVE_DAY = 7;

    public static final int PEN_ATTRS_LENGTH_TRESHOLD = 1024;

    public static final String TRACER_PENETRATE_ATTRIBUTE_MAX_LENGTH = "tracer_penetrate_attribute_max_length";

    public static final String TRACER_SYSTEM_PENETRATE_ATTRIBUTE_MAX_LENGTH = "tracer_system_penetrate_attribute_max_length";

    public static final String STAT_LOG_INTERVAL = "stat_log_interval";

    public static final String TRACER_ASYNC_APPENDER_ALLOW_DISCARD = "tracer_async_appender_allow_discard";

    public static final String TRACER_ASYNC_APPENDER_IS_OUT_DISCARD_NUMBER = "tracer_async_appender_is_out_discard_number";

    public static final String TRACER_ASYNC_APPENDER_IS_OUT_DISCARD_ID = "tracer_async_appender_is_out_discard_id";

    public static final String TRACER_ASYNC_APPENDER_DISCARD_OUT_THRESHOLD = "tracer_async_appender_discard_out_threshold";

    public static final String TRACER_APPNAME_KEY = "spring.application.name";

    public static final String TRACER_JDBC_URL_KEY = "spring.datasource.url";

    private static final Map<String, Object> properties = MapUtils.newConcurrentHashMap();

    private static final Properties fileProperties = PropertiesUtils.getProperties(SofaTracerConfiguration.class.getClassLoader(), "sofa.tracer.properties");

    private static SofaTracerExternalConfiguration sofaTracerExternalConfiguration = null;

    public static final String SAMPLER_STRATEGY_NAME_KEY = "tracer_sampler_strategy_name_key";

    public static final String SAMPLER_STRATEGY_CUSTOM_RULE_CLASS_NAME = "tracer_sampler_strategy_custom_rule_class_name";

    public static final String SAMPLER_STRATEGY_PERCENTAGE_KEY = "tracer_sampler_strategy_percentage_key";

    public static final String JSON_FORMAT_OUTPUT = "global_json_format_output";

    static {
        StaticInfoLog.logStaticInfo();
    }

    public static void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public static void setProperty(String key, Integer value) {
        properties.put(key, value);
    }

    public static void setProperty(String key, Map<String, String> value) {
        properties.put(key, value);
    }

    public static String getProperty(String key) {
        return getProperty(key, StringConstants.EMPTY);
    }

    public static Integer getInteger(String key) {
        if (properties.containsKey(key)) {
            return (Integer) properties.get(key);
        }
        if (System.getProperties().containsKey(key)) {
            return Integer.valueOf(System.getProperty(key));
        }
        if (fileProperties.containsKey(key)) {
            return Integer.valueOf(fileProperties.getProperty(key));
        }
        return null;
    }

    public static Integer getIntegerDefaultIfNull(String key, Integer defaultValue) {
        return ObjectUtils.defaultIfNull(getInteger(key), defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getMapEmptyIfNull(String key) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(properties.get(key), Map.class, Map.class::cast, () -> {
            SelfLog.error(String.format(LogCode2Description.convert(SPACE_ID, "01-00010"), key));
            return Collections.emptyMap();
        });
    }

    public static String getProperty(String key, String defaultValue) {
        if (properties.containsKey(key)) {
            return properties.get(key).toString();
        }
        if (System.getProperties().containsKey(key)) {
            return System.getProperty(key);
        }
        if (fileProperties.containsKey(key)) {
            return fileProperties.get(key).toString();
        }
        if (sofaTracerExternalConfiguration != null && sofaTracerExternalConfiguration.contains(key)) {
            return sofaTracerExternalConfiguration.getValue(key);
        }
        return defaultValue;
    }

    private SofaTracerConfiguration() {
    }
}