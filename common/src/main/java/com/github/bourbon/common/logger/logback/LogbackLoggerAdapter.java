package com.github.bourbon.common.logger.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.github.bourbon.base.logger.Level;
import com.github.bourbon.base.logger.LogParamInfo;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerAdapter;
import com.github.bourbon.common.logger.AbstractLoggerAdapter;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 12:11
 */
public class LogbackLoggerAdapter extends AbstractLoggerAdapter implements LoggerAdapter {

    private static final String ROOT_LOGGER_NAME = org.slf4j.Logger.ROOT_LOGGER_NAME;

    private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new LogbackLogger(LOGGER_CONTEXT.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new LogbackLogger(LOGGER_CONTEXT.getLogger(name));
    }

    @Override
    public Logger getLogger(String name, LogParamInfo info) {

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setCharset(Charset.forName(info.getCharset()));
        encoder.setPattern(info.getPattern());
        encoder.setImmediateFlush(info.isImmediateFlush());
        encoder.setContext(LOGGER_CONTEXT);
        encoder.start();

        ch.qos.logback.classic.Logger logger = LOGGER_CONTEXT.getLogger(name);
        logger.setAdditive(info.isAdditive());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setName(name);
        rollingFileAppender.setFile(info.getFileNamePrefix() + name);
        rollingFileAppender.setContext(LOGGER_CONTEXT);
        rollingFileAppender.setEncoder(encoder);

        SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        sizeAndTimeBasedRollingPolicy.setParent(rollingFileAppender);
        sizeAndTimeBasedRollingPolicy.setContext(LOGGER_CONTEXT);
        sizeAndTimeBasedRollingPolicy.setCleanHistoryOnStart(true);
        sizeAndTimeBasedRollingPolicy.setMaxHistory(info.getMaxHistory());
        sizeAndTimeBasedRollingPolicy.setMaxFileSize(FileSize.valueOf(info.getMaxFileSize()));
        sizeAndTimeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf(info.getTotalSizeCap()));
        sizeAndTimeBasedRollingPolicy.setFileNamePattern(info.getFileNamePrefix() + name + ".%d{yyyy-MM-dd}-%i");
        sizeAndTimeBasedRollingPolicy.start();

        rollingFileAppender.setRollingPolicy(sizeAndTimeBasedRollingPolicy);
        rollingFileAppender.start();

        logger.addAppender(rollingFileAppender);

        return new LogbackLogger(logger);
    }

    @Override
    public Level getLevel() {
        return fromLogbackLevel(LOGGER_CONTEXT.getLogger(ROOT_LOGGER_NAME).getLevel());
    }

    @Override
    public void setLevel(Level level) {
        LOGGER_CONTEXT.getLogger(ROOT_LOGGER_NAME).setLevel(toLogbackLevel(level));
    }

    @Override
    public int getPriority() {
        return 200;
    }

    private static Level fromLogbackLevel(ch.qos.logback.classic.Level level) {
        if (level == ch.qos.logback.classic.Level.ALL) {
            return Level.ALL;
        }
        if (level == ch.qos.logback.classic.Level.TRACE) {
            return Level.TRACE;
        }
        if (level == ch.qos.logback.classic.Level.DEBUG) {
            return Level.DEBUG;
        }
        if (level == ch.qos.logback.classic.Level.INFO) {
            return Level.INFO;
        }
        if (level == ch.qos.logback.classic.Level.WARN) {
            return Level.WARN;
        }
        if (level == ch.qos.logback.classic.Level.ERROR) {
            return Level.ERROR;
        }
        return Level.OFF;
    }

    private static ch.qos.logback.classic.Level toLogbackLevel(Level level) {
        if (level == Level.ALL) {
            return ch.qos.logback.classic.Level.ALL;
        }
        if (level == Level.TRACE) {
            return ch.qos.logback.classic.Level.TRACE;
        }
        if (level == Level.DEBUG) {
            return ch.qos.logback.classic.Level.DEBUG;
        }
        if (level == Level.INFO) {
            return ch.qos.logback.classic.Level.INFO;
        }
        if (level == Level.WARN) {
            return ch.qos.logback.classic.Level.WARN;
        }
        if (level == Level.ERROR) {
            return ch.qos.logback.classic.Level.ERROR;
        }
        return ch.qos.logback.classic.Level.OFF;
    }
}