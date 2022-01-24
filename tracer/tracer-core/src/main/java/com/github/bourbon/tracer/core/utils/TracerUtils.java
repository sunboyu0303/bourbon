package com.github.bourbon.tracer.core.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 14:52
 */
public class TracerUtils {

    public static String getTraceId() {
        return ObjectUtils.defaultIfNull(SofaTraceContextHolder.getSofaTraceContext().getCurrentSpan(), s -> ObjectUtils.defaultIfNull(
                s.getSofaTracerSpanContext(), c -> BooleanUtils.defaultIfPredicate(c.getTraceId(), t -> !CharSequenceUtils.isBlank(t), t -> t, StringConstants.EMPTY), StringConstants.EMPTY
        ), StringConstants.EMPTY);
    }

    private static int tracePenetrateAttributeMaxLength;

    private static int tracerSystemPenetrateAttributeMaxLength = -1;

    static {
        tracePenetrateAttributeMaxLength = BooleanUtils.defaultIfPredicate(
                SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_PENETRATE_ATTRIBUTE_MAX_LENGTH),
                length -> !CharSequenceUtils.isBlank(length),
                length -> {
                    try {
                        return Integer.parseInt(length);
                    } catch (NumberFormatException e) {
                        return SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD;
                    }
                },
                SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD
        );

        tracerSystemPenetrateAttributeMaxLength = BooleanUtils.defaultIfPredicate(
                SofaTracerConfiguration.getProperty(SofaTracerConfiguration.TRACER_SYSTEM_PENETRATE_ATTRIBUTE_MAX_LENGTH),
                length -> !CharSequenceUtils.isBlank(length),
                length -> {
                    try {
                        return Integer.parseInt(length);
                    } catch (NumberFormatException e) {
                        return SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD;
                    }
                },
                SofaTracerConfiguration.PEN_ATTRS_LENGTH_TRESHOLD
        );
    }

    public static int getBaggageMaxLength() {
        return tracePenetrateAttributeMaxLength;
    }

    public static int getSysBaggageMaxLength() {
        return tracerSystemPenetrateAttributeMaxLength;
    }
}