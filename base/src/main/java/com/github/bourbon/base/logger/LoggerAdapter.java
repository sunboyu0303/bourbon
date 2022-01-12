package com.github.bourbon.base.logger;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.base.lang.Prioritized;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 20:00
 */
@SPI(scope = ExtensionScope.FRAMEWORK)
public interface LoggerAdapter extends Prioritized {

    Logger getLogger(Class<?> clazz);

    Logger getLogger(String name);

    Logger getLogger(String name, LogParamInfo info);

    Level getLevel();

    void setLevel(Level level);

    boolean initialize();
}