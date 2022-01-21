package com.github.bourbon.ump.support;

import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.extension.support.AbstractLifecycle;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.ump.AliveMonitorService;
import com.github.bourbon.ump.Reporter;
import com.github.bourbon.ump.constant.UMPConstants;
import com.github.bourbon.ump.domain.LogType;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 23:04
 */
public class SimpleAliveMonitorService extends AbstractLifecycle implements AliveMonitorService {

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private ScheduledExecutorService executorService;

    @Inject
    private Reporter reporter;

    @Override
    public void initSystemAlive(String key) {
        if (initialized.compareAndSet(false, true)) {
            String message = "{\"k\":\"" + key + "\"}";
            executorService = ExecutorFactory.Managed.newSingleScheduledExecutorService("ump", new NamedThreadFactory("UMP-AliveThread", true));
            executorService.scheduleAtFixedRate(() -> reporter.report(LogType.ALIVE, message), 1000L, UMPConstants.DEFAULT_ALIVE_METRIC_PERIOD, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void doDestroy() {
        if (initialized.get() && closed.compareAndSet(false, true)) {
            executorService.shutdown();
        }
    }
}