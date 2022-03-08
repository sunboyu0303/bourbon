package com.github.bourbon.pfinder.profiler;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.pfinder.profiler.consts.SysPropKeys;
import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerFactory;
import com.github.bourbon.pfinder.profiler.utils.RuntimeCheckUtils;

import java.lang.instrument.Instrumentation;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 22:56
 */
public class PFinderAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(PFinderAgent.class);
    private static final String PFINDER_ON_ENVIRONMENT_UNSUPPORTED_ENV = "PFINDER_ON_ENVIRONMENT_UNSUPPORTED";

    public PFinderAgent() {
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        Properties systemProperties = System.getProperties();
        if (!CharSequenceUtils.isNotEmpty(systemProperties.getProperty(SysPropKeys.PFINDER_AGENT_STATUS))) {
            if (!RuntimeCheckUtils.supportEnhance()) {
                LOGGER.error("!!! Your java version is {}. There is a bug in the Java 8 runtime environment below Java 1.8.0_60. When use agent may cause jvm to crash. Please upgrade your java version . For more information, please see '{}'", new Object[]{System.getProperty("java.version"), "http://tigcms.jd.com/details/rJqlJRG14.html"});
                systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "unsupported");
            } else {
                systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "booting");
                PFinderProfiler.boot(agentArgs, instrumentation);
                systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "attached");
            }
        }
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        Properties systemProperties = System.getProperties();
        if (!CharSequenceUtils.isNotEmpty(systemProperties.getProperty(SysPropKeys.PFINDER_AGENT_STATUS))) {
            if (!RuntimeCheckUtils.supportEnhance()) {
                LOGGER.error("!!! Your java version is {}. There is a bug in the Java 8 runtime environment below Java 1.8.0_60. When use agent may cause jvm to crash. Please upgrade your java version . For more information, please see '{}'", new Object[]{System.getProperty("java.version"), "http://tigcms.jd.com/details/rJqlJRG14.html"});
                if ("skip".equals(System.getenv(PFINDER_ON_ENVIRONMENT_UNSUPPORTED_ENV))) {
                    systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "unsupported");
                    return;
                }
                System.exit(1);
            }
            systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "booting");
            PFinderProfiler.boot(agentArgs, instrumentation);
            systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "premain");
        }
    }
}