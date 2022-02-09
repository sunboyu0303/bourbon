package com.github.bourbon.tracer.core.reporter.stat.manager;

import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.reporter.stat.SofaTracerStatisticReporter;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 09:51
 */
public class SofaTracerStatisticReporterCycleTimesManager {

    private static final Map<Long, SofaTracerStatisticReporterManager> cycleTimesManager = MapUtils.newConcurrentHashMap();

    public static Map<Long, SofaTracerStatisticReporterManager> getCycleTimesManager() {
        return cycleTimesManager;
    }

    public static void registerStatReporter(SofaTracerStatisticReporter statisticReporter) {
        ObjectUtils.nonNullConsumer(getSofaTracerStatisticReporterManager(statisticReporter.getPeriodTime()), c -> c.addStatReporter(statisticReporter));
    }

    /**
     * The timed task uses this as the entry: Get the scheduled task with the specified cycle time
     */
    public static SofaTracerStatisticReporterManager getSofaTracerStatisticReporterManager(Long cycleTime) {
        if (cycleTime == null || cycleTime <= 0) {
            return null;
        }
        return cycleTimesManager.computeIfAbsent(cycleTime, SofaTracerStatisticReporterManager::new);
    }
}