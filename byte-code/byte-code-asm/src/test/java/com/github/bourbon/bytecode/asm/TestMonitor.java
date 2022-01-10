package com.github.bourbon.bytecode.asm;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/6 14:40
 */
public class TestMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMonitor.class);

    public static void init() {
        LOGGER.info("init");
    }

    public static void destroy() {
        LOGGER.info("destroy");
    }
}