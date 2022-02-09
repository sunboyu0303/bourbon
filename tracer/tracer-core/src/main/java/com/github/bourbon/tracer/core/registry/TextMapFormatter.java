package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CharSequenceUtils;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 16:47
 */
public class TextMapFormatter extends AbstractTextFormatter {

    @Override
    public Format<TextMap> getFormatType() {
        return Format.Builtin.TEXT_MAP;
    }

    @Override
    protected String encodedValue(String value) {
        if (CharSequenceUtils.isBlank(value)) {
            return StringConstants.EMPTY;
        }
        return value;
    }

    @Override
    protected String decodedValue(String value) {
        if (CharSequenceUtils.isBlank(value)) {
            return StringConstants.EMPTY;
        }
        return value;
    }
}