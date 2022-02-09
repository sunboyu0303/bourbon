package com.github.bourbon.tracer.core.reporter.stat;

import com.github.bourbon.tracer.core.reporter.stat.model.StatKey;
import com.github.bourbon.tracer.core.reporter.stat.model.StatValues;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 11:29
 */
public interface SofaTracerStatisticReporter {

    long getPeriodTime();

    String getStatTracerName();

    void reportStat(SofaTracerSpan sofaTracerSpan);

    Map<StatKey, StatValues> shiftCurrentIndex();

    /**
     * When the method is called, it indicates that a cycle has passed, to determine whether enough cycles have passed, and whether flush is needed.
     *
     * @return true:stat log can be printed and the framework will call {@link SofaTracerStatisticReporter#print}
     */
    boolean shouldPrintNow();

    /**
     * Print, you can print to a local disk, or you can report to a remote server
     */
    void print(StatKey statKey, long[] values);

    void close();
}