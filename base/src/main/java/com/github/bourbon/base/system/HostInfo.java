package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.LocalHostUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 10:32
 */
public final class HostInfo {
    private final String hostName = LocalHostUtils.hostName();
    private final String ip = LocalHostUtils.ip();

    HostInfo() {
    }

    public String getHostName() {
        return hostName;
    }

    public String getIp() {
        return ip;
    }
}