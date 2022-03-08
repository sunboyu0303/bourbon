package com.github.bourbon.pfinder.profiler.load;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.pfinder.profiler.utils.ParamUtils;
import com.github.bourbon.pfinder.profiler.utils.SmartKey;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 14:27
 */
public class Addon {

    private static final String TAG_CHARSET = "UTF-8";
    private String name;
    private String className;
    private final Map<String, String> tags = MapUtils.newHashMap();

    private Addon() {
    }

    public static Addon parse(String addonStr) {
        Addon addon = new Addon();
        String[] parts = addonStr.split("\\?", 2);
        if (parts.length > 1) {
            addon.tags.putAll(ParamUtils.parseQueryString(parts[1], TAG_CHARSET));
        }
        String[] refParts = parts[0].split(StringConstants.EQUAL, 2);
        addon.name = refParts[0];
        addon.className = refParts[refParts.length - 1];
        return addon;
    }

    public String getName() {
        return this.name;
    }

    public String getClassName() {
        return this.className;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    public String getRawTag(String tagName, String defaultValue) {
        return ObjectUtils.defaultIfNull(tags.get(tagName), defaultValue);
    }

    public <T> T getTag(SmartKey<T> key, T defaultValue) {
        return ObjectUtils.defaultIfNullElseFunction(tags.get(key.key()), key::convert, defaultValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Addon[");
        sb.append(" name=").append(this.name);
        sb.append(" className=").append(this.className);
        sb.append(" tags=[ ");
        for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
            sb.append(tagEntry.getKey()).append("=").append(tagEntry.getValue()).append(" ");
        }
        sb.append("]");
        sb.append(" ]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Addon && name.equals(((Addon) obj).name);
    }
}