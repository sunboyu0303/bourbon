package com.github.bourbon.base.config.source;

import com.github.bourbon.base.config.ConfigKey;
import com.github.bourbon.base.config.ConfigSource;
import com.github.bourbon.base.config.converter.DefaultConverter;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:36
 */
public abstract class AbstractConfigSource implements ConfigSource {

    @Override
    public <T> T getConfig(ConfigKey<T> key) {
        return changeValueType(getStringConfig(key), key.getDefaultValue());
    }

    @Override
    public String getStringConfig(ConfigKey key) {
        String value = doGetConfig(key.getKey());
        if (CharSequenceUtils.isNotBlank(value)) {
            return value;
        }
        for (String alias : key.getAlias()) {
            value = doGetConfig(alias);
            if (CharSequenceUtils.isNotBlank(value)) {
                return value;
            }
        }
        return value;
    }

    @Override
    public String getEffectiveKey(ConfigKey configKey) {
        String key = configKey.getKey();
        if (hasKey(key)) {
            return key;
        }
        for (String alias : configKey.getAlias()) {
            if (hasKey(alias)) {
                return alias;
            }
        }
        return StringConstants.EMPTY;
    }

    @SuppressWarnings("unchecked")
    private <T> T changeValueType(String value, T defaultValue) {
        return ObjectUtils.defaultIfNullElseFunction(value, v -> (T) DefaultConverter.getInstance().convert(v, defaultValue));
    }

    public abstract String doGetConfig(String key);

    public abstract boolean hasKey(String key);
}