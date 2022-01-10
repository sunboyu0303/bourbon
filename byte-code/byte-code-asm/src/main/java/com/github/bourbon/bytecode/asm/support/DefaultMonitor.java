package com.github.bourbon.bytecode.asm.support;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.bytecode.core.support.MonitorContext;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/6 14:22
 */
public class DefaultMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMonitor.class);

    public static void start() {
        MonitorContext.set(Clock.currentTimeMillis());
    }

    public static void end() {
        LOGGER.info("execute method use time : " + (Clock.currentTimeMillis() - MonitorContext.get()));
        MonitorContext.clear();
    }
}