package com.github.bourbon.common.logger.log4j;

import com.github.bourbon.base.logger.Level;
import com.github.bourbon.base.logger.LogParamInfo;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerAdapter;
import com.github.bourbon.base.lang.FileSize;
import com.github.bourbon.base.utils.FileTools;
import com.github.bourbon.common.logger.AbstractLoggerAdapter;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 15:58
 */
public class Log4jLoggerAdapter extends AbstractLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Log4jLogger(LogManager.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new Log4jLogger(LogManager.getLogger(name));
    }

    @Override
    public Level getLevel() {
        return fromLog4jLevel(LogManager.getRootLogger().getLevel());
    }

    @Override
    public void setLevel(Level level) {
        LogManager.getRootLogger().setLevel(toLog4jLevel(level));
    }

    @Override
    public Logger getLogger(String name, LogParamInfo info) {

        FileTools.createPathIfNecessary(info.getFileNamePrefix());

        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        rollingFileAppender.setMaxBackupIndex(info.getMaxHistory());
        rollingFileAppender.setMaxFileSize(info.getMaxFileSize());
        rollingFileAppender.setMaximumFileSize(FileSize.valueOf(info.getMaxFileSize()).getSize());
        rollingFileAppender.setImmediateFlush(info.isImmediateFlush());
        rollingFileAppender.setLayout(new PatternLayout());
        rollingFileAppender.setEncoding(info.getCharset());
        rollingFileAppender.setName(name);
        rollingFileAppender.setFile(info.getFileNamePrefix() + name);
        rollingFileAppender.activateOptions();

        org.apache.log4j.Logger logger = LogManager.getLogger(name);
        logger.addAppender(rollingFileAppender);

        return new Log4jLogger(logger);
    }

    @Override
    public boolean initialize() {
        try {
            Class.forName("org.apache.log4j.Priority");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 300;
    }

    private static Level fromLog4jLevel(org.apache.log4j.Level level) {
        if (level == org.apache.log4j.Level.ALL) {
            return Level.ALL;
        }
        if (level == org.apache.log4j.Level.TRACE) {
            return Level.TRACE;
        }
        if (level == org.apache.log4j.Level.DEBUG) {
            return Level.DEBUG;
        }
        if (level == org.apache.log4j.Level.INFO) {
            return Level.INFO;
        }
        if (level == org.apache.log4j.Level.WARN) {
            return Level.WARN;
        }
        if (level == org.apache.log4j.Level.ERROR) {
            return Level.ERROR;
        }
        return Level.OFF;
    }

    private static org.apache.log4j.Level toLog4jLevel(Level level) {
        if (level == Level.ALL) {
            return org.apache.log4j.Level.ALL;
        }
        if (level == Level.TRACE) {
            return org.apache.log4j.Level.TRACE;
        }
        if (level == Level.DEBUG) {
            return org.apache.log4j.Level.DEBUG;
        }
        if (level == Level.INFO) {
            return org.apache.log4j.Level.INFO;
        }
        if (level == Level.WARN) {
            return org.apache.log4j.Level.WARN;
        }
        if (level == Level.ERROR) {
            return org.apache.log4j.Level.ERROR;
        }
        return org.apache.log4j.Level.OFF;
    }
}