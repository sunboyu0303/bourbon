package com.github.bourbon.uuid.core;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 17:19
 */
@SPI(scope = ExtensionScope.FRAMEWORK)
public interface IDGenerator {

    long getId() throws Exception;
}