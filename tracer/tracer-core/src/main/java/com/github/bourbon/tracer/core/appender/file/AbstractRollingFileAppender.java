package com.github.bourbon.tracer.core.appender.file;

import com.github.bourbon.base.appender.TraceAppender;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.tracer.core.appender.TracerLogRootDaemon;
import com.github.bourbon.tracer.core.appender.self.SelfLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 11:37
 */
public abstract class AbstractRollingFileAppender implements TraceAppender {

    private final AtomicBoolean isRolling = new AtomicBoolean(false);

    private final AtomicBoolean isFlushing = new AtomicBoolean(false);

    private final int bufferSize;

    protected final String fileName;

    protected final File logFile;

    protected BufferedOutputStream bos;

    private volatile long nextFlushTime = 0L;

    protected AbstractRollingFileAppender(String file, boolean append) {
        this(file, 8 * 1024, append);
    }

    protected AbstractRollingFileAppender(String file, int bufferSize, boolean append) {
        this.fileName = TracerLogRootDaemon.LOG_FILE_DIR + File.separator + file;
        this.bufferSize = bufferSize;
        this.logFile = new File(fileName);
        setFile(append);
    }

    protected void setFile(boolean append) {
        try {
            if (!logFile.exists()) {
                File parentFile = logFile.getParentFile();
                if (!parentFile.exists() && !parentFile.mkdirs()) {
                    SelfLog.error("Fail to mkdirs: " + parentFile.getAbsolutePath());
                    return;
                }
                if (!logFile.createNewFile()) {
                    SelfLog.error("Fail to create file to write: " + logFile.getAbsolutePath());
                    return;
                }
            }
            if (!logFile.isFile() || !logFile.canWrite()) {
                SelfLog.error("Invalid file, exists=" + logFile.exists() + ", isFile=" + logFile.isFile() + ", canWrite=" + logFile.canWrite() + ", path=" + logFile.getAbsolutePath());
                return;
            }
            bos = new BufferedOutputStream(new FileOutputStream(logFile, append), bufferSize);
        } catch (Throwable e) {
            SelfLog.error("setFile error", e);
        }
    }

    @Override
    public void append(String log) throws IOException {
        if (bos != null) {
            waitUntilRollFinish();
            if (shouldRollOverNow() && isRolling.compareAndSet(false, true)) {
                try {
                    rollOver();
                    nextFlushTime = getNextFlushTime();
                } finally {
                    isRolling.set(false);
                }
            } else {
                long now = Clock.currentTimeMillis();
                if (now >= nextFlushTime && isFlushing.compareAndSet(false, true)) {
                    try {
                        flush();
                        nextFlushTime = getNextFlushTime(now);
                    } finally {
                        isFlushing.set(false);
                    }
                }
            }
            write(log.getBytes(Charset.defaultCharset()));
        }
    }

    private void waitUntilRollFinish() {
        while (isRolling.get()) {
            TimeUnitUtils.sleepMilliSeconds(1L);
        }
    }

    protected abstract boolean shouldRollOverNow();

    protected abstract void rollOver();

    private long getNextFlushTime() {
        return getNextFlushTime(Clock.currentTimeMillis());
    }

    private long getNextFlushTime(long now) {
        return now + 1_000L;
    }

    @Override
    public void flush() {
        if (bos != null) {
            try {
                bos.flush();
            } catch (IOException e) {
                SelfLog.error("Failed to flush file " + fileName, e);
            }
        }
    }

    private void write(byte[] bytes) {
        if (bos != null) {
            try {
                bos.write(bytes);
            } catch (IOException e) {
                SelfLog.error("Failed to write file " + fileName, e);
            }
        }
    }
}