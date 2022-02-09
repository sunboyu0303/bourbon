package com.github.bourbon.tracer.core.reporter.stat;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.base.appender.builder.JsonStringBuilder;
import com.github.bourbon.base.appender.builder.XStringBuilder;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.tracer.core.appender.file.LoadTestAwareAppender;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.configuration.SofaTracerConfiguration;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.reporter.stat.manager.SofaTracerStatisticReporterCycleTimesManager;
import com.github.bourbon.tracer.core.reporter.stat.manager.SofaTracerStatisticReporterManager;
import com.github.bourbon.tracer.core.reporter.stat.model.StatKey;
import com.github.bourbon.tracer.core.reporter.stat.model.StatMapKey;
import com.github.bourbon.tracer.core.reporter.stat.model.StatValues;
import com.github.bourbon.tracer.core.span.CommonSpanTags;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 13:50
 */
public abstract class AbstractSofaTracerStatisticReporter implements SofaTracerStatisticReporter {
    /**
     * The default period is 0 (starting at 0), that is, the output interval is a cycle time (how long a cycle can be set, the default is 60s),
     */
    private static final int DEFAULT_CYCLE = 0;
    /**
     * Used to control the concurrency when initializing the slot
     */
    private static final ReentrantLock INIT_LOCK = new ReentrantLock(false);
    private static final XStringBuilder BUFFER = new XStringBuilder();
    private static final JsonStringBuilder JSON_BUFFER = new JsonStringBuilder();
    /**
     * Whether to turn off stat log print, the default is not closed
     */
    protected final AtomicBoolean isClosePrint = new AtomicBoolean(false);
    protected final TraceAppender appender;
    /**
     * The name of the stat log
     */
    protected final String statTracerName;
    /**
     * period time (Unit:second)
     */
    private final long periodTime;
    /**
     * Output cycle interval
     */
    private final int printCycle;
    /**
     * The number of cycles currently counted
     */
    private final AtomicLong countCycle = new AtomicLong(0);
    /**
     * "Statistics" scrolling array
     */
    private final Map<StatKey, StatValues>[] statDatasPair = ArrayUtils.newArray(ConcurrentHashMap.class, 2);
    /**
     * The current subscript of the "statistics" scrolling array
     */
    private int currentIndex = 0;
    protected Map<StatKey, StatValues> statDatas;

    protected AbstractSofaTracerStatisticReporter(String statTracerName, String rollingPolicy, String logReserveConfig) {
        this(statTracerName, SofaTracerStatisticReporterManager.DEFAULT_CYCLE_SECONDS, DEFAULT_CYCLE, rollingPolicy, logReserveConfig);
    }

    protected AbstractSofaTracerStatisticReporter(String statTracerName, long periodTime, int outputCycle, String rollingPolicy, String logReserveConfig) {
        Assert.notBlank(statTracerName, "Statistics tracer name cat't be empty.");
        this.statTracerName = statTracerName;
        this.periodTime = globalConfiguredCycleTime(periodTime);
        this.printCycle = outputCycle;
        for (int i = 0; i < statDatasPair.length; i++) {
            this.statDatasPair[i] = MapUtils.newConcurrentHashMap(128);
        }
        this.statDatas = statDatasPair[currentIndex];
        // Register a scheduled task and start
        SofaTracerStatisticReporterCycleTimesManager.registerStatReporter(this);
        this.appender = LoadTestAwareAppender.createLoadTestAwareTimedRollingFileAppender(statTracerName, rollingPolicy, logReserveConfig);
    }

    /**
     * Get the output interval of the stat log
     *
     * @param defaultCycle default interval is 60s
     */
    private long globalConfiguredCycleTime(long defaultCycle) {
        long cycleTime = defaultCycle;
        try {
            String statLogInterval = SofaTracerConfiguration.getProperty(SofaTracerConfiguration.STAT_LOG_INTERVAL);
            if (CharSequenceUtils.isNotBlank(statLogInterval)) {
                cycleTime = Long.parseLong(statLogInterval);
            }
        } catch (Exception e) {
            SelfLog.error("Parse stat log interval configure error", e);
        }
        SelfLog.warn(getStatTracerName() + " configured " + SofaTracerConfiguration.STAT_LOG_INTERVAL + "=" + cycleTime + " second and default cycle=" + defaultCycle);
        return cycleTime;
    }

    @Override
    public long getPeriodTime() {
        return periodTime;
    }

    @Override
    public String getStatTracerName() {
        return statTracerName;
    }

    @Override
    public void reportStat(SofaTracerSpan sofaTracerSpan) {
        ObjectUtils.nonNullConsumer(sofaTracerSpan, this::doReportStat);
    }

    public abstract void doReportStat(SofaTracerSpan sofaTracerSpan);

    protected void addStat(StatKey keys, long... values) {
        StatValues oldValues = statDatas.get(keys);
        if (oldValues == null) {
            INIT_LOCK.lock();
            try {
                oldValues = statDatas.get(keys);
                if (null == oldValues) {
                    statDatas.put(keys, new StatValues(values));
                    return;
                }
            } finally {
                INIT_LOCK.unlock();
            }
        }
        // Other threads have created slots and merge new data
        oldValues.update(values);
    }

    @Override
    public Map<StatKey, StatValues> shiftCurrentIndex() {
        Map<StatKey, StatValues> last = statDatasPair[currentIndex];
        currentIndex = 1 - currentIndex;
        statDatas = statDatasPair[currentIndex];
        return last;
    }

    public Map<StatKey, StatValues> getStatData() {
        return MapUtils.newHashMap(statDatas);
    }

    public Map<StatKey, StatValues> getOtherStatData() {
        return MapUtils.newHashMap(statDatasPair[1 - currentIndex]);
    }

    @Override
    public boolean shouldPrintNow() {
        if (countCycle.get() >= printCycle) {
            countCycle.set(0);
            return true;
        }
        countCycle.incrementAndGet();
        return false;
    }

    @Override
    public void print(StatKey statKey, long[] values) {
        if (isClosePrint.get()) {
            // Close the statistics log output
            return;
        }
        if (SofaTracerConfiguration.isJsonOutput()) {
            printJsbStat(statKey, values);
        } else {
            printXsbStat(statKey, values);
        }
    }

    protected void printXsbStat(StatKey statKey, long[] values) {
        try {
            BUFFER.reset();
            BUFFER.append(Timestamp.currentTime()).append(statKey.getKey());
            int i = 0;
            for (; i < values.length - 1; i++) {
                BUFFER.append(values[i]);
            }
            BUFFER.append(values[i]).append(statKey.getResult()).appendEnd(statKey.getEnd());
            if (appender instanceof LoadTestAwareAppender) {
                ((LoadTestAwareAppender) appender).append(BUFFER.toString(), statKey.isLoadTest());
            } else {
                appender.append(BUFFER.toString());
            }
            // Forced to flush
            appender.flush();
        } catch (Throwable t) {
            SelfLog.error("Stat log <" + statTracerName + "> output error!", t);
        }
    }

    protected void printJsbStat(StatKey statKey, long[] values) {
        if (!(statKey instanceof StatMapKey)) {
            return;
        }
        StatMapKey statMapKey = (StatMapKey) statKey;
        try {
            JSON_BUFFER.reset();
            JSON_BUFFER.appendBegin()
                    .append(CommonSpanTags.TIME, Timestamp.currentTime())
                    .append(CommonSpanTags.STAT_KEY, statKeySplit(statMapKey))
                    .append(CommonSpanTags.COUNT, values[0])
                    .append(CommonSpanTags.TOTAL_COST_MILLISECONDS, values[1])
                    .append(CommonSpanTags.SUCCESS, statMapKey.getResult())
                    .appendEnd(CommonSpanTags.LOAD_TEST, statMapKey.getEnd());
            if (appender instanceof LoadTestAwareAppender) {
                ((LoadTestAwareAppender) appender).append(JSON_BUFFER.toString(), statMapKey.isLoadTest());
            } else {
                appender.append(JSON_BUFFER.toString());
            }
            // Forced to flush
            appender.flush();
        } catch (Throwable t) {
            SelfLog.error("Stat log<" + statTracerName + "> error!", t);
        }
    }

    private String statKeySplit(StatMapKey statKey) {
        JsonStringBuilder jsonBufferKey = new JsonStringBuilder();
        jsonBufferKey.appendBegin();
        statKey.getKeyMap().forEach(jsonBufferKey::append);
        jsonBufferKey.appendEnd(false);
        return jsonBufferKey.toString();
    }

    @Override
    public void close() {
        isClosePrint.set(true);
    }

    public AtomicBoolean getIsClosePrint() {
        return isClosePrint;
    }

    public void setIsClosePrint(AtomicBoolean isClosePrint) {
        ObjectUtils.nonNullConsumer(isClosePrint, atomic -> this.isClosePrint.set(atomic.get()));
    }

    protected String buildString(String[] keys) {
        XStringBuilder sb = new XStringBuilder();
        int i;
        for (i = 0; i < keys.length - 1; i++) {
            sb.append(ObjectUtils.defaultIfNull(keys[i], StringConstants.EMPTY));
        }
        sb.appendRaw(ObjectUtils.defaultIfNull(keys[i], StringConstants.EMPTY));
        return sb.toString();
    }

    protected boolean isHttpOrMvcSuccess(String resultCode) {
        return resultCode.charAt(0) == '1' || resultCode.charAt(0) == '2' || "302".equals(resultCode.trim()) || ("301".equals(resultCode.trim()));
    }

    protected boolean isWebHttpClientSuccess(String resultCode) {
        return CharSequenceUtils.isNotBlank(resultCode) && (isHttpOrMvcSuccess(resultCode) || SofaTracerConstants.RESULT_CODE_SUCCESS.equals(resultCode));
    }

    protected boolean isMQSimpleSuccess(String resultCode) {
        return CharSequenceUtils.isNotBlank(resultCode) && SofaTracerConstants.RESULT_CODE_SUCCESS.equals(resultCode);
    }
}