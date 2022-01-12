package com.github.bourbon.springframework.core;

import com.github.bourbon.base.system.Version;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/18 15:58
 */
public final class SpringVersion {

    public static final String VERSION = Version.getVersion(SpringVersion.class);

    private SpringVersion() {
    }
}