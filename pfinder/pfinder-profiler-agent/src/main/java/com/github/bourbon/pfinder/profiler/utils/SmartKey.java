package com.github.bourbon.pfinder.profiler.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.pfinder.profiler.tracer.plugin.interceptor.Describable;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 23:08
 */
public class SmartKey<T> implements Converter<String, T>, Describable {

    private static final Converter<String, Boolean> BOOLEAN_CONVERTER = t -> t != null && !t.isEmpty() ? Boolean.parseBoolean(t) : null;
    private static final Converter<String, Integer> INTEGER_CONVERT = t -> t != null && !t.isEmpty() ? Integer.parseInt(t) : null;
    private static final Converter<String, Long> LONG_CONVERT = t -> t != null && !t.isEmpty() ? Long.parseLong(t) : null;
    private static final Converter<String, String> STRING_CONVERT = t -> t;
    private static final Converter<String, URI> URI_CONVERT = t -> CharSequenceUtils.isEmpty(t) ? null : URI.create(t);

    private final String key;
    private final Converter<String, T> converter;
    private final String description;
    private final RemoteConfigLevel remoteConfigLevel;

    public static <T> SmartKey<T> of(String key, Converter<String, T> converter, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, converter, description, remoteConfigLevel);
    }

    public static SmartKey<String> ofString(String key, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, STRING_CONVERT, description, remoteConfigLevel);
    }

    public static SmartKey<Boolean> ofBoolean(String key, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, BOOLEAN_CONVERTER, description, remoteConfigLevel);
    }

    public static SmartKey<Integer> ofInteger(String key, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, INTEGER_CONVERT, description, remoteConfigLevel);
    }

    public static SmartKey<Integer> ofMicroTimeDuration(String key, TimeUnit originTimeUnit, TimeUnit expectTimeUnit, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, new MicroTimeUnitConverter(originTimeUnit, expectTimeUnit), description, remoteConfigLevel);
    }

    public static SmartKey<Long> ofTimeDuration(String key, TimeUnit originTimeUnit, TimeUnit expectTimeUnit, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, new TimeUnitConverter(originTimeUnit, expectTimeUnit), description, remoteConfigLevel);
    }

    public static SmartKey<URI> ofUri(String key, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, URI_CONVERT, description, remoteConfigLevel);
    }

    public static SmartKey<Long> ofLong(String key, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, LONG_CONVERT, description, remoteConfigLevel);
    }

    public static SmartKey<String[]> ofStringArray(String key, String splitPattern, String description, RemoteConfigLevel remoteConfigLevel) {
        return new SmartKey<>(key, new StringArrayConverter(splitPattern), description, remoteConfigLevel);
    }

    private SmartKey(String key, Converter<String, T> converter, String description, RemoteConfigLevel remoteConfigLevel) {
        this.key = key;
        this.converter = converter;
        this.description = description;
        this.remoteConfigLevel = remoteConfigLevel;
    }

    public String key() {
        return this.key;
    }

    @Override
    public String description() {
        return this.description;
    }

    public RemoteConfigLevel editLevel() {
        return this.remoteConfigLevel;
    }

    public String getConvertReturnType() {
        try {
            return converter.getClass().getMethod("convert", String.class).getReturnType().getName();
        } catch (NoSuchMethodException e) {
            return StringConstants.EMPTY;
        }
    }

    @Override
    public T convert(String target) {
        return this.converter.convert(target);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + this.key + "]";
    }

    public enum RemoteConfigLevel {
        NOBODY, ADMIN, USER
    }

    private static class TimeUnitConverter implements Converter<String, Long> {
        private final TimeUnit from;
        private final TimeUnit to;

        private TimeUnitConverter(TimeUnit from, TimeUnit to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Long convert(String target) {
            if (target != null && !target.isEmpty()) {
                return this.to.convert(Long.parseLong(target), this.from);
            }
            return null;
        }
    }

    private static class MicroTimeUnitConverter implements Converter<String, Integer> {
        private final TimeUnit from;
        private final TimeUnit to;

        private MicroTimeUnitConverter(TimeUnit from, TimeUnit to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Integer convert(String target) {
            if (target != null && !target.isEmpty()) {
                return (int) this.to.convert(Long.parseLong(target), this.from);
            }
            return null;
        }
    }

    private static class StringArrayConverter implements Converter<String, String[]> {
        private final String splitPattern;

        private StringArrayConverter(String splitPattern) {
            this.splitPattern = splitPattern;
        }

        @Override
        public String[] convert(String target) {
            return CharSequenceUtils.isEmpty(target) ? null : target.split(this.splitPattern);
        }
    }
}