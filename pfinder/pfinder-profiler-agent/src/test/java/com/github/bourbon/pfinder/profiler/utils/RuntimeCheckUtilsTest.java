package com.github.bourbon.pfinder.profiler.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 13:36
 */
public class RuntimeCheckUtilsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        logger.info(RuntimeCheckUtils.supportEnhance());
    }
}