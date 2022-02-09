package com.github.bourbon.tracer.core.utils;

import com.github.bourbon.base.appender.config.LogReserveConfig;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SystemUtils;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.TimeZone;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 14:52
 */
public class TracerUtils {

    private static final String CURRENT_ZONE = SystemUtils.get("com.github.bourbon.ldc.zone");

    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();

    private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    public static String getCurrentZone() {
        return CURRENT_ZONE;
    }

    public static TimeZone getDefaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    public static String getDefaultTimeZoneId() {
        return getDefaultTimeZone().getID();
    }

    public static TimeZone getGMTDefaultTimeZone() {
        return GMT_TIME_ZONE;
    }

    public static String getTraceId() {
        return ObjectUtils.defaultIfNullElseFunction(SofaTraceContextHolder.getSofaTraceContext().getCurrentSpan(), s -> ObjectUtils.defaultIfNullElseFunction(s.getSofaTracerSpanContext(), SofaTracerSpanContext::getTraceId, StringConstants.EMPTY), StringConstants.EMPTY);
    }

    public static boolean isLoadTest(SofaTracerSpan sofaTracerSpan) {
        if (sofaTracerSpan == null || sofaTracerSpan.getSofaTracerSpanContext() == null) {
            return false;
        }
        return SofaTracerConstants.LOAD_TEST_VALUE.equals(sofaTracerSpan.getSofaTracerSpanContext().getBizBaggage().get(SofaTracerConstants.LOAD_TEST_TAG));
    }

    public static LogReserveConfig parseLogReserveConfig(String logReserveConfig) {
        if (CharSequenceUtils.isBlank(logReserveConfig)) {
            return new LogReserveConfig(SofaTracerConfiguration.DEFAULT_LOG_RESERVE_DAY, 0);
        }
        int day;
        int hour = 0;
        int dayIndex = logReserveConfig.indexOf(StringConstants.D);
        if (dayIndex >= 0) {
            day = Integer.parseInt(logReserveConfig.substring(0, dayIndex));
        } else {
            day = Integer.parseInt(logReserveConfig);
        }
        int hourIndex = logReserveConfig.indexOf(StringConstants.H);
        if (hourIndex >= 0) {
            hour = Integer.parseInt(logReserveConfig.substring(dayIndex + 1, hourIndex));
        }
        return new LogReserveConfig(day, hour);
    }

    private static final int tracePenetrateAttributeMaxLength = BooleanUtils.defaultIfPredicate(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_PENETRATE_ATTRIBUTE_MAX_LENGTH), CharSequenceUtils::isNotBlank, l -> {
        try {
            return Integer.parseInt(l);
        } catch (NumberFormatException e) {
            return SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD;
        }
    }, SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD);

    private static final int tracerSystemPenetrateAttributeMaxLength = BooleanUtils.defaultIfPredicate(SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_SYSTEM_PENETRATE_ATTRIBUTE_MAX_LENGTH), CharSequenceUtils::isNotBlank, l -> {
        try {
            return Integer.parseInt(l);
        } catch (NumberFormatException e) {
            return SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD;
        }
    }, SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD);

    public static int getBaggageMaxLength() {
        return tracePenetrateAttributeMaxLength;
    }

    public static int getSysBaggageMaxLength() {
        return tracerSystemPenetrateAttributeMaxLength;
    }
}