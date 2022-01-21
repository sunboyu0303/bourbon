package com.github.bourbon.ump;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.ump.domain.JvmEnvironmentInfo;
import com.github.bourbon.ump.domain.JvmRuntimeInfo;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 00:23
 */
@SPI(value = "simple", scope = ExtensionScope.FRAMEWORK)
public interface JvmInfoPicker {

    JvmEnvironmentInfo jvmEnvironmentInfo();

    JvmRuntimeInfo jvmRuntimeInfo();
}