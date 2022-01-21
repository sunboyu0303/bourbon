package com.github.bourbon.ump.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.system.OsInfo;
import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.utils.LocalDateTimeUtils;
import com.github.bourbon.base.utils.SystemUtils;
import com.github.bourbon.ump.JvmInfoPicker;
import com.github.bourbon.ump.RuntimeEnvironmentService;
import com.github.bourbon.ump.domain.JvmEnvironmentInfo;
import com.github.bourbon.ump.domain.JvmRuntimeInfo;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 00:51
 */
public class SimpleJvmInfoPicker implements JvmInfoPicker {

    @Inject
    private RuntimeEnvironmentService runtimeEnvironmentService;

    private final GarbageCollectorMXBean youngGC;
    private final GarbageCollectorMXBean fullGC;
    private final String startPath;
    private final String startTime;
    private String inputArguments;
    private long upTime;
    private long processCpuTime;

    public SimpleJvmInfoPicker() {
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = SystemUtils.garbageCollectorMXBeans();
        youngGC = garbageCollectorMXBeans.get(0);
        fullGC = garbageCollectorMXBeans.get(1);

        startPath = SystemInfo.userInfo.getDir().replace(StringConstants.DOUBLE_BACKSLASH, StringConstants.SLASH);
        startTime = LocalDateTimeUtils.localDateTimeFormat(SystemUtils.runtimeMXBean().getStartTime());
    }

    @Override
    public JvmEnvironmentInfo jvmEnvironmentInfo() {
        OsInfo osInfo = SystemInfo.osInfo;
        OperatingSystemMXBean operatingSystemMXBean = SystemUtils.operatingSystemMXBean();
        return JvmEnvironmentInfo.builder()
                .pid(runtimeEnvironmentService.getPid())
                .jrev(SystemInfo.javaInfo.getVersion())
                .osn(osInfo.getName())
                .osa(osInfo.getArch())
                .osap(runtimeEnvironmentService.getCpuProcessors())
                .args(getInputArguments())
                .sp(startPath)
                .st(startTime)
                .tpms(operatingSystemMXBean.getTotalPhysicalMemorySize())
                .tsss(operatingSystemMXBean.getTotalSwapSpaceSize())
                .cvms(operatingSystemMXBean.getCommittedVirtualMemorySize())
                .ygcn(youngGC.getName())
                .fgcn(fullGC.getName())
                .build();
    }

    private String getInputArguments() {
        if (inputArguments != null) {
            return inputArguments;
        }
        List<String> argList = SystemUtils.runtimeMXBean().getInputArguments();
        StringBuilder sb = new StringBuilder();
        if (argList != null && !argList.isEmpty()) {
            for (String arg : argList) {
                if (arg != null && arg.trim().length() != 0) {
                    if (sb.length() > 0) {
                        sb.append(StringConstants.SPACE);
                    }
                    sb.append(arg.replace(StringConstants.DOUBLE_BACKSLASH, StringConstants.SLASH));
                }
            }
        }
        inputArguments = sb.toString();
        return inputArguments;
    }

    @Override
    public JvmRuntimeInfo jvmRuntimeInfo() {
        final ThreadMXBean threadMXBean = SystemUtils.threadMXBean();
        final ClassLoadingMXBean classLoadingMXBean = SystemUtils.classLoadingMXBean();
        final MemoryUsage nonHeapMemoryUsage = SystemUtils.nonHeapMemoryUsage();
        MemoryUsage heapMemoryUsage = SystemUtils.heapMemoryUsage();
        return JvmRuntimeInfo.builder()
                .ptc(threadMXBean.getPeakThreadCount())
                .tc(threadMXBean.getThreadCount())
                .dtc(threadMXBean.getDaemonThreadCount())
                .lcc(classLoadingMXBean.getLoadedClassCount())
                .tlcc(classLoadingMXBean.getTotalLoadedClassCount())
                .ucc(classLoadingMXBean.getUnloadedClassCount())
                .nhmu(nonHeapMemoryUsage.getUsed())
                .hmu(heapMemoryUsage.getUsed())
                .inhmu(nonHeapMemoryUsage.getInit())
                .ihmu(heapMemoryUsage.getInit())
                .cnhmu(nonHeapMemoryUsage.getCommitted())
                .chmu(heapMemoryUsage.getCommitted())
                .mnhmu(nonHeapMemoryUsage.getMax())
                .mhmu(heapMemoryUsage.getMax())
                .fgcc(fullGC.getCollectionCount())
                .ygcc(youngGC.getCollectionCount())
                .fgct(fullGC.getCollectionTime())
                .ygct(youngGC.getCollectionTime())
                .cpu(getCpu())
                .memory((double) heapMemoryUsage.getUsed() / heapMemoryUsage.getMax())
                .build();
    }

    private double getCpu() {
        long upTimeNow = SystemUtils.runtimeMXBean().getUptime();
        long processCpuTimeNow = SystemUtils.operatingSystemMXBean().getProcessCpuTime();
        double cpu = 0.0d;
        if (upTime > 0L && processCpuTime > 0L) {
            long l2 = upTimeNow - upTime;
            long l1 = processCpuTimeNow - processCpuTime;
            if (l2 > 0L) {
                cpu = Math.min(99.0D, (double) l1 / (l2 * 10000.0D * runtimeEnvironmentService.getCpuProcessors()));
            }
        }
        upTime = upTimeNow;
        processCpuTime = processCpuTimeNow;
        return cpu;
    }
}