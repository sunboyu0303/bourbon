package com.github.bourbon.ump;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.base.logger.Logger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 16:56
 */
@SPI(value = "simple", scope = ExtensionScope.FRAMEWORK)
public interface UmpLoggerFactory {

    Logger tpLogger();

    Logger jvmLogger();

    Logger aliveLogger();
}