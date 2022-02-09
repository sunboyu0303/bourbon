package com.github.bourbon.tracer.core.reporter.stat.manager;

import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.reporter.stat.SofaTracerStatisticReporter;
import com.github.bourbon.tracer.core.reporter.stat.model.StatKey;
import com.github.bourbon.tracer.core.reporter.stat.model.StatValues;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 13:56
 */
public class SofaTracerStatisticReporterManager {
    /**
     * Threshold, if the number of keys in the stat log (map format) exceeds this value, the map is cleared.
     */
    private static final int CLEAR_STAT_KEY_THRESHOLD = 5000;
    /**
     * The default output period is 60 seconds.
     */
    public static final long DEFAULT_CYCLE_SECONDS = 60;

    private final Map<String, SofaTracerStatisticReporter> statReporters = MapUtils.newConcurrentHashMap();

    SofaTracerStatisticReporterManager() {
        this(DEFAULT_CYCLE_SECONDS);
    }

    SofaTracerStatisticReporterManager(long cycleTime) {
        ExecutorFactory.Managed.newSingleScheduledExecutorService("tracer", new SofaTracerStatisticReporterThreadFactory(cycleTime))
                .scheduleAtFixedRate(new StatReporterPrinter(), 0, cycleTime, TimeUnit.SECONDS);
    }

    public SofaTracerStatisticReporter getStatTracer(String statTracerName) {
        return BooleanUtils.defaultIfPredicate(statTracerName, CharSequenceUtils::isNotBlank, statReporters::get);
    }

    public void addStatReporter(SofaTracerStatisticReporter statisticReporter) {
        ObjectUtils.nonNullConsumer(statisticReporter, r -> statReporters.computeIfAbsent(r.getStatTracerName(), o -> r));
    }

    public Map<String, SofaTracerStatisticReporter> getStatReporters() {
        return statReporters;
    }

    class StatReporterPrinter implements Runnable {
        @Override
        public void run() {
            SofaTracerStatisticReporter st = null;
            try {
                for (SofaTracerStatisticReporter statTracer : statReporters.values()) {
                    if (statTracer.shouldPrintNow()) {
                        st = statTracer;
                        Map<StatKey, StatValues> statDatas = statTracer.shiftCurrentIndex();
                        statDatas.forEach((k, v) -> {
                            // print log
                            long[] tobePrint = v.getCurrentValue();
                            // print when the count is greater than 0
                            if (tobePrint[0] > 0) {
                                statTracer.print(k, tobePrint);
                            }
                            // Update the slot value to clear the printed content
                            // Here you must ensure that the input params is the value of the array used in the print process.
                            v.clear(tobePrint);
                        });
                        // If the number of keys in the statistics log is greater than the threshold,
                        // it indicates that the key may have variable parameters,
                        // so clearing it prevents taking up too much memory.
                        if (statDatas.size() > CLEAR_STAT_KEY_THRESHOLD) {
                            statDatas.clear();
                        }
                    }
                }
            } catch (Throwable t) {
                if (st != null) {
                    SelfLog.error("Stat log <" + st.getStatTracerName() + "> flush failure.", t);
                }
            }
        }
    }
}