package com.github.bourbon.tracer.core.reporter.common;

import com.github.bourbon.base.appender.builder.XStringBuilder;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.Timestamp;
import com.github.bourbon.tracer.core.appender.encoder.SpanEncoder;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import com.github.bourbon.tracer.core.span.CommonLogSpan;
import com.github.bourbon.tracer.core.tags.SpanTags;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 10:23
 */
public class CommonSpanEncoder implements SpanEncoder<CommonLogSpan> {

    @Override
    public String encode(CommonLogSpan commonLogSpan) {
        if (commonLogSpan.getSofaTracerSpanContext() == null) {
            return StringConstants.EMPTY;
        }
        SofaTracerSpanContext spanContext = commonLogSpan.getSofaTracerSpanContext();
        XStringBuilder xsb = new XStringBuilder();
        // The time when the report started as the time of printing, there is no completion time
        xsb.append(Timestamp.format(commonLogSpan.getStartTime()))
                //Ensure that the construct common is also carried
                .append(commonLogSpan.getTagsWithStr().get(SpanTags.CURR_APP_TAG.getKey()))
                .append(spanContext.getTraceId()).append(spanContext.getSpanId());
        this.appendStr(xsb, commonLogSpan);
        return xsb.toString();
    }

    private void appendStr(XStringBuilder xsb, CommonLogSpan commonLogSpan) {
        for (int i = 0, slotSize = commonLogSpan.getSlots().size(); i < slotSize; i++) {
            if (i + 1 != slotSize) {
                xsb.append(commonLogSpan.getSlots().get(i));
            } else {
                xsb.appendRaw(commonLogSpan.getSlots().get(i));
            }
        }
        xsb.appendRaw(StringConstants.NEWLINE);
    }
}