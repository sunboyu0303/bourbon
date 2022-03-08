package com.github.bourbon.pfinder.profiler.logging;

import com.github.bourbon.pfinder.profiler.logging.basic.PFinderLoggerConfig;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:23
 */
public class SimpleConfig implements PFinderLoggerConfig {
    private LogLevel logLevel;
    private Properties configProperties;

    public SimpleConfig() {
        try {
            URL configUrl = this.getClass().getClassLoader().getResource("pfinder.logger.properties");
            if (configUrl != null) {
                try (InputStream configIs = configUrl.openStream()) {
                    this.configProperties = new Properties();
                    this.configProperties.load(configIs);
                }
            }
        } catch (Exception e) {
            // ignore
        }

        String logLevelStr = null;
        if (this.configProperties != null) {
            logLevelStr = this.configProperties.getProperty("level");
        }

        if (logLevelStr == null) {
            logLevelStr = System.getProperty("pfinder.log.level");
        }

        if (logLevelStr == null) {
            logLevelStr = System.getenv("PFINDER_LOG_LEVEL");
        }

        this.logLevel = LogLevel.parse(logLevelStr);
    }

    @Override
    public LogLevel logLevel() {
        return this.logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
}