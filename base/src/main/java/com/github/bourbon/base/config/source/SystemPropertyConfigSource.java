package com.github.bourbon.base.config.source;

import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 17:02
 */
public class SystemPropertyConfigSource extends AbstractConfigSource {

    @Override
    public String doGetConfig(String key) {
        return System.getProperty(key);
    }

    @Override
    public boolean hasKey(String key) {
        return CharSequenceUtils.isNotBlank(doGetConfig(key));
    }

    @Override
    public String getName() {
        return "SystemProperty";
    }

    @Override
    public int getPriority() {
        return 100;
    }
}