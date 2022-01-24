package com.github.bourbon.tracer.core.reporter.facade;

import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 10:33
 */
public abstract class AbstractReporter implements Reporter {

    private final AtomicBoolean isClosePrint = new AtomicBoolean(false);

    @Override
    public void report(SofaTracerSpan span) {
        if (span == null) {
            return;
        }
        if (isClosePrint.get()) {
            return;
        }
        this.doReport(span);
    }

    public abstract void doReport(SofaTracerSpan span);

    @Override
    public void close() {
        isClosePrint.set(true);
    }

    public AtomicBoolean getIsClosePrint() {
        return isClosePrint;
    }

    public void setIsClosePrint(AtomicBoolean isClosePrint) {
        if (isClosePrint != null) {
            this.isClosePrint.set(isClosePrint.get());
        }
    }
}