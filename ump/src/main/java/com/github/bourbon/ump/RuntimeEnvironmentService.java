package com.github.bourbon.ump;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 17:02
 */
@SPI(value = "default", scope = ExtensionScope.FRAMEWORK)
public interface RuntimeEnvironmentService {

    int getCpuProcessors();

    int getPid();

    String getIp();

    String getHostName();
}