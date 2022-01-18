package com.github.bourbon.base.ttl.spi;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 23:15
 */
public interface TtlAttachments extends TtlEnhanced {

    void setTtlAttachment(String key, Object value);

    <T> T getTtlAttachment(String key);
}