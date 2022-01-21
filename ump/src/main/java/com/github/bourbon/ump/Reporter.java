package com.github.bourbon.ump;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.ump.domain.LogType;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 20:01
 */
@SPI(value = "disk", scope = ExtensionScope.FRAMEWORK)
public interface Reporter {

    boolean report(LogType logType, String message);
}