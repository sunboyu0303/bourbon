package com.github.bourbon.tracer.core.reporter.stat.model;

import com.github.bourbon.base.utils.MapUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 10:39
 */
public class StatMapKey extends StatKey {

    private final Map<String, String> keyMap = MapUtils.newHashMap();

    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    public void addKey(String key, String value) {
        if (key != null && value != null) {
            keyMap.put(key, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatMapKey)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        StatMapKey that = (StatMapKey) o;
        return getKeyMap().equals(that.getKeyMap());
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + getKeyMap().hashCode();
    }
}