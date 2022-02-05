package com.github.bourbon.ump.support;

import com.alibaba.fastjson.JSON;
import com.github.bourbon.base.extension.support.AbstractLifecycle;
import com.github.bourbon.base.utils.LocalHostUtils;
import com.github.bourbon.base.utils.PidUtils;
import com.github.bourbon.base.utils.SystemUtils;
import com.github.bourbon.ump.RuntimeEnvironmentService;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 17:02
 */
public class DefaultRuntimeEnvironmentService extends AbstractLifecycle implements RuntimeEnvironmentService {

    private int cpu;
    private int pid;
    private String ip;
    private String hostName;

    @Override
    protected void doInitialize() {
        cpu = SystemUtils.operatingSystemMXBean().getAvailableProcessors();
        pid = PidUtils.pid();
        ip = LocalHostUtils.ip();
        hostName = LocalHostUtils.hostName();
    }

    @Override
    public int getCpuProcessors() {
        return cpu;
    }

    @Override
    public int getPid() {
        return pid;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}