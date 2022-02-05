package com.github.bourbon.base.space;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/27 11:09
 */
public class SpaceId {
    private static final Map<String, SpaceId> GLOBAL_SPACE_ID_CACHE = MapUtils.newConcurrentHashMap();

    private final Map<String, String> tags = MapUtils.newHashMap();

    private final String spaceName;

    public static SpaceId withSpaceName(String spaceName) {
        return GLOBAL_SPACE_ID_CACHE.computeIfAbsent(spaceName, SpaceId::new);
    }

    private SpaceId(String spaceName) {
        Assert.notNull(spaceName);
        this.spaceName = spaceName;
    }

    public SpaceId withTag(String key, String value) {
        tags.put(key, value);
        return this;
    }

    public String getSpaceName() {
        return spaceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceId)) {
            return false;
        }
        SpaceId spaceId = (SpaceId) o;
        if (!tags.equals(spaceId.tags)) {
            return false;
        }
        return ObjectUtils.equals(spaceName, spaceId.spaceName);
    }

    @Override
    public int hashCode() {
        return 31 * tags.hashCode() + spaceName.hashCode();
    }
}