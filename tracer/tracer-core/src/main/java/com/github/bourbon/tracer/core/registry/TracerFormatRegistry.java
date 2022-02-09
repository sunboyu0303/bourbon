package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.utils.MapUtils;
import io.opentracing.propagation.Format;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 15:35
 */
public final class TracerFormatRegistry {

    private static final Map<Format<?>, RegistryExtractorInjector<?>> injectorsAndExtractors = MapUtils.newConcurrentHashMap();

    static {
        TextMapFormatter textMapFormatter = new TextMapFormatter();
        HttpHeadersFormatter httpHeadersFormatter = new HttpHeadersFormatter();
        BinaryFormater binaryFormater = new BinaryFormater();
        TextMapB3Formatter textMapB3Formatter = new TextMapB3Formatter();
        HttpHeadersB3Formatter httpHeadersB3Formatter = new HttpHeadersB3Formatter();

        register(textMapFormatter.getFormatType(), textMapFormatter);
        register(httpHeadersFormatter.getFormatType(), httpHeadersFormatter);
        register(binaryFormater.getFormatType(), binaryFormater);
        register(textMapB3Formatter.getFormatType(), textMapB3Formatter);
        register(httpHeadersB3Formatter.getFormatType(), httpHeadersB3Formatter);
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryExtractorInjector<T> getRegistry(Format<T> format) {
        return (RegistryExtractorInjector<T>) injectorsAndExtractors.get(format);
    }

    public static <T> void register(Format<T> format, RegistryExtractorInjector<T> extractor) {
        injectorsAndExtractors.put(format, extractor);
    }

    private TracerFormatRegistry() {
    }
}