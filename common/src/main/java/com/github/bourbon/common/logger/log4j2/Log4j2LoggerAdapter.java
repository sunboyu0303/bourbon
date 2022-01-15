package com.github.bourbon.common.logger.log4j2;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Level;
import com.github.bourbon.base.logger.LogParamInfo;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerAdapter;
import com.github.bourbon.base.lang.FileSize;
import com.github.bourbon.common.logger.AbstractLoggerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.Charset;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 16:02
 */
public class Log4j2LoggerAdapter extends AbstractLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Log4j2Logger(LogManager.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new Log4j2Logger(LogManager.getLogger(name));
    }

    @Override
    public Logger getLogger(String name, LogParamInfo info) {
        // 为false时，返回多个LoggerContext对象、true：返回唯一的单例LoggerContext
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(String.valueOf(FileSize.valueOf(info.getMaxFileSize())));
        policy.start();

        PatternLayout patternLayout = PatternLayout.newBuilder()
                .withCharset(Charset.forName(info.getCharset()))
                .withPattern(info.getPattern())
                .withConfiguration(config)
                .build();

        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(info.getMaxHistory()))
                .withConfig(config)
                .build();

        RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.newBuilder()
                .setName(name)
                .withImmediateFlush(info.isImmediateFlush())
                .withFileName(info.getFileNamePrefix() + name)
                .withFilePattern(info.getFileNamePrefix() + name + ".%d{yyyy-MM-dd}-%i")
                .setLayout(patternLayout)
                .withPolicy(policy)
                .withStrategy(strategy)
                .setConfiguration(config)
                .build();
        appender.start();
        config.addAppender(appender);

        LoggerConfig loggerConfig = new LoggerConfig();
        loggerConfig.setAdditive(info.isAdditive());
        loggerConfig.setLevel(org.apache.logging.log4j.Level.INFO);
        loggerConfig.start();
        loggerConfig.addAppender(appender, org.apache.logging.log4j.Level.INFO, null);

        config.addLogger(name, loggerConfig);
        ctx.updateLoggers();

        return new Log4j2Logger(ctx.getLogger(name));
    }

    @Override
    public Level getLevel() {
        return fromLog4j2Level(LogManager.getRootLogger().getLevel());
    }

    @Override
    public void setLevel(Level level) {
        Configurator.setLevel(StringConstants.EMPTY, toLog4j2Level(level));
    }

    @Override
    public boolean initialize() {
        try {
            Class.forName("org.apache.logging.log4j.LogManager");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 100;
    }

    private static Level fromLog4j2Level(org.apache.logging.log4j.Level level) {
        if (level == org.apache.logging.log4j.Level.ALL) {
            return Level.ALL;
        }
        if (level == org.apache.logging.log4j.Level.TRACE) {
            return Level.TRACE;
        }
        if (level == org.apache.logging.log4j.Level.DEBUG) {
            return Level.DEBUG;
        }
        if (level == org.apache.logging.log4j.Level.INFO) {
            return Level.INFO;
        }
        if (level == org.apache.logging.log4j.Level.WARN) {
            return Level.WARN;
        }
        if (level == org.apache.logging.log4j.Level.ERROR) {
            return Level.ERROR;
        }
        return Level.OFF;
    }

    private static org.apache.logging.log4j.Level toLog4j2Level(Level level) {
        if (level == Level.ALL) {
            return org.apache.logging.log4j.Level.ALL;
        }
        if (level == Level.TRACE) {
            return org.apache.logging.log4j.Level.TRACE;
        }
        if (level == Level.DEBUG) {
            return org.apache.logging.log4j.Level.DEBUG;
        }
        if (level == Level.INFO) {
            return org.apache.logging.log4j.Level.INFO;
        }
        if (level == Level.WARN) {
            return org.apache.logging.log4j.Level.WARN;
        }
        if (level == Level.ERROR) {
            return org.apache.logging.log4j.Level.ERROR;
        }
        return org.apache.logging.log4j.Level.OFF;
    }
}