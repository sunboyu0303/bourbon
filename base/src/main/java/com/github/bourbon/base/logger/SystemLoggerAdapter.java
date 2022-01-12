package com.github.bourbon.base.logger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 22:58
 */
public class SystemLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    @Override
    public Logger getLogger(String name) {
        return new SystemLogger();
    }

    @Override
    public Logger getLogger(String name, LogParamInfo info) {
        return getLogger(name);
    }

    @Override
    public Level getLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLevel(Level level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean initialize() {
        return true;
    }
}