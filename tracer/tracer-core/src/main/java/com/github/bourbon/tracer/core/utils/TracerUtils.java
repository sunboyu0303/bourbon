package com.github.bourbon.tracer.core.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SystemUtils;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;

import java.util.TimeZone;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 14:52
 */
public class TracerUtils {

    private static final String CURRENT_ZONE = SystemUtils.get("com.github.bourbon.ldc.zone");

    private static final String DEFAULT_TIME_ZONE = TimeZone.getDefault().getID();

    public static String getCurrentZone() {
        return CURRENT_ZONE;
    }

    public static String getDefaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    public static String getTraceId() {
        return ObjectUtils.defaultIfNull(SofaTraceContextHolder.getSofaTraceContext().getCurrentSpan(), s -> ObjectUtils.defaultIfNull(s.getSofaTracerSpanContext(), SofaTracerSpanContext::getTraceId, StringConstants.EMPTY), StringConstants.EMPTY);
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