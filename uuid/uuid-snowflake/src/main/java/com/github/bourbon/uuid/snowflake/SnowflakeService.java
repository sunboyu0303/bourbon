package com.github.bourbon.uuid.snowflake;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 16:49
 */
@SPI(value = "default", scope = ExtensionScope.MODULE)
public interface SnowflakeService {

    long assignWorkerId();
}