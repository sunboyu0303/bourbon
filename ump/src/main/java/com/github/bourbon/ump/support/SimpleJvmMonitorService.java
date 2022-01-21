package com.github.bourbon.ump.support;

import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.extension.support.AbstractLifecycle;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.ump.JvmInfoPicker;
import com.github.bourbon.ump.JvmMonitorService;
import com.github.bourbon.ump.Reporter;
import com.github.bourbon.ump.RuntimeEnvironmentService;
import com.github.bourbon.ump.constant.UMPConstants;
import com.github.bourbon.ump.domain.JvmEnvironmentInfo;
import com.github.bourbon.ump.domain.JvmRuntimeInfo;
import com.github.bourbon.ump.domain.LogType;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 23:22
 */
public class SimpleJvmMonitorService extends AbstractLifecycle implements JvmMonitorService {

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final AtomicReference<JvmEnvironmentInfo> jvmEnvironmentInfo = new AtomicReference<>();
    private final AtomicReference<JvmRuntimeInfo> jvmRuntimeInfo = new AtomicReference<>();

    @Inject
    private JvmInfoPicker picker;

    @Inject
    private Reporter reporter;

    @Inject
    private RuntimeEnvironmentService runtimeEnvironmentService;

    private ScheduledExecutorService executorService;

    @Override
    public void initJvm(String key) {
        if (initialized.compareAndSet(false, true)) {
            String ip = runtimeEnvironmentService.getIp();
            String hostName = runtimeEnvironmentService.getHostName();

            executorService = ExecutorFactory.Managed.newScheduledExecutorService("ump", 2, new NamedThreadFactory("UMP-CollectJvmInfoThread", true));

            executorService.scheduleAtFixedRate(() -> {
                JvmEnvironmentInfo info = picker.jvmEnvironmentInfo();
                jvmEnvironmentInfo.set(info);
                reporter.report(LogType.JVM, "{\"k\":\"" + key + "\",\"ty\":\"1\",\"i\":\"" + ip + "\",\"h\":\"" + hostName + "\",\"d\":" + info + "}");
            }, UMPConstants.DEFAULT_JVM_METRIC_DELAY, UMPConstants.ENVIRONMENT_INFO_REPORT_PERIOD, TimeUnit.MILLISECONDS);

            executorService.scheduleAtFixedRate(() -> {
                JvmRuntimeInfo info = picker.jvmRuntimeInfo();
                jvmRuntimeInfo.set(info);
                reporter.report(LogType.JVM, "{\"k\":\"" + key + "\",\"ty\":\"2\",\"i\":\"" + ip + "\",\"h\":\"" + hostName + "\",\"d\":" + info + "}");
            }, UMPConstants.DEFAULT_JVM_METRIC_DELAY, UMPConstants.DEFAULT_JVM_METRIC_PERIOD, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void doDestroy() {
        if (initialized.get() && closed.compareAndSet(false, true)) {
            executorService.shutdown();
        }
    }

    @Override
    public JvmEnvironmentInfo jvmEnvironmentInfo() {
        return jvmEnvironmentInfo.get();
    }

    @Override
    public JvmRuntimeInfo jvmRuntimeInfo() {
        return jvmRuntimeInfo.get();
    }
}