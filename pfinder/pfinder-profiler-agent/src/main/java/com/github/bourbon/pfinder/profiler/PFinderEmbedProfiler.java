package com.github.bourbon.pfinder.profiler;

import com.github.bourbon.pfinder.profiler.consts.SysPropKeys;
import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerFactory;
import com.github.bourbon.pfinder.profiler.utils.RuntimeCheckUtils;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:01
 */
public class PFinderEmbedProfiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PFinderEmbedProfiler.class);

    public static void boot() {
        Properties systemProperties = System.getProperties();
        if (!RuntimeCheckUtils.supportEnhance()) {
            LOGGER.error("Your java version is {}. There is a bug in the Java 8 runtime environment below Java 1.8.0_60. When use agent may cause jvm to crash. Please upgrade your java version. For more information, please see '{}'",
                    System.getProperty("java.version"), "http://tigcms.jd.com/details/rJqlJRG14.html");
            systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "unsupported");
        } else {
            systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "booting");
            PFinderProfiler.boot("", ByteBuddyAgent.install());
            systemProperties.put(SysPropKeys.PFINDER_AGENT_STATUS, "embedded");
        }
    }
}