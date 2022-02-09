package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 17:21
 */
public class HttpHeadersFormatter extends AbstractTextFormatter {

    @Override
    public Format<TextMap> getFormatType() {
        return Format.Builtin.HTTP_HEADERS;
    }

    @Override
    protected String encodedValue(String value) {
        if (CharSequenceUtils.isBlank(value)) {
            return StringConstants.EMPTY;
        }
        try {
            return URLEncoder.encode(value, SofaTracerConstants.DEFAULT_UTF8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    @Override
    protected String decodedValue(String value) {
        if (CharSequenceUtils.isBlank(value)) {
            return StringConstants.EMPTY;
        }
        try {
            return URLDecoder.decode(value, SofaTracerConstants.DEFAULT_UTF8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}