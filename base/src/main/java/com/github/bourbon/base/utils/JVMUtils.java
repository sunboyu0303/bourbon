package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 17:56
 */
public final class JVMUtils {

    private static final Logger log = LoggerFactory.getLogger(JVMUtils.class);

    private static final String USER_HOME = SystemInfo.userInfo.getHome();

    static {
        FileTools.createPathIfNecessary(USER_HOME);
    }

    private static final AtomicLong lastPrintTime = new AtomicLong(0);

    private static final Semaphore guard = new Semaphore(1);

    private static final ExecutorService pool = ThreadPoolExecutorFactory.newFixedThreadPool("base", 1, new NamedThreadFactory("JVMUtils", true));

    public static void dumpJStack() {
        if (SystemClock.currentTimeMillis() - lastPrintTime.get() < 600_000L) {
            return;
        }
        if (!guard.tryAcquire()) {
            return;
        }
        pool.execute(() -> {
            try (FileOutputStream jStackStream = new FileOutputStream(new File(USER_HOME, "_JStack.log." + LocalDateTimeUtils.localDateTimeNowFormat()))) {
                jStack(jStackStream);
            } catch (Exception t) {
                log.error("dump jStack error", t);
            } finally {
                guard.release();
            }
            lastPrintTime.set(SystemClock.currentTimeMillis());
        });
    }

    private static void jStack(OutputStream stream) throws Exception {
        for (ThreadInfo threadInfo : SystemUtils.threadMXBean().dumpAllThreads(true, true)) {
            stream.write(getThreadDumpString(threadInfo).getBytes());
        }
    }

    private static String getThreadDumpString(ThreadInfo threadInfo) {
        StringBuilder sb = new StringBuilder("\"" + threadInfo.getThreadName() + "\"" + " Id=" + threadInfo.getThreadId() + " " + threadInfo.getThreadState());
        if (threadInfo.getLockName() != null) {
            sb.append(" on " + threadInfo.getLockName());
        }
        if (threadInfo.getLockOwnerName() != null) {
            sb.append(" owned by \"" + threadInfo.getLockOwnerName() + "\" Id=" + threadInfo.getLockOwnerId());
        }
        if (threadInfo.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (threadInfo.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;
        StackTraceElement[] stackTrace = threadInfo.getStackTrace();
        MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
        for (; i < stackTrace.length && i < 32; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat " + ste.toString() + '\n');
            if (i == 0 && threadInfo.getLockInfo() != null) {
                switch (threadInfo.getThreadState()) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + threadInfo.getLockInfo() + '\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + threadInfo.getLockInfo() + '\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + threadInfo.getLockInfo() + '\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : lockedMonitors) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked " + mi + "\n");
                }
            }
        }
        if (i < stackTrace.length) {
            sb.append("\t... \n");
        }

        LockInfo[] locks = threadInfo.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = " + locks.length + '\n');
            for (LockInfo li : locks) {
                sb.append("\t- " + li + '\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    private JVMUtils() {
    }
}