package com.github.bourbon.base.thread;

import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.thread.log.ThreadLogger;
import com.github.bourbon.base.thread.space.ThreadPoolSpace;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 15:40
 */
public final class ThreadPoolGovernor {

    private ThreadPoolGovernor() {
    }

    private static final ThreadPoolGovernor INSTANCE = new ThreadPoolGovernor();

    private static final String CLASS_NAME = ThreadPoolGovernor.class.getCanonicalName();

    private final ScheduledExecutorService governorScheduler = ExecutorFactory.Managed.newSingleScheduledExecutorService("base", new NamedThreadFactory("SOFA-Thread-Pool-Governor"));

    private final ScheduledExecutorService monitorScheduler = ExecutorFactory.Managed.newScheduledExecutorService("base", SystemInfo.runtimeInfo.availableProcessors() + 1, new NamedThreadFactory("SOFA-Thread-Pool-Monitor"));

    private final Object monitor = new Object();

    private final GovernorInfoDumper governorInfoDumper = new GovernorInfoDumper();

    private final Map<String, ThreadPoolMonitorWrapper> registry = MapUtils.newConcurrentHashMap();

    private final Map<String, ThreadPoolSpace> spaceNameMap = MapUtils.newConcurrentHashMap();

    private volatile long governorPeriod = SofaThreadPoolConstants.DEFAULT_GOVERNOR_INTERVAL;

    private volatile boolean governorLoggable = SofaThreadPoolConstants.DEFAULT_GOVERNOR_LOGGER_ENABLE;

    private volatile boolean globalMonitorLoggable = SofaThreadPoolConstants.DEFAULT_GLOBAL_MONITOR_LOGGER_ENABLE;

    private ScheduledFuture<?> governorScheduledFuture;

    public static ThreadPoolGovernor getInstance() {
        return INSTANCE;
    }

    public void startGovernorSchedule() {
        synchronized (monitor) {
            if (governorScheduledFuture == null) {
                governorScheduledFuture = governorScheduler.scheduleAtFixedRate(governorInfoDumper, governorPeriod, governorPeriod, TimeUnit.SECONDS);
                ThreadLogger.info("Started {} with period: {} SECONDS", CLASS_NAME, governorPeriod);
            } else {
                ThreadLogger.warn("{} has already started with period: {} SECONDS.", CLASS_NAME, governorPeriod);
            }
        }
    }

    public void stopGovernorSchedule() {
        synchronized (monitor) {
            if (governorScheduledFuture != null) {
                governorScheduledFuture.cancel(true);
                governorScheduledFuture = null;
                ThreadLogger.info("Stopped {}.", CLASS_NAME);
            } else {
                ThreadLogger.warn("{} is not scheduling!", CLASS_NAME);
            }
        }
    }

    private void restartGovernorSchedule() {
        synchronized (monitor) {
            if (governorScheduledFuture != null) {
                governorScheduledFuture.cancel(true);
                governorScheduledFuture = governorScheduler.scheduleAtFixedRate(governorInfoDumper, governorPeriod, governorPeriod, TimeUnit.SECONDS);
                ThreadLogger.info("Reschedule {} with period: {} SECONDS", CLASS_NAME, governorPeriod);
            }
        }
    }

    class GovernorInfoDumper implements Runnable {
        @Override
        public void run() {
            try {
                if (governorLoggable) {
                    registry.forEach((k, v) -> ThreadLogger.info("Thread pool '{}' exists with instance: {}", k, v.getThreadPoolExecutor()));
                }
            } catch (Throwable e) {
                ThreadLogger.warn("{} is interrupted when running: {}", this, e);
            }
        }
    }

    public long getGovernorPeriod() {
        return governorPeriod;
    }

    public void setGovernorPeriod(long governorPeriod) {
        this.governorPeriod = governorPeriod;
        restartGovernorSchedule();
    }

    public boolean isGovernorLoggable() {
        return governorLoggable;
    }

    public void setGovernorLoggable(boolean governorLoggable) {
        this.governorLoggable = governorLoggable;
    }

    public boolean isGlobalMonitorLoggable() {
        return globalMonitorLoggable;
    }

    public void setGlobalMonitorLoggable(boolean globalMonitorLoggable) {
        this.globalMonitorLoggable = globalMonitorLoggable;
    }

    public void registerThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor, ThreadPoolConfig threadPoolConfig, ThreadPoolStatistics threadPoolStatistics) {
        final String identity = threadPoolConfig.getIdentity();
        if (CharSequenceUtils.isEmpty(identity)) {
            ThreadLogger.error("Rejected registering request of instance {} with empty name: {}.", threadPoolExecutor, identity);
            return;
        }
        ThreadPoolMonitorWrapper threadPoolMonitorWrapper = registry.get(identity);
        if (threadPoolMonitorWrapper != null) {
            ThreadLogger.error("Rejected registering request of instance {} with duplicate name: {}", threadPoolExecutor, identity);
            return;
        }
        threadPoolMonitorWrapper = new ThreadPoolMonitorWrapper(threadPoolExecutor, threadPoolConfig, threadPoolStatistics);
        if (registry.putIfAbsent(identity, threadPoolMonitorWrapper) == null) {
            threadPoolMonitorWrapper.startMonitor();
            ThreadLogger.info("Thread pool with name '{}' registered", identity);
            String spaceName = threadPoolConfig.getSpaceName();
            if (!CharSequenceUtils.isEmpty(spaceName)) {
                spaceNameMap.computeIfAbsent(spaceName, k -> new ThreadPoolSpace()).addThreadPool(identity);
            }
        }
    }

    public void unregisterThreadPoolExecutor(ThreadPoolConfig threadPoolConfig) {
        final String identity = threadPoolConfig.getIdentity();
        if (CharSequenceUtils.isEmpty(identity)) {
            ThreadLogger.error("Thread pool with empty name unregistered, may cause memory leak");
            return;
        }
        ThreadPoolMonitorWrapper threadPoolMonitorWrapper = registry.remove(identity);
        if (threadPoolMonitorWrapper != null) {
            threadPoolMonitorWrapper.stopMonitor();
            ThreadLogger.info("Thread pool with name '{}' unregistered", identity);
        }
        final String spaceName = threadPoolConfig.getSpaceName();
        if (!CharSequenceUtils.isEmpty(spaceName)) {
            ThreadPoolSpace space = spaceNameMap.get(spaceName);
            if (space != null) {
                space.removeThreadPool(identity);
            }
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor(String identity) {
        ThreadPoolMonitorWrapper wrapper = registry.get(identity);
        if (wrapper == null) {
            ThreadLogger.warn("Thread pool '{}' is not registered yet", identity);
            return null;
        }
        return wrapper.getThreadPoolExecutor();
    }

    public ThreadPoolMonitorWrapper getThreadPoolMonitorWrapper(String identity) {
        ThreadPoolMonitorWrapper wrapper = registry.get(identity);
        if (wrapper == null) {
            ThreadLogger.warn("Thread pool '{}' is not registered yet", identity);
            return null;
        }
        return wrapper;
    }

    public void startMonitorThreadPool(String identity) {
        ThreadPoolMonitorWrapper wrapper = registry.get(identity);
        if (wrapper == null) {
            ThreadLogger.warn("Thread pool '{}' is not registered yet", identity);
            return;
        }
        wrapper.startMonitor();
    }

    public void stopMonitorThreadPool(String identity) {
        ThreadPoolMonitorWrapper wrapper = registry.get(identity);
        if (wrapper == null) {
            ThreadLogger.warn("Thread pool '{}' is not registered yet", identity);
            return;
        }
        wrapper.stopMonitor();
    }

    public void restartMonitorThreadPool(String identity) {
        ThreadPoolMonitorWrapper wrapper = registry.get(identity);
        if (wrapper == null) {
            ThreadLogger.warn("Thread pool '{}' is not registered yet", identity);
            return;
        }
        wrapper.restartMonitor();
    }

    public ScheduledExecutorService getMonitorScheduler() {
        return monitorScheduler;
    }

    public int getSpaceNameThreadPoolNumber(String spaceName) {
        ThreadPoolSpace threadPoolSpace = spaceNameMap.get(spaceName);
        if (threadPoolSpace == null) {
            ThreadLogger.error("Thread pool with spaceName '{}' is not registered yet, return 0", spaceName);
            return 0;
        }
        return threadPoolSpace.getThreadPoolNumber();
    }

    public void startMonitorThreadPoolBySpaceName(String spaceName) {
        ThreadPoolSpace threadPoolSpace = spaceNameMap.get(spaceName);
        if (threadPoolSpace == null || threadPoolSpace.getThreadPoolIdentities().isEmpty()) {
            ThreadLogger.error("Thread pool with spaceName '{}' is not registered yet", spaceName);
            return;
        }
        threadPoolSpace.getThreadPoolIdentities().forEach(this::startMonitorThreadPool);
        ThreadLogger.info("Thread pool with spaceName '{}' started", spaceName);
    }

    public void stopMonitorThreadPoolBySpaceName(String spaceName) {
        ThreadPoolSpace threadPoolSpace = spaceNameMap.get(spaceName);
        if (threadPoolSpace == null || threadPoolSpace.getThreadPoolIdentities().isEmpty()) {
            ThreadLogger.error("Thread pool with spaceName '{}' is not registered yet", spaceName);
            return;
        }
        threadPoolSpace.getThreadPoolIdentities().forEach(this::stopMonitorThreadPool);
        ThreadLogger.info("Thread pool with spaceName '{}' stopped", spaceName);
    }

    public void setMonitorThreadPoolBySpaceName(String spaceName, long period) {
        ThreadPoolSpace threadPoolSpace = spaceNameMap.get(spaceName);
        if (threadPoolSpace == null || threadPoolSpace.getThreadPoolIdentities().isEmpty()) {
            ThreadLogger.error("Thread pool with spaceName '{}' is not registered yet", spaceName);
            return;
        }
        threadPoolSpace.getThreadPoolIdentities().forEach(i -> {
            ThreadPoolMonitorWrapper wrapper = getThreadPoolMonitorWrapper(i);
            if (wrapper != null) {
                wrapper.getThreadPoolConfig().setPeriod(period);
                restartMonitorThreadPool(i);
            }
        });
        ThreadLogger.info("Thread pool with spaceName '{}' rescheduled with period '{}'", spaceName, period);
    }
}