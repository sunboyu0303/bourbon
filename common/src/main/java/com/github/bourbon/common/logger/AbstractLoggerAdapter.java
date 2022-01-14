package com.github.bourbon.common.logger;

import com.github.bourbon.base.logger.Level;
import com.github.bourbon.base.logger.LogParamInfo;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerAdapter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 11:37
 */
public abstract class AbstractLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(String name, LogParamInfo info) {
        return null;
    }

    @Override
    public Level getLevel() {
        return null;
    }

    @Override
    public void setLevel(Level level) {
    }

    @Override
    public boolean initialize() {
        return false;
    }
}