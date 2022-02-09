package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import io.opentracing.propagation.TextMap;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 16:44
 */
public abstract class AbstractTextFormatter implements RegistryExtractorInjector<TextMap> {

    @Override
    public SofaTracerSpanContext extract(TextMap carrier) {
        SofaTracerSpanContext sofaTracerSpanContext = null;
        if (carrier != null) {
            for (Map.Entry<String, String> entry : carrier) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CharSequenceUtils.isBlank(key)) {
                    continue;
                }
                if (FORMATER_KEY_HEAD.equalsIgnoreCase(key) && CharSequenceUtils.isNotBlank(value)) {
                    sofaTracerSpanContext = SofaTracerSpanContext.deserializeFromString(decodedValue(value));
                }
            }
        }
        return sofaTracerSpanContext;
    }

    @Override
    public void inject(SofaTracerSpanContext spanContext, TextMap carrier) {
        if (carrier == null || spanContext == null) {
            return;
        }
        carrier.put(FORMATER_KEY_HEAD, encodedValue(spanContext.serializeSpanContext()));
    }

    protected abstract String encodedValue(String value);

    protected abstract String decodedValue(String value);
}