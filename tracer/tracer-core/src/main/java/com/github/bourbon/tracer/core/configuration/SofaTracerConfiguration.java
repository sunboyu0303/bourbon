package com.github.bourbon.tracer.core.configuration;

import com.github.bourbon.base.code.LogCode2Description;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.tracer.core.appender.file.TimedRollingFileAppender;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.appender.info.StaticInfoLog;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;

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

    private static final String SAMPLER_STRATEGY_NAME_KEY = "tracer_sampler_strategy_name_key";

    public static final String SAMPLER_STRATEGY_CUSTOM_RULE_CLASS_NAME = "tracer_sampler_strategy_custom_rule_class_name";

    public static final String SAMPLER_STRATEGY_PERCENTAGE_KEY = "tracer_sampler_strategy_percentage_key";

    private static final String JSON_FORMAT_OUTPUT = "global_json_format_output";

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
        Object o = properties.get(key);
        if (ObjectUtils.nonNull(o)) {
            return (Integer) o;
        }
        String value = System.getProperty(key);
        if (CharSequenceUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        }
        value = fileProperties.getProperty(key);
        if (CharSequenceUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    public static Integer getIntegerDefaultIfNull(String key, Integer defaultValue) {
        return ObjectUtils.defaultIfNull(getInteger(key), defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getMapEmptyIfNull(String key) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(properties.get(key), Map.class, Map.class::cast, () -> {
            SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00010"), key));
            return Collections.emptyMap();
        });
    }

    public static String getProperty(String key, String defaultValue) {
        Object o = properties.get(key);
        if (ObjectUtils.nonNull(o)) {
            return o.toString();
        }
        String value = System.getProperty(key);
        if (ObjectUtils.nonNull(value)) {
            return value;
        }
        o = fileProperties.get(key);
        if (ObjectUtils.nonNull(o)) {
            return o.toString();
        }
        if (sofaTracerExternalConfiguration != null) {
            value = sofaTracerExternalConfiguration.getValue(key);
            if (ObjectUtils.nonNull(value)) {
                return value;
            }
        }
        return defaultValue;
    }

    public static String getRollingPolicy(String rollingKey) {
        if (CharSequenceUtils.isBlank(rollingKey)) {
            return StringConstants.EMPTY;
        }
        String rollingPolicy = getProperty(rollingKey);
        if (CharSequenceUtils.isBlank(rollingPolicy)) {
            rollingPolicy = getProperty(TRACER_GLOBAL_ROLLING_KEY);
        }
        return BooleanUtils.defaultIfPredicate(rollingPolicy, CharSequenceUtils::isNotBlank, t -> t, TimedRollingFileAppender.DAILY_ROLLING_PATTERN);
    }

    public static String getLogReserveConfig(String logReserveKey) {
        if (CharSequenceUtils.isBlank(logReserveKey)) {
            return StringConstants.EMPTY;
        }
        String reserveDay = getProperty(logReserveKey);
        if (CharSequenceUtils.isNotBlank(reserveDay)) {
            return reserveDay;
        }
        return getProperty(TRACER_GLOBAL_LOG_RESERVE_DAY, String.valueOf(DEFAULT_LOG_RESERVE_DAY));
    }

    public static void setSofaTracerExternalConfiguration(SofaTracerExternalConfiguration sofaTracerExternalConfiguration) {
        SofaTracerConfiguration.sofaTracerExternalConfiguration = sofaTracerExternalConfiguration;
    }

    public static String getSofaTracerSamplerStrategy() {
        return BooleanUtils.defaultIfPredicate(getProperty(SAMPLER_STRATEGY_NAME_KEY), CharSequenceUtils::isNotBlank, t -> t);
    }

    public static boolean isJsonOutput() {
        return !"false".equalsIgnoreCase(getProperty(JSON_FORMAT_OUTPUT));
    }

    private SofaTracerConfiguration() {
    }
}