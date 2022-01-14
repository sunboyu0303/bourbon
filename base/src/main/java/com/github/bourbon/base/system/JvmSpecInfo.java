package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.SystemUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 11:39
 */
public final class JvmSpecInfo {

    private final String name = SystemUtils.get("java.vm.specification.name");
    private final String version = SystemUtils.get("java.vm.specification.version");
    private final String vendor = SystemUtils.get("java.vm.specification.vendor");

    JvmSpecInfo() {
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