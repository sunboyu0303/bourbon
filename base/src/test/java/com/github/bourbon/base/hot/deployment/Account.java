package com.github.bourbon.base.hot.deployment;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.TimeUnitUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/9 21:57
 */
public class Account {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void operation() {
        logger.info("operation...");
        TimeUnitUtils.sleepMilliSeconds(10);
    }
}