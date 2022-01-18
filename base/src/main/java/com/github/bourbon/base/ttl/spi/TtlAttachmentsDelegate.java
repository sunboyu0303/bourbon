package com.github.bourbon.base.ttl.spi;

import com.github.bourbon.base.utils.MapUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 23:17
 */
public class TtlAttachmentsDelegate implements TtlAttachments {

    private final Map<String, Object> attachments = MapUtils.newConcurrentHashMap();

    public void setTtlAttachment(String key, Object value) {
        attachments.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTtlAttachment(String key) {
        return (T) attachments.get(key);
    }
}