package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.SystemUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 14:12
 */
public class JvmInfo {
    private final String name = SystemUtils.get("java.vm.name");
    private final String version = SystemUtils.get("java.vm.version");
    private final String vendor = SystemUtils.get("java.vm.vendor");
    private final String info = SystemUtils.get("java.vm.info");

    JvmInfo() {
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

    public final String getInfo() {
        return info;
    }
}