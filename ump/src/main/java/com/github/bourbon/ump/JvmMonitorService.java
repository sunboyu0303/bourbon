package com.github.bourbon.ump;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 23:18
 */
@SPI(value = "simple", scope = ExtensionScope.FRAMEWORK)
public interface JvmMonitorService extends JvmInfoPicker {

    void initJvm(String key);
}