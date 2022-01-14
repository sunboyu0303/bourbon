package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.SystemUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 14:16
 */
public class JavaSpecInfo {
    private final String name = SystemUtils.get("java.specification.name");
    private final String version = SystemUtils.get("java.specification.version");
    private final String vendor = SystemUtils.get("java.specification.vendor");

    JavaSpecInfo() {
    }

    public final String getName() {
        return name;
    }

    public final String getVersion() {
        return version;
    }

    public final String getVendor() {
        return vendor;
    }
}