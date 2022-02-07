package com.github.bourbon.tracer.core.reporter.digest.manager;

import com.github.bourbon.tracer.core.appender.manager.AsyncCommonDigestAppenderManager;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/8 13:45
 */
public final class SofaTracerDigestReporterAsyncManager {

    private static volatile AsyncCommonDigestAppenderManager asyncCommonDigestAppenderManager;

    public static AsyncCommonDigestAppenderManager getSofaTracerDigestReporterAsyncManager() {
        if (asyncCommonDigestAppenderManager == null) {
            synchronized (SofaTracerDigestReporterAsyncManager.class) {
                if (asyncCommonDigestAppenderManager == null) {
                    asyncCommonDigestAppenderManager = new AsyncCommonDigestAppenderManager(1024).start("NetworkAppender");
                }
            }
        }
        return asyncCommonDigestAppenderManager;
    }
}